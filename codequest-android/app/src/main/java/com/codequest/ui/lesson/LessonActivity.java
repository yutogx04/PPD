package com.codequest.ui.lesson;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.codequest.ui.base.BaseActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;
import com.codequest.R;
import com.codequest.ui.adapter.SlideAdapter;
import com.codequest.util.LocaleHelper;
import com.codequest.util.SharedPrefManager;
import com.codequest.viewmodel.LessonViewModel;
public class LessonActivity extends BaseActivity {
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.setLocale(newBase, LocaleHelper.getLanguage(newBase)));
    }
    private LessonViewModel viewModel;
    private ViewPager2 viewPagerSlides;
    private ProgressBar progressBar;
    private Button btnPrevious, btnNext;
    private TextView tvSlideIndicator, tvLessonTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson);
        viewModel = new ViewModelProvider(this).get(LessonViewModel.class);
        long lessonId = getIntent().getLongExtra("lessonId", 1);
        int startSlide = getIntent().getIntExtra("startSlide", 0);
        String title = getIntent().getStringExtra("lessonTitle");
        initViews();
        tvLessonTitle.setText(title != null ? title : "Lesson");
        viewModel.loadLesson(lessonId, startSlide);
        observeData();
        setupNavigation();
    }
    private void initViews() {
        viewPagerSlides = findViewById(R.id.viewPagerSlides);
        progressBar = findViewById(R.id.progressLesson);
        btnPrevious = findViewById(R.id.btnPrevious);
        btnNext = findViewById(R.id.btnNext);
        tvSlideIndicator = findViewById(R.id.tvSlideIndicator);
        tvLessonTitle = findViewById(R.id.tvLessonTitle);
        
        View btnClose = findViewById(R.id.btnClose);
        if (btnClose != null) btnClose.setOnClickListener(v -> finish());
        viewPagerSlides.setUserInputEnabled(false);
    }
    private void observeData() {
        viewModel.getSlides().observe(this, slides -> {
            if (slides != null) {
                viewModel.setTotalSlides(slides.size());
                SlideAdapter adapter = new SlideAdapter(slides);
                viewPagerSlides.setAdapter(adapter);
                int startIndex = viewModel.getCurrentSlideIndex().getValue() != null ?
                        viewModel.getCurrentSlideIndex().getValue() : 0;
                viewPagerSlides.setCurrentItem(startIndex, false);
                updateUI();
            }
        });
        viewModel.getCurrentSlideIndex().observe(this, index -> {
            viewPagerSlides.setCurrentItem(index, true);
            updateUI();
        });
    }
    private void setupNavigation() {
        btnNext.setOnClickListener(v -> {
            if (viewModel.isLastSlide()) {
                viewModel.completeLesson().observe(this, result -> {
                    SharedPrefManager prefs = SharedPrefManager.getInstance(this);
                    prefs.clearLessonPosition(viewModel.getLessonId());
                    Intent intent = new Intent(this, LessonCompleteActivity.class);
                    intent.putExtra("lessonTitle", tvLessonTitle.getText().toString());
                    if (result != null) {
                        intent.putExtra("xpGained", result.getXpGained());
                        intent.putExtra("totalXp", result.getNewXpTotal());
                        intent.putExtra("userLevel", result.getNewLevel());
                        if (result.hasBadges()) {
                            intent.putExtra("badgeName", result.getBadgesUnlocked().get(0).getName());
                        }
                    } else {
                        intent.putExtra("xpGained", 20);
                    }
                    startActivity(intent);
                    finish();
                });
            } else {
                viewModel.nextSlide();
            }
        });
        btnPrevious.setOnClickListener(v -> viewModel.previousSlide());
    }
    @Override
    protected void onPause() {
        super.onPause();
        int currentIndex = viewModel.getCurrentSlideIndex().getValue() != null ?
                viewModel.getCurrentSlideIndex().getValue() : 0;
        SharedPrefManager.getInstance(this)
                .saveLessonPosition(viewModel.getLessonId(), currentIndex);
    }
    private void updateUI() {
        int current = viewModel.getCurrentSlideIndex().getValue() != null ?
                viewModel.getCurrentSlideIndex().getValue() : 0;
        int total = viewModel.getSlides().getValue() != null ?
                viewModel.getSlides().getValue().size() : 0;
        tvSlideIndicator.setText(String.format("%d / %d", current + 1, total));
        progressBar.setProgress(viewModel.getProgressPercent());
        btnPrevious.setVisibility(viewModel.isFirstSlide() ? View.INVISIBLE : View.VISIBLE);
        btnNext.setText(viewModel.isLastSlide() ?
                getString(R.string.complete_lesson) : getString(R.string.next_slide));
    }
}

