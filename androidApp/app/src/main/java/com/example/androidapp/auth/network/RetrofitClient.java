package com.example.androidapp.auth.network;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Singleton Retrofit2 client avec intercepteurs.
 * [POURQUOI] Singleton pour partager le pool de connexions OkHttp
 * et éviter les fuites mémoire liées à la création de multiples clients.
 */
public class RetrofitClient {

    // [POURQUOI] URL configurable — changer selon l'environnement
    // Pour l'émulateur Android : 10.0.2.2 (alias localhost du host)
    // Pour un device physique : IP locale du PC (ex: 192.168.1.x)
    private static final String BASE_URL = "http://10.0.2.2:8080/";

    private static RetrofitClient instance;
    private final Retrofit retrofit;
    private final AuthApiService authApiService;

    private RetrofitClient(TokenManager tokenManager) {
        // [POURQUOI] Logging en DEBUG uniquement — ne JAMAIS logger les tokens en prod
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new AuthInterceptor(tokenManager))
                .addInterceptor(loggingInterceptor) // [POURQUOI] En dernier pour logger la requête finale
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        authApiService = retrofit.create(AuthApiService.class);
    }

    /**
     * [POURQUOI] Double-checked locking pour thread-safety du singleton.
     */
    public static synchronized RetrofitClient getInstance(TokenManager tokenManager) {
        if (instance == null) {
            instance = new RetrofitClient(tokenManager);
        }
        return instance;
    }

    public AuthApiService getAuthApiService() {
        return authApiService;
    }

    /**
     * Retourne l'instance Retrofit pour créer d'autres services API.
     */
    public Retrofit getRetrofit() {
        return retrofit;
    }

    /**
     * [POURQUOI] Permet de reset le singleton (utile après un logout complet
     * pour recréer le client avec un nouveau TokenManager).
     */
    public static synchronized void reset() {
        instance = null;
    }
}
