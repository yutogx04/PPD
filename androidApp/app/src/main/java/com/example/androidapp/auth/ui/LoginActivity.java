package com.example.androidapp.auth.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.androidapp.MainActivity;
import com.example.androidapp.R;
import com.example.androidapp.auth.helper.GoogleSignInHelper;
import com.example.androidapp.auth.network.TokenManager;
import com.example.androidapp.auth.viewmodel.AuthViewModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

/**
 * Écran de connexion.
 * [POURQUOI] Implémente GoogleSignIn via ActivityResultLauncher (API moderne)
 * au lieu du deprecated onActivityResult.
 */
public class LoginActivity extends AppCompatActivity {

    private AuthViewModel authViewModel;
    private GoogleSignInHelper googleSignInHelper;

    // ─── UI Elements ─────────────────────────────────────────
    private TextInputLayout tilEmail, tilPassword;
    private TextInputEditText etEmail, etPassword;
    private MaterialButton btnLogin, btnGoogle;
    private ProgressBar progressBar;
    private View rootView;

    // [POURQUOI] ActivityResultLauncher remplace startActivityForResult (deprecated)
    private final ActivityResultLauncher<Intent> googleSignInLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getData() != null) {
                            String idToken = googleSignInHelper.handleSignInResult(result.getData());
                            if (idToken != null) {
                                authViewModel.loginWithGoogle(idToken);
                            } else {
                                showError("Connexion Google annulée ou échouée");
                            }
                        }
                    }
            );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // [POURQUOI] Vérifier si déjà connecté avant d'afficher l'écran
        TokenManager tokenManager = new TokenManager(this);
        if (tokenManager.isLoggedIn()) {
            navigateToMain();
            return;
        }

        setContentView(R.layout.activity_login);

        initViews();
        initViewModel();
        initGoogleSignIn();
        setupListeners();
        observeViewModel();
    }

    private void initViews() {
        rootView = findViewById(R.id.login_root);
        tilEmail = findViewById(R.id.til_email);
        tilPassword = findViewById(R.id.til_password);
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);
        btnGoogle = findViewById(R.id.btn_google);
        progressBar = findViewById(R.id.progress_bar);
    }

    private void initViewModel() {
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
    }

    private void initGoogleSignIn() {
        googleSignInHelper = new GoogleSignInHelper(this);
    }

    private void setupListeners() {
        btnLogin.setOnClickListener(v -> {
            if (validateFields()) {
                String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                authViewModel.login(email, password);
            }
        });

        btnGoogle.setOnClickListener(v -> {
            Intent signInIntent = googleSignInHelper.getSignInIntent();
            googleSignInLauncher.launch(signInIntent);
        });

        // Navigation vers l'inscription
        findViewById(R.id.tv_register).setOnClickListener(v -> {
            startActivity(new Intent(this, RegisterActivity.class));
        });

        // Navigation vers mot de passe oublié
        findViewById(R.id.tv_forgot_password).setOnClickListener(v -> {
            // [POURQUOI] TODO: Implémenter un ForgotPasswordActivity ou Dialog
            // Pour l'instant on montre un Snackbar
            showError("Fonctionnalité de récupération à implémenter dans un écran dédié");
        });
    }

    private void observeViewModel() {
        authViewModel.getAuthState().observe(this, state -> {
            switch (state) {
                case LOADING:
                    setLoading(true);
                    break;
                case SUCCESS:
                    setLoading(false);
                    navigateToMain();
                    break;
                case ERROR:
                    setLoading(false);
                    break;
                case IDLE:
                    setLoading(false);
                    break;
            }
        });

        authViewModel.getErrorMessage().observe(this, error -> {
            if (error != null) {
                showError(error);
                authViewModel.resetState();
            }
        });
    }

    // ─── Validation ──────────────────────────────────────────

    private boolean validateFields() {
        boolean valid = true;

        String email = etEmail.getText() != null ? etEmail.getText().toString().trim() : "";
        String password = etPassword.getText() != null ? etPassword.getText().toString().trim() : "";

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            tilEmail.setError("Email invalide");
            valid = false;
        } else {
            tilEmail.setError(null);
        }

        if (password.isEmpty()) {
            tilPassword.setError("Mot de passe requis");
            valid = false;
        } else {
            tilPassword.setError(null);
        }

        return valid;
    }

    // ─── UI Helpers ──────────────────────────────────────────

    private void setLoading(boolean loading) {
        progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
        btnLogin.setEnabled(!loading);
        btnGoogle.setEnabled(!loading);
    }

    private void showError(String message) {
        Snackbar.make(rootView, message, Snackbar.LENGTH_LONG)
                .setBackgroundTint(getColor(com.google.android.material.R.color.design_default_color_error))
                .show();
    }

    private void navigateToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
