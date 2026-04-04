package com.codequest.ui.auth;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.codequest.viewmodel.RegisterViewModel;
public class OTPActivity extends BaseActivity {
    private RegisterViewModel viewModel;
    private EditText[] otpFields;
    private Button btnVerify;
    private TextView tvError, tvOtpSubtitle, tvResend;
    private ProgressBar progressBar;
    private String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        viewModel = new ViewModelProvider(this).get(RegisterViewModel.class);
        email = getIntent().getStringExtra("email");
        initViews();
        setupOtpAutoFocus();
        setupListeners();
        if (email != null) {
            viewModel.setEmail(email);
            tvOtpSubtitle.setText(getString(R.string.otp_subtitle) + " " + email);
        }
    }
    private void initViews() {
        otpFields = new EditText[]{
                findViewById(R.id.etOtp1),
                findViewById(R.id.etOtp2),
                findViewById(R.id.etOtp3),
                findViewById(R.id.etOtp4),
                findViewById(R.id.etOtp5),
                findViewById(R.id.etOtp6)
        };
        btnVerify = findViewById(R.id.btnVerify);
        tvError = findViewById(R.id.tvError);
        tvOtpSubtitle = findViewById(R.id.tvOtpSubtitle);
        tvResend = findViewById(R.id.tvResend);
        progressBar = findViewById(R.id.progressBar);
    }
    private void setupOtpAutoFocus() {
        for (int i = 0; i < otpFields.length; i++) {
            final int index = i;
            otpFields[i].addTextChangedListener(new TextWatcher() {
                @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
                @Override
                public void afterTextChanged(Editable s) {
                    if (s.length() == 1 && index < otpFields.length - 1) {
                        otpFields[index + 1].requestFocus();
                    }
                }
            });
        }
    }
    private void setupListeners() {
        btnVerify.setOnClickListener(v -> {
            StringBuilder otp = new StringBuilder();
            for (EditText field : otpFields) {
                otp.append(field.getText().toString());
            }
            viewModel.verifyOTP(otp.toString()).observe(this, response -> {
                if (response != null && response.getError() == null) {
                    Intent intent = new Intent(this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                } else if (response != null && response.getError() != null) {
                    tvError.setText(response.getError());
                    tvError.setVisibility(View.VISIBLE);
                } else {
                    tvError.setText(R.string.error_otp);
                    tvError.setVisibility(View.VISIBLE);
                }
            });
        });
        tvResend.setOnClickListener(v -> {
            tvResend.setEnabled(false);
            tvResend.postDelayed(() -> tvResend.setEnabled(true), 30000);
        });
    }
}

