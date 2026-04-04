package com.codequest.viewmodel;
import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.codequest.model.dto.AuthResponse;
import com.codequest.repository.AuthRepository;
public class LoginViewModel extends AndroidViewModel {
    private final AuthRepository authRepository;
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    public LoginViewModel(@NonNull Application application) {
        super(application);
        authRepository = new AuthRepository(application);
    }
    public LiveData<AuthResponse> login(String email, String password) {
        if (email == null || email.trim().isEmpty()) {
            errorMessage.setValue("L'email est requis");
            return new MutableLiveData<>(null);
        }
        if (password == null || password.length() < 8) {
            errorMessage.setValue("Le mot de passe doit contenir au moins 8 caractères");
            return new MutableLiveData<>(null);
        }
        isLoading.setValue(true);
        errorMessage.setValue(null);
        LiveData<AuthResponse> result = authRepository.login(email.trim(), password);
        isLoading.setValue(false);
        return result;
    }
    public LiveData<Boolean> forgotPassword(String email) {
        if (email == null || email.trim().isEmpty()) {
            errorMessage.setValue("Saisis ton email pour réinitialiser");
            return new MutableLiveData<>(false);
        }
        isLoading.setValue(true);
        LiveData<Boolean> result = authRepository.forgotPassword(email.trim());
        isLoading.setValue(false);
        return result;
    }
    public LiveData<Boolean> getIsLoading() { return isLoading; }
    public LiveData<String> getErrorMessage() { return errorMessage; }
    public boolean isLoggedIn() { return authRepository.isLoggedIn(); }
}
