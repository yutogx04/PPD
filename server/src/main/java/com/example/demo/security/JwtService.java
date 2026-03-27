package com.example.demo.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.file.Files;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

/**
 * Service JWT pour la génération et validation des tokens.
 * [POURQUOI] RS256 pour l'access token (asymétrique → vérifiable par des services tiers
 * sans exposer la clé privée). HS256 pour le refresh token (symétrique, plus simple,
 * utilisé uniquement côté serveur).
 */
@Service
public class JwtService {

    @Value("${jwt.private-key}")
    private String privateKeyPath;

    @Value("${jwt.public-key}")
    private String publicKeyPath;

    @Value("${jwt.access-token-expiration}")
    private long accessTokenExpiration; // 900000 = 15 min

    @Value("${jwt.refresh-token-expiration}")
    private long refreshTokenExpiration; // 604800000 = 7 jours

    @Value("${jwt.refresh-token-secret}")
    private String refreshTokenSecret;

    private PrivateKey rsaPrivateKey;
    private PublicKey rsaPublicKey;
    private SecretKey hmacSecretKey;

    @PostConstruct
    public void init() {
        try {
            // [POURQUOI] Chargement des clés RSA depuis le classpath au démarrage
            this.rsaPrivateKey = loadPrivateKey(privateKeyPath);
            this.rsaPublicKey = loadPublicKey(publicKeyPath);

            // [POURQUOI] Clé HMAC dérivée du secret pour les refresh tokens
            this.hmacSecretKey = Keys.hmacShaKeyFor(refreshTokenSecret.getBytes());
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors du chargement des clés JWT", e);
        }
    }

    // ─── Access Token (RS256) ────────────────────────────────

    public String generateAccessToken(UUID userId, String email, String role) {
        return Jwts.builder()
                .subject(userId.toString())
                .claim("email", email)
                .claim("role", role)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + accessTokenExpiration))
                .signWith(rsaPrivateKey, Jwts.SIG.RS256)
                .compact();
    }

    public Claims parseAccessToken(String token) {
        return Jwts.parser()
                .verifyWith(rsaPublicKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean validateAccessToken(String token) {
        try {
            parseAccessToken(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    // ─── Refresh Token (HS256) ───────────────────────────────

    public String generateRefreshToken(UUID userId) {
        return Jwts.builder()
                .subject(userId.toString())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + refreshTokenExpiration))
                .signWith(hmacSecretKey, Jwts.SIG.HS256)
                .compact();
    }

    public Claims parseRefreshToken(String token) {
        return Jwts.parser()
                .verifyWith(hmacSecretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean validateRefreshToken(String token) {
        try {
            parseRefreshToken(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    // ─── Utilitaires ─────────────────────────────────────────

    /**
     * Extrait le temps restant avant expiration du token (en millisecondes).
     * [POURQUOI] Utilisé pour le TTL de la blacklist Redis — on ne veut pas
     * garder un token blacklisté au-delà de son expiration naturelle.
     */
    public long getAccessTokenRemainingMs(String token) {
        Claims claims = parseAccessToken(token);
        return claims.getExpiration().getTime() - System.currentTimeMillis();
    }

    public UUID extractUserIdFromAccessToken(String token) {
        Claims claims = parseAccessToken(token);
        return UUID.fromString(claims.getSubject());
    }

    // ─── Chargement RSA ──────────────────────────────────────

    private PrivateKey loadPrivateKey(String path) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        // [POURQUOI] Lecture depuis classpath pour portabilité (empaqueté dans le JAR)
        byte[] keyBytes = new ClassPathResource(path).getInputStream().readAllBytes();
        String keyContent = new String(keyBytes)
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s", "");
        byte[] decodedKey = Base64.getDecoder().decode(keyContent);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(decodedKey);
        KeyFactory factory = KeyFactory.getInstance("RSA");
        return factory.generatePrivate(spec);
    }

    private PublicKey loadPublicKey(String path) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] keyBytes = new ClassPathResource(path).getInputStream().readAllBytes();
        String keyContent = new String(keyBytes)
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s", "");
        byte[] decodedKey = Base64.getDecoder().decode(keyContent);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(decodedKey);
        KeyFactory factory = KeyFactory.getInstance("RSA");
        return factory.generatePublic(spec);
    }
}
