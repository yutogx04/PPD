package com.codequest.viewmodel;
import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.codequest.model.dto.AuthResponse;
import com.codequest.repository.AuthRepository;
public class RegisterViewModel extends AndroidViewModel {
    private final AuthRepository authRepository;
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private String pseudo;
    private String email;
    private String password;
    private String selectedLevel = "BEGINNER";
    public RegisterViewModel(@NonNull Application application) {
        super(application);
        authRepository = new AuthRepository(application);
    }
    public boolean validateStep1(String pseudo, String email, String password) {
        if (pseudo == null || pseudo.trim().length() < 3) {
            errorMessage.setValue("Le pseudo doit contenir au moins 3 caractères");
            return false;
        }
        if (email == null || !email.contains("@")) {
            errorMessage.setValue("Email invalide");
            return false;
        }
        if (password == null || password.length() < 8) {
            errorMessage.setValue("Le mot de passe doit contenir au moins 8 caractères");
            return false;
        }
        this.pseudo = pseudo.trim();
        this.email = email.trim();
        this.password = password;
        errorMessage.setValue(null);
        return true;
    }
    public void setSelectedLevel(String level) {
        this.selectedLevel = level;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public LiveData<AuthResponse> register() {
        isLoading.setValue(true);
        errorMessage.setValue(null);
        LiveData<AuthResponse> result = authRepository.register(pseudo, email, password, selectedLevel);
        isLoading.setValue(false);
        return result;
    }
    public LiveData<AuthResponse> verifyOTP(String otpCode) {
        if (otpCode == null || otpCode.length() != 6) {
            errorMessage.setValue("Le code doit contenir 6 chiffres");
            return new MutableLiveData<>(null);
        }
        isLoading.setValue(true);
        LiveData<AuthResponse> result = authRepository.verifyOTP(email, otpCode);
        isLoading.setValue(false);
        return result;
    }
    public String getEmail() { return email; }
    public String getSelectedLevel() { return selectedLevel; }
    public LiveData<Boolean> getIsLoading() { return isLoading; }
    public LiveData<String> getErrorMessage() { return errorMessage; }
}
