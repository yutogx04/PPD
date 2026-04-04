package com.codequest.network;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;

import com.codequest.model.dto.AuthResponse;
import com.codequest.ui.auth.LoginActivity;
import com.codequest.util.SharedPrefManager;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class TokenAuthenticator implements Interceptor {

    private final Context context;
    private final SharedPrefManager prefManager;

    public TokenAuthenticator(Context context) {
        this.context = context.getApplicationContext();
        this.prefManager = SharedPrefManager.getInstance(context);
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);

        // If it's a 401 or 403, we try to refresh
        if (response.code() == 401 || response.code() == 403) {
            String url = request.url().toString();
            
            // Skip if the failing endpoint WAS the refresh endpoint or login
            if (url.contains("/auth/refresh") || url.contains("/auth/login")) {
                if (url.contains("/auth/refresh")) forceLogout();
                return response;
            }

            String refreshToken = prefManager.getRefreshToken();
            if (refreshToken == null || refreshToken.isEmpty()) {
                forceLogout();
                return response;
            }

            // Close current response body to avoid connection leaks
            response.close();

            try {
                // Manually build OkHttp request to completely decouple it from RetrofitClient cycle
                okhttp3.OkHttpClient refreshClient = new okhttp3.OkHttpClient();
                String refreshBody = "{\"refreshToken\":\"" + refreshToken + "\"}";
                okhttp3.RequestBody rBody = okhttp3.RequestBody.create(refreshBody, okhttp3.MediaType.parse("application/json"));
                Request refreshRequest = new Request.Builder()
                        .url(com.codequest.util.Constants.BASE_URL + "api/v1/auth/refresh")
                        .post(rBody)
                        .build();

                Response refreshResponse = refreshClient.newCall(refreshRequest).execute();

                if (refreshResponse.isSuccessful() && refreshResponse.body() != null) {
                    String responseString = refreshResponse.body().string();
                    try {
                        org.json.JSONObject json = new org.json.JSONObject(responseString);
                        String newAccessToken = json.getString("accessToken");
                        String newRefreshToken = json.has("refreshToken") ? json.getString("refreshToken") : refreshToken;
                        
                        prefManager.saveTokens(newAccessToken, newRefreshToken);

                        Request newRequest = request.newBuilder()
                                .header("Authorization", "Bearer " + newAccessToken)
                                .build();
                        return chain.proceed(newRequest);
                    } catch (Exception e) {
                        forceLogout();
                        return response;
                    }
                } else {
                    forceLogout();
                    return refreshResponse;
                }
            } catch (Exception e) {
                forceLogout();
                return response;
            }
        }
        return response;
    }

    private void forceLogout() {
        prefManager.logout();
        new Handler(Looper.getMainLooper()).post(() -> {
            Intent intent = new Intent(context, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(intent);
        });
    }
}
