package com.example.androidapp.auth.network;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

import java.io.IOException;
import java.security.GeneralSecurityException;

/**
 * Gestionnaire de tokens sécurisé via EncryptedSharedPreferences.
 * [POURQUOI] EncryptedSharedPreferences chiffre à la fois les clés et les valeurs
 * avec AES-256-GCM, protégeant les tokens même en cas de root/backup.
 */
public class TokenManager {

    private static final String PREF_FILE_NAME = "codequest_secure_prefs";
    private static final String KEY_ACCESS_TOKEN = "access_token";
    private static final String KEY_REFRESH_TOKEN = "refresh_token";

    private final SharedPreferences encryptedPrefs;

    public TokenManager(Context context) {
        try {
            // [POURQUOI] MasterKey utilise Android Keystore — la clé maître
            // ne quitte jamais le hardware de sécurité du device
            MasterKey masterKey = new MasterKey.Builder(context)
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                    .build();

            this.encryptedPrefs = EncryptedSharedPreferences.create(
                    context,
                    PREF_FILE_NAME,
                    masterKey,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
        } catch (GeneralSecurityException | IOException e) {
            throw new RuntimeException("Impossible d'initialiser EncryptedSharedPreferences", e);
        }
    }

    public void saveAccessToken(String token) {
        encryptedPrefs.edit().putString(KEY_ACCESS_TOKEN, token).apply();
    }

    public void saveRefreshToken(String token) {
        encryptedPrefs.edit().putString(KEY_REFRESH_TOKEN, token).apply();
    }

    public String getAccessToken() {
        return encryptedPrefs.getString(KEY_ACCESS_TOKEN, null);
    }

    public String getRefreshToken() {
        return encryptedPrefs.getString(KEY_REFRESH_TOKEN, null);
    }

    /**
     * Sauvegarde les deux tokens en une seule transaction.
     */
    public void saveTokens(String accessToken, String refreshToken) {
        encryptedPrefs.edit()
                .putString(KEY_ACCESS_TOKEN, accessToken)
                .putString(KEY_REFRESH_TOKEN, refreshToken)
                .apply();
    }

    public void clearTokens() {
        encryptedPrefs.edit()
                .remove(KEY_ACCESS_TOKEN)
                .remove(KEY_REFRESH_TOKEN)
                .apply();
    }

    /**
     * Vérifie si un access token existe (l'utilisateur est potentiellement connecté).
     * [POURQUOI] Ne vérifie pas l'expiration — c'est le serveur qui fait ça.
     * L'intercepteur gèrera le refresh automatiquement si le token est expiré.
     */
    public boolean isLoggedIn() {
        return getAccessToken() != null;
    }
}
