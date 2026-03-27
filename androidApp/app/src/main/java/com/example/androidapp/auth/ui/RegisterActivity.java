package com.example.androidapp.auth.ui;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.androidapp.R;
import com.example.androidapp.auth.viewmodel.AuthViewModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Locale;
import java.util.regex.Pattern;

/**
 * Écran d'inscription en 2 étapes :
 * Étape 1 : pseudo, email, password, confirm password
 * Étape 2 : saisie OTP 6 chiffres avec countdown
 */
public class RegisterActivity extends AppCompatActivity {

    // [POURQUOI] Regex identique au backend pour validation cohérente
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&#+\\-_])[A-Za-z\\d@$!%*?&#+\\-_]{8,}$");
    private static final Pattern PSEUDO_PATTERN =
            Pattern.compile("^[a-zA-Z0-9_]{3,20}$");

    private AuthViewModel authViewModel;

    // ─── Step 1 Views ────────────────────────────────────────
    private View step1Container;
    private TextInputLayout tilPseudo, tilEmail, tilPassword, tilConfirmPassword;
    private TextInputEditText etPseudo, etEmail, etPassword, etConfirmPassword;
    private ProgressBar passwordStrengthBar;
    private MaterialButton btnRegister;

    // ─── Step 2 (OTP) Views ──────────────────────────────────
    private View step2Container;
    private TextView tvOtpTitle, tvOtpSubtitle, tvCountdown, tvResendOtp;
    private EditText etOtp1, etOtp2, etOtp3, etOtp4, etOtp5, etOtp6;
    private MaterialButton btnVerifyOtp;

    private ProgressBar progressBar;
    private View rootView;

    private CountDownTimer countDownTimer;
    private String registeredEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initViews();
        initViewModel();
        setupValidationWatchers();
        setupListeners();
        observeViewModel();
    }

    // ═══════════════════════════════════════════════════════════
    //  INIT
    // ═══════════════════════════════════════════════════════════

    private void initViews() {
        rootView = findViewById(R.id.register_root);
        progressBar = findViewById(R.id.progress_bar);

        // Step 1
        step1Container = findViewById(R.id.step1_container);
        tilPseudo = findViewById(R.id.til_pseudo);
        tilEmail = findViewById(R.id.til_email);
        tilPassword = findViewById(R.id.til_password);
        tilConfirmPassword = findViewById(R.id.til_confirm_password);
        etPseudo = findViewById(R.id.et_pseudo);
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        etConfirmPassword = findViewById(R.id.et_confirm_password);
        passwordStrengthBar = findViewById(R.id.password_strength_bar);
        btnRegister = findViewById(R.id.btn_register);

        // Step 2
        step2Container = findViewById(R.id.step2_container);
        tvOtpTitle = findViewById(R.id.tv_otp_title);
        tvOtpSubtitle = findViewById(R.id.tv_otp_subtitle);
        tvCountdown = findViewById(R.id.tv_countdown);
        tvResendOtp = findViewById(R.id.tv_resend_otp);
        etOtp1 = findViewById(R.id.et_otp_1);
        etOtp2 = findViewById(R.id.et_otp_2);
        etOtp3 = findViewById(R.id.et_otp_3);
        etOtp4 = findViewById(R.id.et_otp_4);
        etOtp5 = findViewById(R.id.et_otp_5);
        etOtp6 = findViewById(R.id.et_otp_6);
        btnVerifyOtp = findViewById(R.id.btn_verify_otp);

        // Par défaut : step 1 visible
        step1Container.setVisibility(View.VISIBLE);
        step2Container.setVisibility(View.GONE);
    }

    private void initViewModel() {
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
    }

    // ═══════════════════════════════════════════════════════════
    //  VALIDATION EN TEMPS RÉEL
    // ═══════════════════════════════════════════════════════════

    private void setupValidationWatchers() {
        // Pseudo validation
        etPseudo.addTextChangedListener(createWatcher(text -> {
            if (!PSEUDO_PATTERN.matcher(text).matches()) {
                tilPseudo.setError("3-20 caractères alphanumériques");
            } else {
                tilPseudo.setError(null);
            }
        }));

        // Email validation
        etEmail.addTextChangedListener(createWatcher(text -> {
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(text).matches()) {
                tilEmail.setError("Email invalide");
            } else {
                tilEmail.setError(null);
            }
        }));

        // Password strength indicator
        etPassword.addTextChangedListener(createWatcher(text -> {
            updatePasswordStrength(text);
            if (!PASSWORD_PATTERN.matcher(text).matches()) {
                tilPassword.setError("Min 8 chars, 1 majuscule, 1 chiffre, 1 spécial");
            } else {
                tilPassword.setError(null);
            }
        }));

        // Confirm password match
        etConfirmPassword.addTextChangedListener(createWatcher(text -> {
            String password = etPassword.getText() != null ? etPassword.getText().toString() : "";
            if (!text.equals(password)) {
                tilConfirmPassword.setError("Les mots de passe ne correspondent pas");
            } else {
                tilConfirmPassword.setError(null);
            }
        }));

        // OTP auto-focus entre les 6 champs
        setupOtpAutoFocus();
    }

    // ═══════════════════════════════════════════════════════════
    //  LISTENERS
    // ═══════════════════════════════════════════════════════════

    private void setupListeners() {
        btnRegister.setOnClickListener(v -> {
            if (validateStep1()) {
                String pseudo = etPseudo.getText().toString().trim();
                String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                registeredEmail = email;
                authViewModel.register(pseudo, email, password);
            }
        });

        btnVerifyOtp.setOnClickListener(v -> {
            String otp = collectOtp();
            if (otp.length() == 6) {
                authViewModel.verifyOtp(registeredEmail, otp);
            } else {
                showError("Veuillez saisir les 6 chiffres du code");
            }
        });

        tvResendOtp.setOnClickListener(v -> {
            // [POURQUOI] Re-register pour renvoyer un nouvel OTP
            if (registeredEmail != null) {
                String pseudo = etPseudo.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                authViewModel.register(pseudo, registeredEmail, password);
            }
        });

        // Retour
        if (findViewById(R.id.tv_back_to_login) != null) {
            findViewById(R.id.tv_back_to_login).setOnClickListener(v -> finish());
        }
    }

    // ═══════════════════════════════════════════════════════════
    //  OBSERVE VIEWMODEL
    // ═══════════════════════════════════════════════════════════

    private void observeViewModel() {
        authViewModel.getAuthState().observe(this, state -> {
            switch (state) {
                case LOADING:
                    setLoading(true);
                    break;
                case OTP_REQUIRED:
                    setLoading(false);
                    showOtpStep();
                    break;
                case SUCCESS:
                    setLoading(false);
                    // Vérification OTP réussie → retour au login
                    showSuccess("Compte vérifié ! Vous êtes connecté.");
                    finish();
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

    // ═══════════════════════════════════════════════════════════
    //  OTP STEP
    // ═══════════════════════════════════════════════════════════

    private void showOtpStep() {
        step1Container.setVisibility(View.GONE);
        step2Container.setVisibility(View.VISIBLE);

        // Masquer l'email (ex: f***@gmail.com)
        String maskedEmail = maskEmail(registeredEmail);
        tvOtpSubtitle.setText("Un code a été envoyé à " + maskedEmail);

        startCountdown();
    }

    /**
     * [POURQUOI] Countdown de 10 minutes pour correspondre à l'expiry OTP backend.
     */
    private void startCountdown() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        countDownTimer = new CountDownTimer(10 * 60 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long minutes = millisUntilFinished / 60000;
                long seconds = (millisUntilFinished % 60000) / 1000;
                tvCountdown.setText(String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds));
            }

            @Override
            public void onFinish() {
                tvCountdown.setText("00:00");
                tvResendOtp.setVisibility(View.VISIBLE);
                showError("Le code a expiré. Cliquez sur 'Renvoyer le code'.");
            }
        };
        countDownTimer.start();
        tvResendOtp.setVisibility(View.GONE);
    }

    // ═══════════════════════════════════════════════════════════
    //  OTP AUTO-FOCUS
    // ═══════════════════════════════════════════════════════════

    /**
     * [POURQUOI] Auto-focus vers le champ suivant pour une meilleure UX.
     * L'utilisateur n'a qu'à taper les chiffres sans toucher l'écran.
     */
    private void setupOtpAutoFocus() {
        EditText[] otpFields = { etOtp1, etOtp2, etOtp3, etOtp4, etOtp5, etOtp6 };

        for (int i = 0; i < otpFields.length; i++) {
            final int index = i;
            otpFields[i].addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s.length() == 1 && index < otpFields.length - 1) {
                        otpFields[index + 1].requestFocus();
                    }
                    // Auto-submit quand tous les champs sont remplis
                    if (index == otpFields.length - 1 && s.length() == 1) {
                        String otp = collectOtp();
                        if (otp.length() == 6) {
                            btnVerifyOtp.performClick();
                        }
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {}
            });

            // [POURQUOI] Gestion du backspace pour revenir au champ précédent
            otpFields[i].setOnKeyListener((v, keyCode, event) -> {
                if (keyCode == android.view.KeyEvent.KEYCODE_DEL
                        && otpFields[index].getText().toString().isEmpty()
                        && index > 0) {
                    otpFields[index - 1].requestFocus();
                    otpFields[index - 1].setText("");
                    return true;
                }
                return false;
            });
        }
    }

    // ═══════════════════════════════════════════════════════════
    //  HELPERS
    // ═══════════════════════════════════════════════════════════

    private boolean validateStep1() {
        boolean valid = true;

        String pseudo = etPseudo.getText() != null ? etPseudo.getText().toString().trim() : "";
        String email = etEmail.getText() != null ? etEmail.getText().toString().trim() : "";
        String password = etPassword.getText() != null ? etPassword.getText().toString().trim() : "";
        String confirm = etConfirmPassword.getText() != null ? etConfirmPassword.getText().toString().trim() : "";

        if (!PSEUDO_PATTERN.matcher(pseudo).matches()) {
            tilPseudo.setError("3-20 caractères alphanumériques");
            valid = false;
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            tilEmail.setError("Email invalide");
            valid = false;
        }
        if (!PASSWORD_PATTERN.matcher(password).matches()) {
            tilPassword.setError("Min 8 chars, 1 majuscule, 1 chiffre, 1 spécial");
            valid = false;
        }
        if (!password.equals(confirm)) {
            tilConfirmPassword.setError("Les mots de passe ne correspondent pas");
            valid = false;
        }

        return valid;
    }

    private String collectOtp() {
        return String.valueOf(etOtp1.getText()) +
                etOtp2.getText() +
                etOtp3.getText() +
                etOtp4.getText() +
                etOtp5.getText() +
                etOtp6.getText();
    }

    /**
     * Masque l'email : f***@gmail.com
     */
    private String maskEmail(String email) {
        if (email == null || !email.contains("@")) return email;
        int atIndex = email.indexOf('@');
        if (atIndex <= 1) return email;
        return email.charAt(0) + "***" + email.substring(atIndex);
    }

    /**
     * [POURQUOI] Score de force du mot de passe sur 4 critères (100 max).
     */
    private void updatePasswordStrength(String password) {
        int strength = 0;
        if (password.length() >= 8) strength += 25;
        if (password.matches(".*[A-Z].*")) strength += 25;
        if (password.matches(".*\\d.*")) strength += 25;
        if (password.matches(".*[@$!%*?&#+\\-_].*")) strength += 25;
        passwordStrengthBar.setProgress(strength);
    }

    private void setLoading(boolean loading) {
        progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
        btnRegister.setEnabled(!loading);
    }

    private void showError(String message) {
        Snackbar.make(rootView, message, Snackbar.LENGTH_LONG)
                .setBackgroundTint(getColor(com.google.android.material.R.color.design_default_color_error))
                .show();
    }

    private void showSuccess(String message) {
        Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT).show();
    }

    /**
     * Helper pour créer un TextWatcher simplifié.
     */
    private TextWatcher createWatcher(java.util.function.Consumer<String> onChanged) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                onChanged.accept(s.toString());
            }
        };
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}
