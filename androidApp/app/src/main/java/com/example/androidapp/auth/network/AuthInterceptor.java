package com.example.androidapp.auth.network;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.androidapp.auth.models.AuthModels.RefreshRequest;
import com.example.androidapp.auth.models.AuthModels.TokenResponse;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;

/**
 * Intercepteur OkHttp pour l'authentification automatique.
 * [POURQUOI] Ajoute le Bearer token à chaque requête et gère
 * le refresh automatique en cas de 401 (token expiré).
 */
public class AuthInterceptor implements Interceptor {

    private static final String TAG = "AuthInterceptor";
    private final TokenManager tokenManager;

    public AuthInterceptor(TokenManager tokenManager) {
        this.tokenManager = tokenManager;
    }

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request originalRequest = chain.request();

        // [POURQUOI] Ne pas ajouter de token aux requêtes d'auth publiques
        // pour éviter un cycle infini sur /refresh
        String path = originalRequest.url().encodedPath();
        if (isPublicEndpoint(path)) {
            return chain.proceed(originalRequest);
        }

        String accessToken = tokenManager.getAccessToken();

        // Si pas de token, procéder sans authentification
        if (accessToken == null) {
            return chain.proceed(originalRequest);
        }

        // Ajouter le token à la requête
        Request authenticatedRequest = originalRequest.newBuilder()
                .header("Authorization", "Bearer " + accessToken)
                .build();

        Response response = chain.proceed(authenticatedRequest);

        // [POURQUOI] Si 401, tenter un refresh automatique du token
        if (response.code() == 401) {
            response.close(); // Fermer la réponse 401

            String refreshToken = tokenManager.getRefreshToken();
            if (refreshToken == null) {
                // Pas de refresh token → déconnexion
                tokenManager.clearTokens();
                return response;
            }

            // Tenter le refresh (appel synchrone car on est dans un intercepteur)
            String newAccessToken = attemptTokenRefresh(refreshToken);

            if (newAccessToken != null) {
                tokenManager.saveAccessToken(newAccessToken);

                // Rejouer la requête originale avec le nouveau token
                Request retryRequest = originalRequest.newBuilder()
                        .header("Authorization", "Bearer " + newAccessToken)
                        .build();

                return chain.proceed(retryRequest);
            } else {
                // Refresh échoué → déconnexion
                tokenManager.clearTokens();
            }
        }

        return response;
    }

    /**
     * Tente de rafraîchir le token d'accès.
     * [POURQUOI] Appel synchrone (execute() au lieu de enqueue())
     * car OkHttp exige une réponse synchrone dans un intercepteur.
     */
    private String attemptTokenRefresh(String refreshToken) {
        try {
            AuthApiService apiService = RetrofitClient.getInstance(tokenManager).getAuthApiService();
            Call<TokenResponse> call = apiService.refreshToken(new RefreshRequest(refreshToken));
            retrofit2.Response<TokenResponse> response = call.execute();

            if (response.isSuccessful() && response.body() != null) {
                Log.d(TAG, "Token rafraîchi avec succès");
                return response.body().getToken();
            }
        } catch (IOException e) {
            Log.e(TAG, "Erreur lors du refresh token", e);
        }
        return null;
    }

    /**
     * [POURQUOI] Les endpoints publics ne nécessitent pas de token.
     * On évite d'ajouter un token invalide ou d'entrer en boucle de refresh.
     */
    private boolean isPublicEndpoint(String path) {
        return path.contains("/auth/login")
                || path.contains("/auth/register")
                || path.contains("/auth/verify-otp")
                || path.contains("/auth/google")
                || path.contains("/auth/refresh")
                || path.contains("/auth/forgot-password")
                || path.contains("/auth/reset-password");
    }
}
