package com.codequest.ui.auth;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.codequest.ui.base.BaseActivity;
import androidx.lifecycle.ViewModelProvider;
import com.codequest.R;
import com.codequest.viewmodel.RegisterViewModel;
public class RegisterActivity extends BaseActivity {
    private RegisterViewModel viewModel;
    private LinearLayout step1Container, step2Container;
    private EditText etPseudo, etEmail, etPassword, etConfirmPassword;
    private ImageView togglePassword, toggleConfirmPassword, btnBack;
    private RadioGroup rgLevel;
    private Button btnNext;
    private TextView tvStepIndicator, tvError;
    private boolean isStep2 = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        viewModel = new ViewModelProvider(this).get(RegisterViewModel.class);
        initViews();
        setupListeners();
        observeViewModel();
    }
    private void initViews() {
        step1Container = findViewById(R.id.step1Container);
        step2Container = findViewById(R.id.step2Container);
        etPseudo = findViewById(R.id.etPseudo);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        togglePassword = findViewById(R.id.togglePassword);
        toggleConfirmPassword = findViewById(R.id.toggleConfirmPassword);
        btnBack = findViewById(R.id.btnBack);
        rgLevel = findViewById(R.id.rgLevel);
        btnNext = findViewById(R.id.btnNext);
        tvStepIndicator = findViewById(R.id.tvStepIndicator);
        tvError = findViewById(R.id.tvError);
    }
    private void setupListeners() {
        btnBack.setOnClickListener(v -> onBackPressed());

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

        toggleConfirmPassword.setOnClickListener(v -> {
            boolean isVisible = etConfirmPassword.getTransformationMethod() == null;
            if (isVisible) {
                etConfirmPassword.setTransformationMethod(new android.text.method.PasswordTransformationMethod());
                toggleConfirmPassword.setImageResource(R.drawable.ic_visibility_off);
            } else {
                etConfirmPassword.setTransformationMethod(null);
                toggleConfirmPassword.setImageResource(R.drawable.ic_visibility);
            }
            etConfirmPassword.setSelection(etConfirmPassword.getText().length());
        });

        btnNext.setOnClickListener(v -> {
            if (!isStep2) {
                String pseudo = etPseudo.getText().toString().trim();
                String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString();
                String confirmPassword = etConfirmPassword.getText().toString();
                if (!password.equals(confirmPassword)) {
                    tvError.setText("Les mots de passe ne correspondent pas");
                    tvError.setVisibility(View.VISIBLE);
                    return;
                }
                if (viewModel.validateStep1(pseudo, email, password)) {
                    showStep2();
                }
            } else {
                int selectedId = rgLevel.getCheckedRadioButtonId();
                if (selectedId == R.id.rbBeginner) {
                    viewModel.setSelectedLevel("BEGINNER");
                } else if (selectedId == R.id.rbIntermediate) {
                    viewModel.setSelectedLevel("INTERMEDIATE");
                } else if (selectedId == R.id.rbAdvanced) {
                    viewModel.setSelectedLevel("ADVANCED");
                }
                viewModel.register().observe(this, response -> {
                    if (response != null && response.getError() == null) {
                        Intent intent = new Intent(this, OTPActivity.class);
                        intent.putExtra("email", viewModel.getEmail());
                        startActivity(intent);
                    } else if (response != null && response.getError() != null) {
                        tvError.setText(response.getError());
                        tvError.setVisibility(View.VISIBLE);
                    } else {
                        tvError.setText(R.string.error_register);
                        tvError.setVisibility(View.VISIBLE);
                    }
                });
            }
        });
    }
    private void showStep2() {
        isStep2 = true;
        step1Container.setVisibility(View.GONE);
        step2Container.setVisibility(View.VISIBLE);
        tvStepIndicator.setText(R.string.step_2_of_2);
        btnNext.setText(R.string.register_btn);
        tvError.setVisibility(View.GONE);
    }
    private void observeViewModel() {
        viewModel.getErrorMessage().observe(this, error -> {
            if (error != null) {
                tvError.setText(error);
                tvError.setVisibility(View.VISIBLE);
            } else {
                tvError.setVisibility(View.GONE);
            }
        });
    }
    @Override
    public void onBackPressed() {
        if (isStep2) {
            isStep2 = false;
            step2Container.setVisibility(View.GONE);
            step1Container.setVisibility(View.VISIBLE);
            tvStepIndicator.setText(R.string.step_1_of_2);
            btnNext.setText(R.string.next_btn);
        } else {
            super.onBackPressed();
        }
    }
}

