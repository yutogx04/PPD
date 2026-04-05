package com.example.androidapp.auth.models;

import com.google.gson.annotations.SerializedName;

import java.util.UUID;

/**
 * Toutes les classes POJO pour la sérialisation/désérialisation JSON Retrofit.
 * [POURQUOI] Regroupées dans un seul fichier pour simplifier les imports.
 * Pas de Lombok sur Android — getters/setters explicites.
 */
public class AuthModels {

    // ═══════════════════════════════════════════════════════════
    //  REQUESTS
    // ═══════════════════════════════════════════════════════════

    public static class RegisterRequest {
        @SerializedName("pseudo")
        private String pseudo;

        @SerializedName("email")
        private String email;

        @SerializedName("password")
        private String password;

        public RegisterRequest(String pseudo, String email, String password) {
            this.pseudo = pseudo;
            this.email = email;
            this.password = password;
        }

        public String getPseudo() { return pseudo; }
        public String getEmail() { return email; }
        public String getPassword() { return password; }
    }

    public static class LoginRequest {
        @SerializedName("email")
        private String email;

        @SerializedName("password")
        private String password;

        public LoginRequest(String email, String password) {
            this.email = email;
            this.password = password;
        }

        public String getEmail() { return email; }
        public String getPassword() { return password; }
    }

    public static class GoogleAuthRequest {
        @SerializedName("idToken")
        private String idToken;

        public GoogleAuthRequest(String idToken) {
            this.idToken = idToken;
        }

        public String getIdToken() { return idToken; }
    }

    public static class OtpRequest {
        @SerializedName("email")
        private String email;

        @SerializedName("otp")
        private String otp;

        public OtpRequest(String email, String otp) {
            this.email = email;
            this.otp = otp;
        }

        public String getEmail() { return email; }
        public String getOtp() { return otp; }
    }

    public static class RefreshRequest {
        @SerializedName("refreshToken")
        private String refreshToken;

        public RefreshRequest(String refreshToken) {
            this.refreshToken = refreshToken;
        }

        public String getRefreshToken() { return refreshToken; }
    }

    public static class ForgotPasswordRequest {
        @SerializedName("email")
        private String email;

        public ForgotPasswordRequest(String email) {
            this.email = email;
        }

        public String getEmail() { return email; }
    }

    public static class ResetPasswordRequest {
        @SerializedName("email")
        private String email;

        @SerializedName("otp")
        private String otp;

        @SerializedName("newPassword")
        private String newPassword;

        public ResetPasswordRequest(String email, String otp, String newPassword) {
            this.email = email;
            this.otp = otp;
            this.newPassword = newPassword;
        }

        public String getEmail() { return email; }
        public String getOtp() { return otp; }
        public String getNewPassword() { return newPassword; }
    }

    // ═══════════════════════════════════════════════════════════
    //  RESPONSES
    // ═══════════════════════════════════════════════════════════

    public static class AuthResponse {
        @SerializedName("token")
        private String token;

        @SerializedName("refreshToken")
        private String refreshToken;

        @SerializedName("user")
        private UserResponse user;

        public String getToken() { return token; }
        public String getRefreshToken() { return refreshToken; }
        public UserResponse getUser() { return user; }
    }

    public static class UserResponse {
        @SerializedName("id")
        private String id;

        @SerializedName("pseudo")
        private String pseudo;

        @SerializedName("email")
        private String email;

        @SerializedName("level")
        private int level;

        @SerializedName("xp")
        private int xp;

        @SerializedName("streak")
        private int streak;

        @SerializedName("avatarUrl")
        private String avatarUrl;

        public String getId() { return id; }
        public String getPseudo() { return pseudo; }
        public String getEmail() { return email; }
        public int getLevel() { return level; }
        public int getXp() { return xp; }
        public int getStreak() { return streak; }
        public String getAvatarUrl() { return avatarUrl; }
    }

    public static class MessageResponse {
        @SerializedName("message")
        private String message;

        public String getMessage() { return message; }
    }

    public static class TokenResponse {
        @SerializedName("token")
        private String token;

        public String getToken() { return token; }
    }

    public static class ApiError {
        @SerializedName("error")
        private String error;

        public String getError() { return error; }

        /**
         * [POURQUOI] Parse le body d'erreur JSON depuis Retrofit.
         * Utilisé dans le Repository pour extraire les messages d'erreur serveur.
         */
        public static String parseError(okhttp3.ResponseBody errorBody) {
            try {
                if (errorBody != null) {
                    String body = errorBody.string();
                    com.google.gson.Gson gson = new com.google.gson.Gson();
                    ApiError apiError = gson.fromJson(body, ApiError.class);
                    if (apiError != null && apiError.getError() != null) {
                        return apiError.getError();
                    }
                }
            } catch (Exception ignored) {
            }
            return "Une erreur est survenue";
        }
    }
}
