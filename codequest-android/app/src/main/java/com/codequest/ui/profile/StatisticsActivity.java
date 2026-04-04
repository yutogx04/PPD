package com.codequest.ui.profile;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.codequest.ui.base.BaseActivity;
import androidx.lifecycle.ViewModelProvider;

import com.codequest.R;
import com.codequest.model.UserStats;
import com.codequest.viewmodel.StatisticsViewModel;

public class StatisticsActivity extends BaseActivity {

    private StatisticsViewModel viewModel;
    private ProgressBar progressBar;

    private TextView tvLevelTitle;
    private TextView tvMemberSince;

    private TextView tvXp;
    private TextView tvStreak;
    private TextView tvChallenges;
    private TextView tvLessons;
    private TextView tvBadges;
    private TextView tvTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        viewModel = new ViewModelProvider(this).get(StatisticsViewModel.class);

        ImageView ivBack = findViewById(R.id.ivBack);
        progressBar = findViewById(R.id.progressBar);
        tvLevelTitle = findViewById(R.id.tvLevelTitle);
        tvMemberSince = findViewById(R.id.tvMemberSince);
        
        tvXp = findViewById(R.id.tvXp);
        tvStreak = findViewById(R.id.tvStreak);
        tvChallenges = findViewById(R.id.tvChallenges);
        tvLessons = findViewById(R.id.tvLessons);
        tvBadges = findViewById(R.id.tvBadges);
        tvTime = findViewById(R.id.tvTime);

        ivBack.setOnClickListener(v -> finish());

        progressBar.setVisibility(View.VISIBLE);

        viewModel.getUserStats().observe(this, stats -> {
            progressBar.setVisibility(View.GONE);
            if (stats != null) {
                populateStats(stats);
            } else {
                Toast.makeText(this, getString(R.string.error_loading_stats), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void populateStats(UserStats stats) {
        try {
            String levelName = stats.getLevelName();
            if (levelName == null) levelName = getString(R.string.filter_beginner);
            tvLevelTitle.setText(getString(R.string.stat_level_format, stats.getLevel(), levelName));
            
            String member = stats.getMemberSince();
            if (member != null && !member.isEmpty()) {
                if (member.contains("T")) {
                    member = member.substring(0, member.indexOf("T"));
                }
                tvMemberSince.setText(getString(R.string.stat_member_since, member));
            } else {
                tvMemberSince.setText(getString(R.string.stat_member_recent));
            }

            tvXp.setText(String.valueOf(stats.getXp()));
            tvStreak.setText(String.valueOf(stats.getStreak()));
            tvChallenges.setText(String.valueOf(stats.getTotalChallengesSolved()));
            tvLessons.setText(String.valueOf(stats.getTotalLessonsCompleted()));
            tvBadges.setText(String.valueOf(stats.getTotalBadges()));
            
            int estMinutes = (stats.getTotalLessonsCompleted() * 10) + (stats.getTotalChallengesSolved() * 15);
            int hours = estMinutes / 60;
            tvTime.setText(String.valueOf(hours) + "h");
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Erreur interne: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}

