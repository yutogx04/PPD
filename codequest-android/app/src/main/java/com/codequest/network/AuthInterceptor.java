package com.codequest.network;
import android.content.Context;
import com.codequest.util.SharedPrefManager;
import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
public class AuthInterceptor implements Interceptor {
    private final SharedPrefManager prefManager;
    public AuthInterceptor(Context context) {
        this.prefManager = SharedPrefManager.getInstance(context);
    }
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();
        String url = originalRequest.url().toString();
        String lang = prefManager.getLanguage();

        if (url.contains("/auth/login") ||
            url.contains("/auth/register") ||
            url.contains("/auth/verify-email") ||
            url.contains("/auth/forgot-password") ||
            url.contains("/auth/google") ||
            url.contains("/auth/refresh")) {
            Request langRequest = originalRequest.newBuilder()
                    .header("Accept-Language", lang)
                    .build();
            return chain.proceed(langRequest);
        }

        String token = prefManager.getAccessToken();
        if (token != null && !token.isEmpty()) {
            Request authenticatedRequest = originalRequest.newBuilder()
                    .header("Authorization", "Bearer " + token)
                    .header("Content-Type", "application/json")
                    .header("Accept-Language", lang)
                    .build();
            return chain.proceed(authenticatedRequest);
        }

        Request langRequest = originalRequest.newBuilder()
                .header("Accept-Language", lang)
                .build();
        return chain.proceed(langRequest);
    }
}
