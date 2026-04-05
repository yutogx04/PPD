package com.example.androidapp.auth.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.androidapp.auth.models.AuthModels.*;
import com.example.androidapp.auth.network.TokenManager;
import com.example.androidapp.auth.repository.AuthRepository;
import com.example.androidapp.auth.repository.AuthRepository.AuthResult;

/**
 * ViewModel pour l'authentification.
 * [POURQUOI] AndroidViewModel pour accéder à l'Application context
 * (nécessaire pour TokenManager/EncryptedSharedPreferences).
 * Survit aux rotations d'écran sans refaire les appels réseau.
 */
public class AuthViewModel extends AndroidViewModel {

    /**
     * États possibles de l'authentification pour l'UI.
     */
    public enum AuthState {
        IDLE,
        LOADING,
        SUCCESS,
        ERROR,
        OTP_REQUIRED
    }

    private final AuthRepository repository;
    private final TokenManager tokenManager;

    // ─── State observables pour l'UI ─────────────────────────

    private final MutableLiveData<AuthState> authState = new MutableLiveData<>(AuthState.IDLE);
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<String> successMessage = new MutableLiveData<>();
    private final MutableLiveData<AuthResponse> authResponse = new MutableLiveData<>();
    private final MutableLiveData<UserResponse> userProfile = new MutableLiveData<>();

    // Email sauvegardé pour la vérification OTP
    private String pendingEmail;

    public AuthViewModel(@NonNull Application application) {
        super(application);
        this.tokenManager = new TokenManager(application.getApplicationContext());
        this.repository = new AuthRepository(tokenManager);
        setupObservers();
    }

    // ═══════════════════════════════════════════════════════════
    //  SETUP OBSERVERS
    // ═══════════════════════════════════════════════════════════

    /**
     * [POURQUOI] On observe les LiveData du Repository via MediatorLiveData pattern
     * pour transformer les AuthResult en AuthState lisibles pour l'UI.
     */
    private void setupObservers() {
        // Observer Register
        repository.getRegisterResult().observeForever(result -> {
            switch (result.getStatus()) {
                case LOADING:
                    authState.postValue(AuthState.LOADING);
                    break;
                case SUCCESS:
                    authState.postValue(AuthState.OTP_REQUIRED);
                    successMessage.postValue(result.getData().getMessage());
                    break;
                case ERROR:
                    authState.postValue(AuthState.ERROR);
                    errorMessage.postValue(result.getError());
                    break;
            }
        });

        // Observer Login
        repository.getLoginResult().observeForever(result -> {
            handleAuthResult(result);
        });

        // Observer OTP Verify
        repository.getOtpResult().observeForever(result -> {
            handleAuthResult(result);
        });

        // Observer Google Auth
        repository.getGoogleResult().observeForever(result -> {
            handleAuthResult(result);
        });

        // Observer Forgot Password
        repository.getForgotPasswordResult().observeForever(result -> {
            handleMessageResult(result);
        });

        // Observer Reset Password
        repository.getResetPasswordResult().observeForever(result -> {
            handleMessageResult(result);
        });

        // Observer Logout
        repository.getLogoutResult().observeForever(result -> {
            if (result.getStatus() == AuthResult.Status.SUCCESS) {
                authState.postValue(AuthState.IDLE);
            }
        });

        // Observer Get Me
        repository.getMeResult().observeForever(result -> {
            switch (result.getStatus()) {
                case LOADING:
                    authState.postValue(AuthState.LOADING);
                    break;
                case SUCCESS:
                    userProfile.postValue(result.getData());
                    authState.postValue(AuthState.SUCCESS);
                    break;
                case ERROR:
                    errorMessage.postValue(result.getError());
                    authState.postValue(AuthState.ERROR);
                    break;
            }
        });
    }

    // ═══════════════════════════════════════════════════════════
    //  PUBLIC METHODS (appelées depuis les Activities/Fragments)
    // ═══════════════════════════════════════════════════════════

    public void register(String pseudo, String email, String password) {
        this.pendingEmail = email;
        repository.register(pseudo, email, password);
    }

    public void verifyOtp(String otp) {
        if (pendingEmail == null) {
            errorMessage.postValue("Email non disponible. Veuillez recommencer l'inscription.");
            return;
        }
        repository.verifyOtp(pendingEmail, otp);
    }

    public void verifyOtp(String email, String otp) {
        repository.verifyOtp(email, otp);
    }

    public void login(String email, String password) {
        repository.login(email, password);
    }

    public void loginWithGoogle(String idToken) {
        repository.loginWithGoogle(idToken);
    }

    public void forgotPassword(String email) {
        this.pendingEmail = email;
        repository.forgotPassword(email);
    }

    public void resetPassword(String email, String otp, String newPassword) {
        repository.resetPassword(email, otp, newPassword);
    }

    public void logout() {
        repository.logout();
    }

    public void getMe() {
        repository.getMe();
    }

    public boolean isLoggedIn() {
        return tokenManager.isLoggedIn();
    }

    public void resetState() {
        authState.postValue(AuthState.IDLE);
        errorMessage.postValue(null);
        successMessage.postValue(null);
    }

    // ═══════════════════════════════════════════════════════════
    //  GETTERS
    // ═══════════════════════════════════════════════════════════

    public LiveData<AuthState> getAuthState() { return authState; }
    public LiveData<String> getErrorMessage() { return errorMessage; }
    public LiveData<String> getSuccessMessage() { return successMessage; }
    public LiveData<AuthResponse> getAuthResponse() { return authResponse; }
    public LiveData<UserResponse> getUserProfile() { return userProfile; }
    public String getPendingEmail() { return pendingEmail; }
    public void setPendingEmail(String email) { this.pendingEmail = email; }

    // ═══════════════════════════════════════════════════════════
    //  PRIVATE HELPERS
    // ═══════════════════════════════════════════════════════════

    private void handleAuthResult(AuthResult<AuthResponse> result) {
        switch (result.getStatus()) {
            case LOADING:
                authState.postValue(AuthState.LOADING);
                break;
            case SUCCESS:
                authResponse.postValue(result.getData());
                authState.postValue(AuthState.SUCCESS);
                break;
            case ERROR:
                errorMessage.postValue(result.getError());
                authState.postValue(AuthState.ERROR);
                break;
        }
    }

    private void handleMessageResult(AuthResult<MessageResponse> result) {
        switch (result.getStatus()) {
            case LOADING:
                authState.postValue(AuthState.LOADING);
                break;
            case SUCCESS:
                successMessage.postValue(result.getData().getMessage());
                authState.postValue(AuthState.SUCCESS);
                break;
            case ERROR:
                errorMessage.postValue(result.getError());
                authState.postValue(AuthState.ERROR);
                break;
        }
    }
}
