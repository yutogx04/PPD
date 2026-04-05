package com.example.demo.service;

import com.example.demo.dto.AuthDTOs.*;
import com.example.demo.model.User;
import com.example.demo.model.User.Provider;
import com.example.demo.model.User.Role;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.JwtService;
import com.example.demo.service.GoogleAuthService.GoogleUserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Service principal de la logique métier d'authentification.
 */
@Service
public class AuthService {

  private static final Logger log = LoggerFactory.getLogger(AuthService.class);
  private static final SecureRandom SECURE_RANDOM = new SecureRandom();

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final EmailService emailService;
  private final GoogleAuthService googleAuthService;
  private final TokenBlacklistService tokenBlacklistService;

  @Value("${rate-limit.max-attempts:5}")
  private int maxAttempts;

  @Value("${rate-limit.window-seconds:60}")
  private int windowSeconds;

  // [POURQUOI] ConcurrentHashMap pour le rate limiting en mémoire — suffisant
  // pour une instance unique. Pour le multi-instance, utiliser Redis.
  private final Map<String, RateLimitEntry> rateLimitMap = new ConcurrentHashMap<>();

  public AuthService(UserRepository userRepository,
      PasswordEncoder passwordEncoder,
      JwtService jwtService,
      EmailService emailService,
      GoogleAuthService googleAuthService,
      TokenBlacklistService tokenBlacklistService) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.jwtService = jwtService;
    this.emailService = emailService;
    this.googleAuthService = googleAuthService;
    this.tokenBlacklistService = tokenBlacklistService;
  }

  // ═══════════════════════════════════════════════════════════
  // REGISTER
  // ═══════════════════════════════════════════════════════════

  @Transactional
  public MessageResponse register(RegisterRequest request) {
    // Vérifier l'unicité
    if (userRepository.existsByEmail(request.getEmail())) {
      throw new IllegalArgumentException("Cet email est déjà utilisé");
    }
    if (userRepository.existsByPseudo(request.getPseudo())) {
      throw new IllegalArgumentException("Ce pseudo est déjà pris");
    }

    // Créer l'utilisateur
    User user = new User();
    user.setPseudo(request.getPseudo());
    user.setEmail(request.getEmail());
    // [POURQUOI] BCrypt avec strength 12 — bon compromis sécurité/performance
    user.setPassword(passwordEncoder.encode(request.getPassword()));
    user.setRole(Role.ROLE_USER);
    user.setProvider(Provider.LOCAL);
    user.setEmailVerified(false);

    // Générer et stocker l'OTP
    String otp = generateOtp();
    user.setOtpCode(otp);
    user.setOtpExpiry(LocalDateTime.now().plusMinutes(10));

    userRepository.save(user);

    // Envoyer l'email OTP (async)
    emailService.sendOtpEmail(request.getEmail(), otp, request.getPseudo());

    log.info("Nouvel utilisateur inscrit: {}", request.getEmail());
    return new MessageResponse("OTP envoyé à " + request.getEmail());
  }

  // ═══════════════════════════════════════════════════════════
  // VERIFY OTP
  // ═══════════════════════════════════════════════════════════

  @Transactional
  public AuthResponse verifyOtp(OtpRequest request) {
    User user = userRepository.findByEmail(request.getEmail())
        .orElseThrow(() -> new IllegalArgumentException("Utilisateur non trouvé"));

    validateOtp(user, request.getOtp());

    // Marquer comme vérifié et effacer l'OTP
    user.setEmailVerified(true);
    user.setOtpCode(null);
    user.setOtpExpiry(null);
    userRepository.save(user);

    // Générer les tokens
    return buildAuthResponse(user);
  }

  // ═══════════════════════════════════════════════════════════
  // LOGIN
  // ═══════════════════════════════════════════════════════════

  @Transactional(readOnly = true)
  public AuthResponse login(LoginRequest request, String clientIp) {
    // [POURQUOI] Rate limiting par IP pour prévenir le brute force
    checkRateLimit(clientIp);

    User user = userRepository.findByEmail(request.getEmail())
        .orElseThrow(() -> new IllegalArgumentException("Email ou mot de passe incorrect"));

    // Vérifier que c'est un compte local
    if (user.getProvider() != Provider.LOCAL) {
      throw new IllegalArgumentException(
          "Ce compte utilise la connexion Google. Utilisez le bouton Google pour vous connecter.");
    }

    // Vérifier le mot de passe
    if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
      recordFailedAttempt(clientIp);
      throw new IllegalArgumentException("Email ou mot de passe incorrect");
    }

    // Vérifier la vérification email
    if (!user.isEmailVerified()) {
      throw new IllegalArgumentException("Veuillez vérifier votre email avant de vous connecter");
    }

    // Reset rate limiter après succès
    rateLimitMap.remove(clientIp);

    return buildAuthResponse(user);
  }

  // ═══════════════════════════════════════════════════════════
  // GOOGLE AUTH
  // ═══════════════════════════════════════════════════════════

  @Transactional
  public AuthResponse googleAuth(GoogleAuthRequest request) {
    GoogleUserInfo googleUser = googleAuthService.verifyIdToken(request.getIdToken());

    // Find or create l'utilisateur
    User user = userRepository.findByEmail(googleUser.email())
        .orElseGet(() -> {
          User newUser = new User();
          newUser.setEmail(googleUser.email());
          // [POURQUOI] Utiliser le nom Google comme pseudo, tronqué à 20 chars
          // et nettoyé des caractères non-alphanumériques
          String pseudo = sanitizePseudo(googleUser.name());
          // S'assurer que le pseudo est unique
          pseudo = ensureUniquePseudo(pseudo);
          newUser.setPseudo(pseudo);
          newUser.setProvider(Provider.GOOGLE);
          newUser.setPassword(null); // [POURQUOI] Pas de password pour les comptes Google
          newUser.setRole(Role.ROLE_USER);
          newUser.setEmailVerified(true); // [POURQUOI] Email vérifié par Google
          if (googleUser.pictureUrl() != null) {
            newUser.setAvatarUrl(googleUser.pictureUrl());
          }
          return userRepository.save(newUser);
        });

    // [POURQUOI] Si un utilisateur LOCAL existe avec le même email,
    // on ne fusionne pas les comptes pour éviter les failles de sécurité
    if (user.getProvider() == Provider.LOCAL) {
      throw new IllegalArgumentException(
          "Un compte existe déjà avec cet email. Connectez-vous avec votre mot de passe.");
    }

    return buildAuthResponse(user);
  }

  // ═══════════════════════════════════════════════════════════
  // REFRESH TOKEN
  // ═══════════════════════════════════════════════════════════

  public TokenResponse refreshToken(RefreshRequest request) {
    if (!jwtService.validateRefreshToken(request.getRefreshToken())) {
      throw new IllegalArgumentException("Refresh token invalide ou expiré");
    }

    var claims = jwtService.parseRefreshToken(request.getRefreshToken());
    UUID userId = UUID.fromString(claims.getSubject());

    User user = userRepository.findById(userId)
        .orElseThrow(() -> new IllegalArgumentException("Utilisateur non trouvé"));

    String newAccessToken = jwtService.generateAccessToken(
        user.getId(), user.getEmail(), user.getRole().name());

    return new TokenResponse(newAccessToken);
  }

  // ═══════════════════════════════════════════════════════════
  // FORGOT PASSWORD
  // ═══════════════════════════════════════════════════════════

  @Transactional
  public MessageResponse forgotPassword(ForgotPasswordRequest request) {
    User user = userRepository.findByEmail(request.getEmail())
        .orElseThrow(() -> new IllegalArgumentException("Aucun compte associé à cet email"));

    if (user.getProvider() == Provider.GOOGLE) {
      throw new IllegalArgumentException(
          "Ce compte utilise la connexion Google. La réinitialisation du mot de passe n'est pas disponible.");
    }

    String otp = generateOtp();
    user.setOtpCode(otp);
    user.setOtpExpiry(LocalDateTime.now().plusMinutes(15)); // [POURQUOI] 15 min pour reset vs 10 min pour register
    userRepository.save(user);

    emailService.sendPasswordResetEmail(request.getEmail(), otp, user.getPseudo());

    return new MessageResponse("Un code de réinitialisation a été envoyé à " + request.getEmail());
  }

  // ═══════════════════════════════════════════════════════════
  // RESET PASSWORD
  // ═══════════════════════════════════════════════════════════

  @Transactional
  public MessageResponse resetPassword(ResetPasswordRequest request) {
    User user = userRepository.findByEmail(request.getEmail())
        .orElseThrow(() -> new IllegalArgumentException("Utilisateur non trouvé"));

    validateOtp(user, request.getOtp());

    user.setPassword(passwordEncoder.encode(request.getNewPassword()));
    user.setOtpCode(null);
    user.setOtpExpiry(null);
    userRepository.save(user);

    log.info("Mot de passe réinitialisé pour: {}", request.getEmail());
    return new MessageResponse("Mot de passe réinitialisé avec succès");
  }

  // ═══════════════════════════════════════════════════════════
  // LOGOUT
  // ═══════════════════════════════════════════════════════════

  public MessageResponse logout(String token) {
    try {
      long remainingMs = jwtService.getAccessTokenRemainingMs(token);
      tokenBlacklistService.blacklistToken(token, remainingMs);
      log.info("Token blacklisté avec succès");
    } catch (Exception e) {
      // [POURQUOI] On ne lève pas d'erreur si le token est déjà expiré
      log.warn("Impossible de blacklister le token (probablement expiré): {}", e.getMessage());
    }
    return new MessageResponse("Déconnexion réussie");
  }

  // ═══════════════════════════════════════════════════════════
  // GET ME
  // ═══════════════════════════════════════════════════════════

  public UserResponse getMe(UUID userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new IllegalArgumentException("Utilisateur non trouvé"));

    return new UserResponse(
        user.getId(),
        user.getPseudo(),
        user.getEmail(),
        user.getLevel(),
        user.getXp(),
        user.getStreak(),
        user.getAvatarUrl());
  }

  // ═══════════════════════════════════════════════════════════
  // MÉTHODES PRIVÉES
  // ═══════════════════════════════════════════════════════════

  private AuthResponse buildAuthResponse(User user) {
    String accessToken = jwtService.generateAccessToken(
        user.getId(), user.getEmail(), user.getRole().name());
    String refreshToken = jwtService.generateRefreshToken(user.getId());

    UserResponse userResponse = new UserResponse(
        user.getId(),
        user.getPseudo(),
        user.getEmail(),
        user.getLevel(),
        user.getXp(),
        user.getStreak(),
        user.getAvatarUrl());

    return new AuthResponse(accessToken, refreshToken, userResponse);
  }

  /**
   * Génère un OTP de 6 chiffres.
   * [POURQUOI] SecureRandom au lieu de Random pour la sécurité cryptographique.
   */
  private String generateOtp() {
    int otp = 100000 + SECURE_RANDOM.nextInt(900000);
    return String.valueOf(otp);
  }

  private void validateOtp(User user, String otp) {
    if (user.getOtpCode() == null || user.getOtpExpiry() == null) {
      throw new IllegalArgumentException("Aucun code OTP en attente pour cet utilisateur");
    }
    if (LocalDateTime.now().isAfter(user.getOtpExpiry())) {
      throw new IllegalArgumentException("Le code OTP a expiré. Veuillez en demander un nouveau.");
    }
    if (!user.getOtpCode().equals(otp)) {
      throw new IllegalArgumentException("Code OTP incorrect");
    }
  }

  /**
   * Nettoie un nom Google pour en faire un pseudo valide.
   */
  private String sanitizePseudo(String name) {
    if (name == null || name.isBlank()) {
      return "user" + SECURE_RANDOM.nextInt(99999);
    }
    String sanitized = name.replaceAll("[^a-zA-Z0-9_]", "");
    if (sanitized.length() < 3) {
      sanitized = sanitized + "user" + SECURE_RANDOM.nextInt(999);
    }
    if (sanitized.length() > 20) {
      sanitized = sanitized.substring(0, 20);
    }
    return sanitized;
  }

  /**
   * S'assure que le pseudo est unique en ajoutant un suffixe numérique si
   * nécessaire.
   */
  private String ensureUniquePseudo(String pseudo) {
    String candidate = pseudo;
    int suffix = 1;
    while (userRepository.existsByPseudo(candidate)) {
      String base = pseudo.length() > 16 ? pseudo.substring(0, 16) : pseudo;
      candidate = base + suffix;
      suffix++;
    }
    return candidate;
  }

  // ─── Rate Limiting ───────────────────────────────────────

  private void checkRateLimit(String ip) {
    RateLimitEntry entry = rateLimitMap.get(ip);
    if (entry != null && !entry.isExpired(windowSeconds)) {
      if (entry.attempts.get() >= maxAttempts) {
        throw new IllegalStateException(
            "Trop de tentatives de connexion. Réessayez dans " + windowSeconds + " secondes.");
      }
    }
  }

  private void recordFailedAttempt(String ip) {
    rateLimitMap.compute(ip, (key, existing) -> {
      if (existing == null || existing.isExpired(windowSeconds)) {
        return new RateLimitEntry();
      }
      existing.attempts.incrementAndGet();
      return existing;
    });
  }

  /**
   * [POURQUOI] Classe interne pour stocker les tentatives de connexion par IP.
   * Utilise AtomicInteger pour la thread-safety.
   */
  private static class RateLimitEntry {
    final AtomicInteger attempts = new AtomicInteger(1);
    final long startTimeMs = System.currentTimeMillis();

    boolean isExpired(int windowSeconds) {
      return System.currentTimeMillis() - startTimeMs > (long) windowSeconds * 1000;
    }
  }
}
