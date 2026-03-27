package com.example.androidapp.auth.repository;

import androidx.lifecycle.MutableLiveData;

import com.example.androidapp.auth.models.AuthModels.*;
import com.example.androidapp.auth.network.AuthApiService;
import com.example.androidapp.auth.network.RetrofitClient;
import com.example.androidapp.auth.network.TokenManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Repository pattern pour l'authentification.
 * [POURQUOI] Sépare la logique réseau du ViewModel.
 * Chaque méthode expose un MutableLiveData pour observer le résultat.
 */
public class AuthRepository {

    private final AuthApiService apiService;
    private final TokenManager tokenManager;

    // ─── LiveData observables ────────────────────────────────

    private final MutableLiveData<AuthResult<MessageResponse>> registerResult = new MutableLiveData<>();
    private final MutableLiveData<AuthResult<AuthResponse>> otpResult = new MutableLiveData<>();
    private final MutableLiveData<AuthResult<AuthResponse>> loginResult = new MutableLiveData<>();
    private final MutableLiveData<AuthResult<AuthResponse>> googleResult = new MutableLiveData<>();
    private final MutableLiveData<AuthResult<MessageResponse>> forgotPasswordResult = new MutableLiveData<>();
    private final MutableLiveData<AuthResult<MessageResponse>> resetPasswordResult = new MutableLiveData<>();
    private final MutableLiveData<AuthResult<MessageResponse>> logoutResult = new MutableLiveData<>();
    private final MutableLiveData<AuthResult<UserResponse>> meResult = new MutableLiveData<>();

    public AuthRepository(TokenManager tokenManager) {
        this.tokenManager = tokenManager;
        this.apiService = RetrofitClient.getInstance(tokenManager).getAuthApiService();
    }

    // ═══════════════════════════════════════════════════════════
    //  REGISTER
    // ═══════════════════════════════════════════════════════════

    public void register(String pseudo, String email, String password) {
        registerResult.postValue(AuthResult.loading());

        apiService.register(new RegisterRequest(pseudo, email, password)).enqueue(
                new Callback<MessageResponse>() {
                    @Override
                    public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            registerResult.postValue(AuthResult.success(response.body()));
                        } else {
                            String error = ApiError.parseError(response.errorBody());
                            registerResult.postValue(AuthResult.error(error));
                        }
                    }

                    @Override
                    public void onFailure(Call<MessageResponse> call, Throwable t) {
                        registerResult.postValue(AuthResult.error("Erreur réseau : " + t.getMessage()));
                    }
                });
    }

    // ═══════════════════════════════════════════════════════════
    //  VERIFY OTP
    // ═══════════════════════════════════════════════════════════

    public void verifyOtp(String email, String otp) {
        otpResult.postValue(AuthResult.loading());

        apiService.verifyOtp(new OtpRequest(email, otp)).enqueue(
                new Callback<AuthResponse>() {
                    @Override
                    public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            // [POURQUOI] Sauvegarder automatiquement les tokens à la réception
                            saveTokensFromResponse(response.body());
                            otpResult.postValue(AuthResult.success(response.body()));
                        } else {
                            String error = ApiError.parseError(response.errorBody());
                            otpResult.postValue(AuthResult.error(error));
                        }
                    }

                    @Override
                    public void onFailure(Call<AuthResponse> call, Throwable t) {
                        otpResult.postValue(AuthResult.error("Erreur réseau : " + t.getMessage()));
                    }
                });
    }

    // ═══════════════════════════════════════════════════════════
    //  LOGIN
    // ═══════════════════════════════════════════════════════════

    public void login(String email, String password) {
        loginResult.postValue(AuthResult.loading());

        apiService.login(new LoginRequest(email, password)).enqueue(
                new Callback<AuthResponse>() {
                    @Override
                    public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            saveTokensFromResponse(response.body());
                            loginResult.postValue(AuthResult.success(response.body()));
                        } else {
                            String error = ApiError.parseError(response.errorBody());
                            loginResult.postValue(AuthResult.error(error));
                        }
                    }

                    @Override
                    public void onFailure(Call<AuthResponse> call, Throwable t) {
                        loginResult.postValue(AuthResult.error("Erreur réseau : " + t.getMessage()));
                    }
                });
    }

    // ═══════════════════════════════════════════════════════════
    //  GOOGLE AUTH
    // ═══════════════════════════════════════════════════════════

    public void loginWithGoogle(String idToken) {
        googleResult.postValue(AuthResult.loading());

        apiService.googleAuth(new GoogleAuthRequest(idToken)).enqueue(
                new Callback<AuthResponse>() {
                    @Override
                    public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            saveTokensFromResponse(response.body());
                            googleResult.postValue(AuthResult.success(response.body()));
                        } else {
                            String error = ApiError.parseError(response.errorBody());
                            googleResult.postValue(AuthResult.error(error));
                        }
                    }

                    @Override
                    public void onFailure(Call<AuthResponse> call, Throwable t) {
                        googleResult.postValue(AuthResult.error("Erreur réseau : " + t.getMessage()));
                    }
                });
    }

    // ═══════════════════════════════════════════════════════════
    //  FORGOT PASSWORD
    // ═══════════════════════════════════════════════════════════

    public void forgotPassword(String email) {
        forgotPasswordResult.postValue(AuthResult.loading());

        apiService.forgotPassword(new ForgotPasswordRequest(email)).enqueue(
                new Callback<MessageResponse>() {
                    @Override
                    public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            forgotPasswordResult.postValue(AuthResult.success(response.body()));
                        } else {
                            String error = ApiError.parseError(response.errorBody());
                            forgotPasswordResult.postValue(AuthResult.error(error));
                        }
                    }

                    @Override
                    public void onFailure(Call<MessageResponse> call, Throwable t) {
                        forgotPasswordResult.postValue(AuthResult.error("Erreur réseau : " + t.getMessage()));
                    }
                });
    }

    // ═══════════════════════════════════════════════════════════
    //  RESET PASSWORD
    // ═══════════════════════════════════════════════════════════

    public void resetPassword(String email, String otp, String newPassword) {
        resetPasswordResult.postValue(AuthResult.loading());

        apiService.resetPassword(new ResetPasswordRequest(email, otp, newPassword)).enqueue(
                new Callback<MessageResponse>() {
                    @Override
                    public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            resetPasswordResult.postValue(AuthResult.success(response.body()));
                        } else {
                            String error = ApiError.parseError(response.errorBody());
                            resetPasswordResult.postValue(AuthResult.error(error));
                        }
                    }

                    @Override
                    public void onFailure(Call<MessageResponse> call, Throwable t) {
                        resetPasswordResult.postValue(AuthResult.error("Erreur réseau : " + t.getMessage()));
                    }
                });
    }

    // ═══════════════════════════════════════════════════════════
    //  LOGOUT
    // ═══════════════════════════════════════════════════════════

    public void logout() {
        logoutResult.postValue(AuthResult.loading());

        String token = tokenManager.getAccessToken();
        if (token == null) {
            tokenManager.clearTokens();
            logoutResult.postValue(AuthResult.success(new MessageResponse()));
            return;
        }

        apiService.logout("Bearer " + token).enqueue(
                new Callback<MessageResponse>() {
                    @Override
                    public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                        // [POURQUOI] On clear les tokens même si le serveur renvoie une erreur
                        tokenManager.clearTokens();
                        RetrofitClient.reset();
                        if (response.isSuccessful() && response.body() != null) {
                            logoutResult.postValue(AuthResult.success(response.body()));
                        } else {
                            logoutResult.postValue(AuthResult.success(new MessageResponse()));
                        }
                    }

                    @Override
                    public void onFailure(Call<MessageResponse> call, Throwable t) {
                        tokenManager.clearTokens();
                        RetrofitClient.reset();
                        logoutResult.postValue(AuthResult.success(new MessageResponse()));
                    }
                });
    }

    // ═══════════════════════════════════════════════════════════
    //  GET ME
    // ═══════════════════════════════════════════════════════════

    public void getMe() {
        meResult.postValue(AuthResult.loading());

        apiService.getMe().enqueue(
                new Callback<UserResponse>() {
                    @Override
                    public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            meResult.postValue(AuthResult.success(response.body()));
                        } else {
                            String error = ApiError.parseError(response.errorBody());
                            meResult.postValue(AuthResult.error(error));
                        }
                    }

                    @Override
                    public void onFailure(Call<UserResponse> call, Throwable t) {
                        meResult.postValue(AuthResult.error("Erreur réseau : " + t.getMessage()));
                    }
                });
    }

    // ═══════════════════════════════════════════════════════════
    //  PRIVATE HELPERS
    // ═══════════════════════════════════════════════════════════

    private void saveTokensFromResponse(AuthResponse authResponse) {
        tokenManager.saveTokens(authResponse.getToken(), authResponse.getRefreshToken());
    }

    // ─── Getters ─────────────────────────────────────────────

    public MutableLiveData<AuthResult<MessageResponse>> getRegisterResult() { return registerResult; }
    public MutableLiveData<AuthResult<AuthResponse>> getOtpResult() { return otpResult; }
    public MutableLiveData<AuthResult<AuthResponse>> getLoginResult() { return loginResult; }
    public MutableLiveData<AuthResult<AuthResponse>> getGoogleResult() { return googleResult; }
    public MutableLiveData<AuthResult<MessageResponse>> getForgotPasswordResult() { return forgotPasswordResult; }
    public MutableLiveData<AuthResult<MessageResponse>> getResetPasswordResult() { return resetPasswordResult; }
    public MutableLiveData<AuthResult<MessageResponse>> getLogoutResult() { return logoutResult; }
    public MutableLiveData<AuthResult<UserResponse>> getMeResult() { return meResult; }

    // ═══════════════════════════════════════════════════════════
    //  AuthResult wrapper
    // ═══════════════════════════════════════════════════════════

    /**
     * [POURQUOI] Wrapper générique pour encapsuler les états Loading/Success/Error
     * dans un seul objet LiveData, simplifiant l'observation côté UI.
     */
    public static class AuthResult<T> {

        public enum Status { LOADING, SUCCESS, ERROR }

        private final Status status;
        private final T data;
        private final String error;

        private AuthResult(Status status, T data, String error) {
            this.status = status;
            this.data = data;
            this.error = error;
        }

        public static <T> AuthResult<T> loading() {
            return new AuthResult<>(Status.LOADING, null, null);
        }

        public static <T> AuthResult<T> success(T data) {
            return new AuthResult<>(Status.SUCCESS, data, null);
        }

        public static <T> AuthResult<T> error(String error) {
            return new AuthResult<>(Status.ERROR, null, error);
        }

        public Status getStatus() { return status; }
        public T getData() { return data; }
        public String getError() { return error; }
    }
}
