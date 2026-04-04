package com.codequest.service;

import com.codequest.dto.AuthResponse;
import com.codequest.dto.LoginRequest;
import com.codequest.dto.RegisterRequest;
import com.codequest.entity.User;
import com.codequest.repository.UserRepository;
import com.codequest.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final OtpService otpService;
    private final EmailService emailService;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email déjà utilisé");
        }
        if (userRepository.existsByPseudo(request.getPseudo())) {
            throw new RuntimeException("Pseudo déjà pris");
        }

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .pseudo(request.getPseudo())
                .xp(0)
                .level(1)
                .streak(0)
                .emailVerified(false)
                .role(User.Role.USER)
                .build();

        user = userRepository.save(user);

        String otp = otpService.generateOtp(user.getEmail());
        emailService.sendOtpEmail(user.getEmail(), otp);

        return buildAuthResponse(user);
    }

    @Transactional
    public AuthResponse verifyEmail(String email, String code) {
        if (!otpService.verifyOtp(email, code)) {
            throw new RuntimeException("Code OTP invalide ou expiré");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        user.setEmailVerified(true);
        userRepository.save(user);

        return buildAuthResponse(user);
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Email ou mot de passe incorrect"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Email ou mot de passe incorrect");
        }

        if (!user.isEnabled()) {
            throw new RuntimeException("Compte désactivé");
        }

        return buildAuthResponse(user);
    }

    public AuthResponse refreshToken(String refreshToken) {
        if (!jwtTokenProvider.validateToken(refreshToken) || !jwtTokenProvider.isRefreshToken(refreshToken)) {
            throw new RuntimeException("Token de rafraîchissement invalide");
        }

        Long userId = jwtTokenProvider.getUserIdFromToken(refreshToken);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        return buildAuthResponse(user);
    }

    public void forgotPassword(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Aucun compte associé à cet email"));

        String otp = otpService.generateOtp(email);
        emailService.sendPasswordResetEmail(email, otp);
    }

    @Transactional
    public AuthResponse googleSignIn(String idToken) {
        try {
            
            com.google.firebase.auth.FirebaseToken decodedToken =
                    com.google.firebase.auth.FirebaseAuth.getInstance().verifyIdToken(idToken);

            String email = decodedToken.getEmail();
            String name = decodedToken.getName();
            String picture = decodedToken.getPicture();

            User user = userRepository.findByEmail(email)
                    .orElseGet(() -> {
                        User newUser = User.builder()
                                .email(email)
                                .pseudo(name != null ? name : email.split("@")[0])
                                .password(passwordEncoder.encode(java.util.UUID.randomUUID().toString()))
                                .avatarUrl(picture)
                                .xp(0)
                                .level(1)
                                .streak(0)
                                .emailVerified(true) 
                                .role(User.Role.USER)
                                .build();
                        return userRepository.save(newUser);
                    });

            if (picture != null && user.getAvatarUrl() == null) {
                user.setAvatarUrl(picture);
                userRepository.save(user);
            }

            if (!user.isEnabled()) {
                throw new RuntimeException("Compte désactivé par un administrateur.");
            }

            return buildAuthResponse(user);
        } catch (Exception e) {
            throw new RuntimeException("Échec de la vérification du token Google: " + e.getMessage());
        }
    }

    private AuthResponse buildAuthResponse(User user) {
        String accessToken = jwtTokenProvider.generateAccessToken(user.getId(), user.getEmail());
        String refreshToken = jwtTokenProvider.generateRefreshToken(user.getId(), user.getEmail());

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .user(AuthResponse.fromEntity(user))
                .build();
    }
}
