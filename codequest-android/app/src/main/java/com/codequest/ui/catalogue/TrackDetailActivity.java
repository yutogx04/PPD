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
import com.codequest.ui.adapter.ModuleAdapter;
import com.codequest.viewmodel.TrackDetailViewModel;
public class TrackDetailActivity extends BaseActivity {
    private TrackDetailViewModel viewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_detail);
        viewModel = new ViewModelProvider(this).get(TrackDetailViewModel.class);
        long trackId = getIntent().getLongExtra("trackId", 1);
        ImageView ivBack = findViewById(R.id.ivBack);
        TextView tvTrackTitle = findViewById(R.id.tvTrackTitle);
        TextView tvTrackDesc = findViewById(R.id.tvTrackDesc);
        TextView tvModuleCount = findViewById(R.id.tvModuleCount);
        TextView tvLessonCount = findViewById(R.id.tvLessonCount);
        TextView tvChallengeCount = findViewById(R.id.tvChallengeCount);
        ProgressBar progressTrack = findViewById(R.id.progressTrack);
        TextView tvProgressPercent = findViewById(R.id.tvProgressPercent);
        RecyclerView rvModules = findViewById(R.id.rvModules);
        rvModules.setLayoutManager(new LinearLayoutManager(this));
        ivBack.setOnClickListener(v -> finish());
        viewModel.loadTrack(trackId);
        viewModel.getTrack().observe(this, track -> {
            if (track != null) {
                tvTrackTitle.setText(track.getTitle());
                tvTrackDesc.setText(track.getDescription());
                tvModuleCount.setText(track.getModuleCount() + " modules");
                tvLessonCount.setText(track.getLessonCount() + " lessons");
                tvChallengeCount.setText(track.getChallengeCount() + " challenges");
                progressTrack.setProgress(track.getProgressPercent());
                tvProgressPercent.setText(track.getProgressPercent() + "% completed");
            }
        });
        viewModel.getModules().observe(this, modules -> {
            if (modules != null) {
                rvModules.setAdapter(new ModuleAdapter(modules, module -> {
                    Intent intent = new Intent(this, ModuleDetailActivity.class);
                    intent.putExtra("moduleId", module.getId());
                    intent.putExtra("moduleTitle", module.getTitle());
                    startActivity(intent);
                }));
            }
        });
    }
}

