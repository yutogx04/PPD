package com.codequest.ui.auth;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.codequest.ui.base.BaseActivity;
import androidx.lifecycle.ViewModelProvider;
import com.codequest.R;
import com.codequest.viewmodel.LoginViewModel;
public class ForgotPasswordActivity extends BaseActivity {
    private LoginViewModel viewModel;
    private EditText etEmail;
    private Button btnSend;
    private TextView tvError, tvSuccess;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        etEmail = findViewById(R.id.etEmail);
        btnSend = findViewById(R.id.btnSend);
        tvError = findViewById(R.id.tvError);
        tvSuccess = findViewById(R.id.tvSuccess);
        progressBar = findViewById(R.id.progressBar);
        ImageView ivBack = findViewById(R.id.ivBack);
        ivBack.setOnClickListener(v -> finish());
        btnSend.setOnClickListener(v -> {
            String email = etEmail.getText().toString();
            viewModel.forgotPassword(email).observe(this, success -> {
                if (success != null && success) {
                    tvSuccess.setVisibility(View.VISIBLE);
                    tvError.setVisibility(View.GONE);
                    btnSend.setEnabled(false);
                } else {
                    tvError.setText(R.string.error_network);
                    tvError.setVisibility(View.VISIBLE);
                }
            });
        });
        viewModel.getIsLoading().observe(this, loading -> {
            progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
            btnSend.setEnabled(!loading);
        });
        viewModel.getErrorMessage().observe(this, error -> {
            if (error != null) {
                tvError.setText(error);
                tvError.setVisibility(View.VISIBLE);
            }
        });
    }
}

