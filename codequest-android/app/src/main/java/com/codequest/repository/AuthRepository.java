package com.codequest.repository;
import android.content.Context;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.codequest.model.User;
import com.codequest.model.dto.AuthResponse;
import com.codequest.model.dto.ForgotPasswordRequest;
import com.codequest.model.dto.LoginRequest;
import com.codequest.model.dto.RegisterRequest;
import com.codequest.model.dto.VerifyOTPRequest;
import com.codequest.network.RetrofitClient;
import com.codequest.util.SharedPrefManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import org.json.JSONObject;
public class AuthRepository {
    private final SharedPrefManager prefManager;
    private final boolean useMockData = false; 
    public AuthRepository(Context context) {
        this.prefManager = SharedPrefManager.getInstance(context);
    }
    public LiveData<AuthResponse> login(String email, String password) {
        MutableLiveData<AuthResponse> result = new MutableLiveData<>();
        if (useMockData) {
            User mockUser = new User(1, "CodeWizard", 840, 3, 12);
            mockUser.setEmail(email);
            mockUser.setTotalLessonsCompleted(18);
            mockUser.setTotalChallengesSolved(9);
            AuthResponse mockResponse = new AuthResponse();
            mockResponse.setAccessToken("mock_jwt_token_123");
            mockResponse.setRefreshToken("mock_refresh_token_456");
            mockResponse.setUser(mockUser);
            prefManager.saveTokens(mockResponse.getAccessToken(), mockResponse.getRefreshToken());
            prefManager.saveUserInfo(1, "CodeWizard", 840, 3, 12);
            result.setValue(mockResponse);
            return result;
        }
        RetrofitClient.getApi().login(new LoginRequest(email, password))
                .enqueue(new Callback<AuthResponse>() {
                    @Override
                    public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            AuthResponse auth = response.body();
                            prefManager.saveTokens(auth.getAccessToken(), auth.getRefreshToken());
                            User u = auth.getUser();
                            prefManager.saveUserInfo(u.getId(), u.getPseudo(), u.getXp(), u.getLevel(), u.getStreak());
                            result.setValue(auth);
                        } else {
                            result.setValue(parseError(response));
                        }
                    }
                    @Override
                    public void onFailure(Call<AuthResponse> call, Throwable t) {
                        result.setValue(null);
                    }
                });
        return result;
    }
    public LiveData<AuthResponse> register(String pseudo, String email, String password, String level) {
        MutableLiveData<AuthResponse> result = new MutableLiveData<>();
        if (useMockData) {
            AuthResponse mockResponse = new AuthResponse();
            mockResponse.setAccessToken("mock_jwt_after_register");
            result.setValue(mockResponse);
            return result;
        }
        RetrofitClient.getApi().register(new RegisterRequest(pseudo, email, password, level))
                .enqueue(new Callback<AuthResponse>() {
                    @Override
                    public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            result.setValue(response.body());
                        } else {
                            result.setValue(parseError(response));
                        }
                    }
                    @Override
                    public void onFailure(Call<AuthResponse> call, Throwable t) {
                        result.setValue(null);
                    }
                });
        return result;
    }
    public LiveData<AuthResponse> verifyOTP(String email, String otpCode) {
        MutableLiveData<AuthResponse> result = new MutableLiveData<>();
        if (useMockData) {
            User mockUser = new User(1, "CodeWizard", 0, 1, 0);
            AuthResponse mockResponse = new AuthResponse();
            mockResponse.setAccessToken("mock_jwt_after_otp");
            mockResponse.setRefreshToken("mock_refresh_after_otp");
            mockResponse.setUser(mockUser);
            prefManager.saveTokens(mockResponse.getAccessToken(), mockResponse.getRefreshToken());
            prefManager.saveUserInfo(1, "CodeWizard", 0, 1, 0);
            result.setValue(mockResponse);
            return result;
        }
        RetrofitClient.getApi().verifyEmail(new VerifyOTPRequest(email, otpCode))
                .enqueue(new Callback<AuthResponse>() {
                    @Override
                    public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            AuthResponse auth = response.body();
                            prefManager.saveTokens(auth.getAccessToken(), auth.getRefreshToken());
                            User u = auth.getUser();
                            prefManager.saveUserInfo(u.getId(), u.getPseudo(), u.getXp(), u.getLevel(), u.getStreak());
                            result.setValue(auth);
                        } else {
                            result.setValue(parseError(response));
                        }
                    }
                    @Override
                    public void onFailure(Call<AuthResponse> call, Throwable t) {
                        result.setValue(null);
                    }
                });
        return result;
    }
    public LiveData<Boolean> forgotPassword(String email) {
        MutableLiveData<Boolean> result = new MutableLiveData<>();
        if (useMockData) {
            result.setValue(true);
            return result;
        }
        RetrofitClient.getApi().forgotPassword(new ForgotPasswordRequest(email))
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        result.setValue(response.isSuccessful());
                    }
                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        result.setValue(false);
                    }
                });
        return result;
    }
    public void logout() {
        prefManager.logout();
    }
    public boolean isLoggedIn() {
        return prefManager.isLoggedIn();
    }
    private AuthResponse parseError(Response<?> response) {
        AuthResponse err = new AuthResponse();
        try {
            if (response.errorBody() != null) {
                JSONObject jsonObject = new JSONObject(response.errorBody().string());
                if (jsonObject.has("error")) {
                    err.setError(jsonObject.getString("error"));
                } else {
                    err.setError("Erreur (" + response.code() + ")");
                }
            } else {
                err.setError("Erreur (" + response.code() + ")");
            }
        } catch (Exception e) {
            err.setError("Erreur serveur");
        }
        return err;
    }
}
