package com.codequest.ui.lesson;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.codequest.ui.base.BaseActivity;
import com.codequest.R;
import com.codequest.util.Constants;
public class LessonCompleteActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson_complete);
        int xpGained = getIntent().getIntExtra("xpGained", 20);
        String lessonTitle = getIntent().getStringExtra("lessonTitle");
        String badgeName = getIntent().getStringExtra("badgeName");
        int totalXp = getIntent().getIntExtra("totalXp", 0);
        int userLevel = getIntent().getIntExtra("userLevel", 1);
        TextView tvLessonTitle = findViewById(R.id.tvLessonTitle);
        TextView tvXpGained = findViewById(R.id.tvXpGained);
        TextView tvLevelProgress = findViewById(R.id.tvLevelProgress);
        ProgressBar progressLevel = findViewById(R.id.progressLevel);
        LinearLayout badgeContainer = findViewById(R.id.badgeContainer);
        TextView tvBadgeName = findViewById(R.id.tvBadgeName);
        Button btnContinue = findViewById(R.id.btnContinue);
        Button btnShare = findViewById(R.id.btnShare);
        tvLessonTitle.setText(lessonTitle != null ? lessonTitle : "");
        tvXpGained.setText(String.format("+%d XP", xpGained));
        int xpForNextLevel = Constants.getXpForLevel(userLevel + 1);
        int xpProgress = (int) ((float) totalXp / xpForNextLevel * 100);
        tvLevelProgress.setText(String.format("%d / %d XP", totalXp, xpForNextLevel));
        progressLevel.setProgress(Math.min(xpProgress, 100));
        if (badgeName != null) {
            badgeContainer.setVisibility(View.VISIBLE);
            tvBadgeName.setText(badgeName);
        }
        btnContinue.setOnClickListener(v -> finish());
        btnShare.setOnClickListener(v -> {
            String shareText = String.format(
                    "I just finished the lesson \"%s\" on CodeQuest and earned +%d XP!",
                    lessonTitle != null ? lessonTitle : "a lesson", xpGained);
            android.content.Intent shareIntent = new android.content.Intent(android.content.Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareText);
            startActivity(android.content.Intent.createChooser(shareIntent, "Partager via"));
        });
    }
}

