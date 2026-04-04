package com.codequest.ui.catalogue;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.codequest.ui.base.BaseActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.codequest.R;
import com.codequest.ui.adapter.LessonAdapter;
import com.codequest.ui.challenge.EditorActivity;
import com.codequest.ui.lesson.LessonActivity;
import com.codequest.viewmodel.TrackDetailViewModel;
public class ModuleDetailActivity extends BaseActivity {
    private TrackDetailViewModel viewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_module_detail);
        viewModel = new ViewModelProvider(this).get(TrackDetailViewModel.class);
        long moduleId = getIntent().getLongExtra("moduleId", 1);
        String moduleTitle = getIntent().getStringExtra("moduleTitle");
        ImageView ivBack = findViewById(R.id.ivBack);
        TextView tvModuleTitle = findViewById(R.id.tvModuleTitle);
        TextView tvModuleDesc = findViewById(R.id.tvModuleDesc);
        TextView tvLessonCount = findViewById(R.id.tvLessonCount);
        TextView tvChallengeCount = findViewById(R.id.tvChallengeCount);
        TextView tvXpReward = findViewById(R.id.tvXpReward);
        ProgressBar progressModule = findViewById(R.id.progressModule);
        RecyclerView rvLessons = findViewById(R.id.rvLessons);
        RecyclerView rvChallenges = findViewById(R.id.rvChallenges);
        rvLessons.setLayoutManager(new LinearLayoutManager(this));
        rvChallenges.setLayoutManager(new LinearLayoutManager(this));
        ivBack.setOnClickListener(v -> finish());
        tvModuleTitle.setText(moduleTitle != null ? moduleTitle : "Module");
        viewModel.loadModule(moduleId);
        viewModel.getModule().observe(this, module -> {
            if (module != null) {
                tvModuleTitle.setText(module.getTitle());
                tvModuleDesc.setText(module.getDescription());
                tvLessonCount.setText(module.getLessonCount() + " lessons");
                tvChallengeCount.setText(module.getChallengeCount() + " challenges");
                tvXpReward.setText("+" + module.getXpReward() + " XP");
                progressModule.setProgress(module.getProgressPercent());
            }
        });
        viewModel.getLessons().observe(this, lessons -> {
            if (lessons != null) {
                rvLessons.setAdapter(new LessonAdapter(lessons, lesson -> {
                    Intent intent = new Intent(this, LessonActivity.class);
                    intent.putExtra("lessonId", lesson.getId());
                    intent.putExtra("lessonTitle", lesson.getTitle());
                    startActivity(intent);
                }));
            }
        });
        viewModel.getChallenges().observe(this, challenges -> {
            if (challenges != null && !challenges.isEmpty()) {
                com.codequest.ui.adapter.ChallengeAdapter challengeAdapter =
                        new com.codequest.ui.adapter.ChallengeAdapter(challenges, challenge -> {
                    Intent intent = new Intent(this, EditorActivity.class);
                    intent.putExtra("challengeId", challenge.getId());
                    startActivity(intent);
                });
                rvChallenges.setAdapter(challengeAdapter);
            }
        });
    }
}

