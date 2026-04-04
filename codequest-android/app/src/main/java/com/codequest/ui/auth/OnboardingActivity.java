package com.codequest.ui.auth;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.codequest.ui.base.BaseActivity;
import androidx.viewpager2.widget.ViewPager2;
import com.codequest.R;
import com.codequest.ui.adapter.OnboardingAdapter;
import com.codequest.util.LocaleHelper;
import com.codequest.util.SharedPrefManager;

public class OnboardingActivity extends BaseActivity {
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.setLocale(newBase, LocaleHelper.getLanguage(newBase)));
    }
    private ViewPager2 viewPager;
    private LinearLayout dotsContainer;
    private Button btnGetStarted;
    private TextView tvSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPrefManager prefs = SharedPrefManager.getInstance(this);
        if (prefs.isOnboardingDone()) {
            navigateToLogin();
            return;
        }
        setContentView(R.layout.activity_onboarding);

        getWindow().setStatusBarColor(getColor(R.color.onboarding_start));

        viewPager = findViewById(R.id.viewPagerOnboarding);
        dotsContainer = findViewById(R.id.dotsContainer);
        btnGetStarted = findViewById(R.id.btnGetStarted);
        tvSignIn = findViewById(R.id.tvSignIn);

        String[] titles = {
                getString(R.string.onboarding_title_1),
                getString(R.string.onboarding_title_2),
                getString(R.string.onboarding_title_3)
        };
        String[] descs = {
                getString(R.string.onboarding_desc_1),
                getString(R.string.onboarding_desc_2),
                getString(R.string.onboarding_desc_3)
        };
        int[] icons = {
                R.drawable.ic_code_slide,
                R.drawable.ic_challenge_slide,
                R.drawable.ic_progress_slide
        };
        OnboardingAdapter adapter = new OnboardingAdapter(titles, descs, icons);
        viewPager.setAdapter(adapter);
        setupDots(0);
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                setupDots(position);
            }
        });
        btnGetStarted.setOnClickListener(v -> {
            prefs.setOnboardingDone();
            startActivity(new Intent(this, RegisterActivity.class));
            finish();
        });
        tvSignIn.setOnClickListener(v -> {
            prefs.setOnboardingDone();
            navigateToLogin();
        });
    }

    private void setupDots(int activePosition) {
        dotsContainer.removeAllViews();
        for (int i = 0; i < 3; i++) {
            View dot = new View(this);
            LinearLayout.LayoutParams params;
            if (i == activePosition) {
                params = new LinearLayout.LayoutParams(24, 8);
                dot.setBackgroundColor(getColor(R.color.primary));
            } else {
                params = new LinearLayout.LayoutParams(8, 8);
                dot.setBackgroundColor(getColor(R.color.primary_light));
                dot.setAlpha(0.4f);
            }
            params.setMargins(4, 0, 4, 0);
            dot.setLayoutParams(params);
            
            dot.setBackground(null);
            android.graphics.drawable.GradientDrawable shape = new android.graphics.drawable.GradientDrawable();
            shape.setShape(android.graphics.drawable.GradientDrawable.RECTANGLE);
            shape.setCornerRadius(4);
            if (i == activePosition) {
                shape.setColor(getColor(R.color.primary));
            } else {
                shape.setColor(0x66A78BFA); 
            }
            dot.setBackground(shape);
            dotsContainer.addView(dot);
        }
    }

    private void navigateToLogin() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}

