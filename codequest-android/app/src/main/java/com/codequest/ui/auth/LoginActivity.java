package com.codequest.ui.auth;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.codequest.ui.base.BaseActivity;
import androidx.lifecycle.ViewModelProvider;
import com.codequest.R;
import com.codequest.ui.main.MainActivity;
import com.codequest.util.LocaleHelper;
import com.codequest.viewmodel.LoginViewModel;
public class LoginActivity extends BaseActivity {
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.setLocale(newBase, LocaleHelper.getLanguage(newBase)));
    }
    private LoginViewModel viewModel;
    private EditText etEmail, etPassword;
    private Button btnLogin, btnGoogle;
    private TextView tvError, tvForgotPassword, tvCreateAccount;
    private android.widget.ImageView togglePassword;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        if (viewModel.isLoggedIn()) {
            navigateToMain();
            return;
        }
        initViews();
        setupListeners();
        observeViewModel();
    }
    private void initViews() {
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnGoogle = findViewById(R.id.btnGoogle);
        tvError = findViewById(R.id.tvError);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
        tvCreateAccount = findViewById(R.id.tvCreateAccount);
        togglePassword = findViewById(R.id.togglePassword);
        progressBar = findViewById(R.id.progressBar);
    }
    private void setupListeners() {
        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText().toString();
            String password = etPassword.getText().toString();
            viewModel.login(email, password).observe(this, response -> {
                if (response != null && response.getError() == null) {
                    navigateToMain();
                } else if (response != null && response.getError() != null) {
                    tvError.setText(response.getError());
                    tvError.setVisibility(View.VISIBLE);
                } else {
                    tvError.setText(R.string.error_login);
                    tvError.setVisibility(View.VISIBLE);
                }
            });
        });
        tvForgotPassword.setOnClickListener(v -> {
            startActivity(new Intent(this, ForgotPasswordActivity.class));
        });
        tvCreateAccount.setOnClickListener(v -> {
            startActivity(new Intent(this, RegisterActivity.class));
        });
        togglePassword.setOnClickListener(v -> {
            boolean isVisible = etPassword.getTransformationMethod() == null;
            if (isVisible) {
                etPassword.setTransformationMethod(new android.text.method.PasswordTransformationMethod());
                togglePassword.setImageResource(R.drawable.ic_visibility_off);
            } else {
                etPassword.setTransformationMethod(null);
                togglePassword.setImageResource(R.drawable.ic_visibility);
            }
            etPassword.setSelection(etPassword.getText().length());
        });
        btnGoogle.setOnClickListener(v -> {
            btnGoogle.setEnabled(false);
            progressBar.setVisibility(View.VISIBLE);
            com.codequest.util.GoogleSignInHelper.signIn(this, new com.codequest.util.GoogleSignInHelper.GoogleSignInCallback() {
                @Override
                public void onSuccess(String idToken) {
                    
                    java.util.Map<String, String> body = new java.util.HashMap<>();
                    body.put("idToken", idToken);
                    com.codequest.network.RetrofitClient.getApi().googleSignIn(body)
                            .enqueue(new retrofit2.Callback<com.codequest.model.dto.AuthResponse>() {
                                @Override
                                public void onResponse(retrofit2.Call<com.codequest.model.dto.AuthResponse> call,
                                                       retrofit2.Response<com.codequest.model.dto.AuthResponse> response) {
                                    runOnUiThread(() -> {
                                        progressBar.setVisibility(View.GONE);
                                        btnGoogle.setEnabled(true);
                                        if (response.isSuccessful() && response.body() != null) {
                                            com.codequest.model.dto.AuthResponse auth = response.body();
                                            com.codequest.util.SharedPrefManager prefs =
                                                    com.codequest.util.SharedPrefManager.getInstance(LoginActivity.this);
                                            prefs.saveTokens(auth.getAccessToken(), auth.getRefreshToken());
                                            if (auth.getUser() != null) {
                                                com.codequest.model.User u = auth.getUser();
                                                prefs.saveUserInfo(u.getId(), u.getPseudo(), u.getXp(), u.getLevel(), u.getStreak());
                                            }
                                            navigateToMain();
                                        } else {
                                            String err = "Google login failed (" + response.code() + ")";
                                            try {
                                                if (response.errorBody() != null) {
                                                    err += ": " + response.errorBody().string();
                                                }
                                            } catch(Exception ignored) {}
                                            tvError.setText(err);
                                            tvError.setVisibility(View.VISIBLE);
                                        }
                                    });
                                }

                                @Override
                                public void onFailure(retrofit2.Call<com.codequest.model.dto.AuthResponse> call, Throwable t) {
                                    runOnUiThread(() -> {
                                        progressBar.setVisibility(View.GONE);
                                        btnGoogle.setEnabled(true);
                                        tvError.setText("Network error: " + t.getMessage());
                                        tvError.setVisibility(View.VISIBLE);
                                    });
                                }
                            });
                }

                @Override
                public void onFailure(String errorMessage) {
                    runOnUiThread(() -> {
                        progressBar.setVisibility(View.GONE);
                        btnGoogle.setEnabled(true);
                        tvError.setText(errorMessage);
                        tvError.setVisibility(View.VISIBLE);
                    });
                }
            });
        });
    }
    private void observeViewModel() {
        viewModel.getIsLoading().observe(this, isLoading -> {
            progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            btnLogin.setEnabled(!isLoading);
        });
        viewModel.getErrorMessage().observe(this, error -> {
            if (error != null) {
                tvError.setText(error);
                tvError.setVisibility(View.VISIBLE);
            } else {
                tvError.setVisibility(View.GONE);
            }
        });
    }
    private void navigateToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}

