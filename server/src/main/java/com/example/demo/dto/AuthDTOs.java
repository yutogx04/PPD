package com.example.demo.dto;

import jakarta.validation.constraints.*;
import java.util.UUID;

/**
 * Toutes les classes DTO pour l'authentification.
 * [POURQUOI] Regroupées dans un seul fichier pour simplifier l'import
 * et la maintenance. Chaque classe interne est statique et publique.
 */
public class AuthDTOs {

    // ═══════════════════════════════════════════════════════════
    //  REQUESTS
    // ═══════════════════════════════════════════════════════════

    public static class RegisterRequest {

        @NotBlank(message = "Le pseudo est obligatoire")
        @Size(min = 3, max = 20, message = "Le pseudo doit contenir entre 3 et 20 caractères")
        @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "Le pseudo ne peut contenir que des caractères alphanumériques et underscores")
        private String pseudo;

        @NotBlank(message = "L'email est obligatoire")
        @Email(message = "Format d'email invalide")
        private String email;

        // [POURQUOI] Regex pour garantir min 8 chars, 1 majuscule, 1 chiffre, 1 spécial
        @NotBlank(message = "Le mot de passe est obligatoire")
        @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&#+\\-_])[A-Za-z\\d@$!%*?&#+\\-_]{8,}$",
            message = "Le mot de passe doit contenir au moins 8 caractères, 1 majuscule, 1 chiffre et 1 caractère spécial"
        )
        private String password;

        public RegisterRequest() {}

        public RegisterRequest(String pseudo, String email, String password) {
            this.pseudo = pseudo;
            this.email = email;
            this.password = password;
        }

        public String getPseudo() { return pseudo; }
        public void setPseudo(String pseudo) { this.pseudo = pseudo; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }

    public static class LoginRequest {

        @NotBlank(message = "L'email est obligatoire")
        @Email(message = "Format d'email invalide")
        private String email;

        @NotBlank(message = "Le mot de passe est obligatoire")
        private String password;

        public LoginRequest() {}

        public LoginRequest(String email, String password) {
            this.email = email;
            this.password = password;
        }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }

    public static class GoogleAuthRequest {

        @NotBlank(message = "Le token Google est obligatoire")
        private String idToken;

        public GoogleAuthRequest() {}

        public GoogleAuthRequest(String idToken) {
            this.idToken = idToken;
        }

        public String getIdToken() { return idToken; }
        public void setIdToken(String idToken) { this.idToken = idToken; }
    }

    public static class OtpRequest {

        @NotBlank(message = "L'email est obligatoire")
        @Email(message = "Format d'email invalide")
        private String email;

        @NotBlank(message = "Le code OTP est obligatoire")
        @Size(min = 6, max = 6, message = "Le code OTP doit contenir exactement 6 chiffres")
        private String otp;

        public OtpRequest() {}

        public OtpRequest(String email, String otp) {
            this.email = email;
            this.otp = otp;
        }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getOtp() { return otp; }
        public void setOtp(String otp) { this.otp = otp; }
    }

    public static class RefreshRequest {

        @NotBlank(message = "Le refresh token est obligatoire")
        private String refreshToken;

        public RefreshRequest() {}

        public RefreshRequest(String refreshToken) {
            this.refreshToken = refreshToken;
        }

        public String getRefreshToken() { return refreshToken; }
        public void setRefreshToken(String refreshToken) { this.refreshToken = refreshToken; }
    }

    public static class ForgotPasswordRequest {

        @NotBlank(message = "L'email est obligatoire")
        @Email(message = "Format d'email invalide")
        private String email;

        public ForgotPasswordRequest() {}

        public ForgotPasswordRequest(String email) {
            this.email = email;
        }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
    }

    public static class ResetPasswordRequest {

        @NotBlank(message = "L'email est obligatoire")
        @Email(message = "Format d'email invalide")
        private String email;

        @NotBlank(message = "Le code OTP est obligatoire")
        @Size(min = 6, max = 6, message = "Le code OTP doit contenir exactement 6 chiffres")
        private String otp;

        @NotBlank(message = "Le nouveau mot de passe est obligatoire")
        @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&#+\\-_])[A-Za-z\\d@$!%*?&#+\\-_]{8,}$",
            message = "Le mot de passe doit contenir au moins 8 caractères, 1 majuscule, 1 chiffre et 1 caractère spécial"
        )
        private String newPassword;

        public ResetPasswordRequest() {}

        public ResetPasswordRequest(String email, String otp, String newPassword) {
            this.email = email;
            this.otp = otp;
            this.newPassword = newPassword;
        }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getOtp() { return otp; }
        public void setOtp(String otp) { this.otp = otp; }
        public String getNewPassword() { return newPassword; }
        public void setNewPassword(String newPassword) { this.newPassword = newPassword; }
    }

    // ═══════════════════════════════════════════════════════════
    //  RESPONSES
    // ═══════════════════════════════════════════════════════════

    public static class AuthResponse {
        private String token;
        private String refreshToken;
        private UserResponse user;

        public AuthResponse() {}

        public AuthResponse(String token, String refreshToken, UserResponse user) {
            this.token = token;
            this.refreshToken = refreshToken;
            this.user = user;
        }

        public String getToken() { return token; }
        public void setToken(String token) { this.token = token; }
        public String getRefreshToken() { return refreshToken; }
        public void setRefreshToken(String refreshToken) { this.refreshToken = refreshToken; }
        public UserResponse getUser() { return user; }
        public void setUser(UserResponse user) { this.user = user; }
    }

    public static class UserResponse {
        private UUID id;
        private String pseudo;
        private String email;
        private int level;
        private int xp;
        private int streak;
        private String avatarUrl;

        public UserResponse() {}

        public UserResponse(UUID id, String pseudo, String email, int level, int xp, int streak, String avatarUrl) {
            this.id = id;
            this.pseudo = pseudo;
            this.email = email;
            this.level = level;
            this.xp = xp;
            this.streak = streak;
            this.avatarUrl = avatarUrl;
        }

        public UUID getId() { return id; }
        public void setId(UUID id) { this.id = id; }
        public String getPseudo() { return pseudo; }
        public void setPseudo(String pseudo) { this.pseudo = pseudo; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public int getLevel() { return level; }
        public void setLevel(int level) { this.level = level; }
        public int getXp() { return xp; }
        public void setXp(int xp) { this.xp = xp; }
        public int getStreak() { return streak; }
        public void setStreak(int streak) { this.streak = streak; }
        public String getAvatarUrl() { return avatarUrl; }
        public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }
    }

    public static class MessageResponse {
        private String message;

        public MessageResponse() {}

        public MessageResponse(String message) {
            this.message = message;
        }

        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
    }

    public static class ErrorResponse {
        private String error;

        public ErrorResponse() {}

        public ErrorResponse(String error) {
            this.error = error;
        }

        public String getError() { return error; }
        public void setError(String error) { this.error = error; }
    }

    public static class TokenResponse {
        private String token;

        public TokenResponse() {}

        public TokenResponse(String token) {
            this.token = token;
        }

        public String getToken() { return token; }
        public void setToken(String token) { this.token = token; }
    }
}
