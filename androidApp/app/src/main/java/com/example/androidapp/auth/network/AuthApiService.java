package com.example.androidapp.auth.network;

import com.example.androidapp.auth.models.AuthModels.*;

import retrofit2.Call;
import retrofit2.http.*;

/**
 * Interface Retrofit2 pour tous les endpoints d'authentification.
 * [POURQUOI] Call<> (synchrone) au lieu de Single<>/Observable<>
 * car on utilise le pattern enqueue() + Callback dans le Repository.
 */
public interface AuthApiService {

    @POST("api/auth/register")
    Call<MessageResponse> register(@Body RegisterRequest request);

    @POST("api/auth/verify-otp")
    Call<AuthResponse> verifyOtp(@Body OtpRequest request);

    @POST("api/auth/login")
    Call<AuthResponse> login(@Body LoginRequest request);

    @POST("api/auth/google")
    Call<AuthResponse> googleAuth(@Body GoogleAuthRequest request);

    @POST("api/auth/refresh")
    Call<TokenResponse> refreshToken(@Body RefreshRequest request);

    @POST("api/auth/forgot-password")
    Call<MessageResponse> forgotPassword(@Body ForgotPasswordRequest request);

    @POST("api/auth/reset-password")
    Call<MessageResponse> resetPassword(@Body ResetPasswordRequest request);

    // [POURQUOI] @Header pour passer le Bearer token manuellement sur logout
    // (car l'intercepteur l'ajoute déjà, mais ici on le rend explicite)
    @POST("api/auth/logout")
    Call<MessageResponse> logout(@Header("Authorization") String bearerToken);

    @GET("api/auth/me")
    Call<UserResponse> getMe();
}
