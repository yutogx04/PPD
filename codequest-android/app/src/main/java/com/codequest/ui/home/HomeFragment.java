package com.codequest.ui.home;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.codequest.R;
import com.codequest.ui.adapter.TrackAdapter;
import com.codequest.ui.catalogue.TrackDetailActivity;
import com.codequest.ui.challenge.EditorActivity;
import com.codequest.ui.settings.NotificationSettingsActivity;
import com.codequest.viewmodel.HomeViewModel;
import com.codequest.model.DailyChallenge;
public class HomeFragment extends Fragment {
    private HomeViewModel viewModel;
    private TextView tvGreeting, tvSubGreeting, tvStreakCount, tvXpCount, tvLevelCount, tvDailyTitle;
    private TextView tvXpLevel, tvXpProgress;
    private ProgressBar xpProgressBar;
    private RecyclerView rvTracks;
    private SwipeRefreshLayout swipeRefresh;
    private DailyChallenge currentDailyChallenge;
    private LinearLayout cardContinue;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        initViews(view);
        setupUI(view);
        observeData();
    }
    private void initViews(View view) {
        tvGreeting = view.findViewById(R.id.tvGreeting);
        tvSubGreeting = view.findViewById(R.id.tvSubGreeting);
        tvStreakCount = view.findViewById(R.id.tvStreakCount);
        tvXpCount = view.findViewById(R.id.tvXpCount);
        tvLevelCount = view.findViewById(R.id.tvLevelCount);
        tvDailyTitle = view.findViewById(R.id.tvDailyTitle);
        tvXpLevel = view.findViewById(R.id.tvXpLevel);
        tvXpProgress = view.findViewById(R.id.tvXpProgress);
        xpProgressBar = view.findViewById(R.id.xpProgressBar);
        rvTracks = view.findViewById(R.id.rvTracks);
        swipeRefresh = view.findViewById(R.id.swipeRefresh);
        cardContinue = view.findViewById(R.id.cardContinue);
    }
    private void setupUI(View view) {
        
        String pseudo = viewModel.getUserPseudo();
        if (pseudo == null || pseudo.isEmpty()) pseudo = "Utilisateur";
        tvGreeting.setText(getString(R.string.home_greeting, pseudo));

        int streak = viewModel.getUserStreak();
        int xp = viewModel.getUserXp();
        int level = viewModel.getUserLevel();

        tvStreakCount.setText(String.valueOf(streak));
        tvXpCount.setText(String.valueOf(xp));
        tvLevelCount.setText(String.valueOf(level));

        updateXpDisplay(xp, level);

        if (cardContinue != null) {
            cardContinue.setVisibility(View.GONE);
        }

        rvTracks.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        View btnNotification = view.findViewById(R.id.btnNotification);
        if (btnNotification != null) {
            btnNotification.setOnClickListener(v -> {
                Intent intent = new Intent(getActivity(), NotificationSettingsActivity.class);
                startActivity(intent);
            });
        }

        Button btnDaily = view.findViewById(R.id.btnDaily);
        if (btnDaily != null) {
            btnDaily.setOnClickListener(v -> {
                if (currentDailyChallenge != null) {
                    Intent intent = new Intent(getActivity(), EditorActivity.class);
                    intent.putExtra("challengeId", currentDailyChallenge.getChallengeId());
                    startActivity(intent);
                } else {
                    android.widget.Toast.makeText(getContext(),
                            "Daily challenge is loading...", android.widget.Toast.LENGTH_SHORT).show();
                }
            });
        }

        View tvSeeAll = view.findViewById(R.id.tvSeeAll);
        if (tvSeeAll != null) {
            tvSeeAll.setOnClickListener(v -> {
                
                if (getActivity() != null) {
                    androidx.navigation.NavController navController =
                            androidx.navigation.Navigation.findNavController(getActivity(), R.id.navHostFragment);
                    navController.navigate(R.id.nav_catalogue);
                }
            });
        }

        swipeRefresh.setColorSchemeResources(R.color.primary);
        swipeRefresh.setOnRefreshListener(() -> {
            viewModel.refresh();
            observeData(); 
        });
    }
    private void updateXpDisplay(int xp, int level) {
        
        int xpForCurrentLevel = level * 200;
        int xpInCurrentLevel = xp % 200;
        int progressPercent = (xpForCurrentLevel > 0) ? (xp * 100 / xpForCurrentLevel) : 0;
        if (progressPercent > 100) progressPercent = 100;

        String levelTitle;
        switch (level) {
            case 1: levelTitle = getString(R.string.level_beginner); break;
            case 2: levelTitle = getString(R.string.level_novice); break;
            case 3: levelTitle = getString(R.string.level_appr); break;
            case 4: levelTitle = getString(R.string.level_dev); break;
            case 5: levelTitle = getString(R.string.level_expert); break;
            case 6: levelTitle = getString(R.string.level_master); break;
            default: levelTitle = getString(R.string.level_beginner); break;
        }

        if (tvXpLevel != null) {
            tvXpLevel.setText(getString(R.string.profile_level_format, level, levelTitle));
        }
        if (tvXpProgress != null) {
            tvXpProgress.setText(String.format("%d / %d XP", xp, xpForCurrentLevel));
        }
        xpProgressBar.setProgress(progressPercent);

        if (tvSubGreeting != null) {
            int lessonsToNext = Math.max(0, (xpForCurrentLevel - xp) / 20);
            if (lessonsToNext > 0) {
                tvSubGreeting.setText(getString(R.string.home_lessons_to_level, lessonsToNext, level + 1));
            } else {
                tvSubGreeting.setText(getString(R.string.home_keep_going));
            }
        }
    }
    private void observeData() {
        viewModel.getTracks().observe(getViewLifecycleOwner(), tracks -> {
            if (tracks != null) {
                TrackAdapter adapter = new TrackAdapter(tracks, track -> {
                    
                    Intent intent = new Intent(getContext(), TrackDetailActivity.class);
                    intent.putExtra("trackId", track.getId());
                    intent.putExtra("trackTitle", track.getTitle());
                    startActivity(intent);
                });
                rvTracks.setAdapter(adapter);
            }
        });
        viewModel.getDailyChallenge().observe(getViewLifecycleOwner(), daily -> {
            if (daily != null) {
                currentDailyChallenge = daily;
                tvDailyTitle.setText(daily.getTitle());
            }
        });

        viewModel.getUserProfile().observe(getViewLifecycleOwner(), user -> {
            if (swipeRefresh != null) swipeRefresh.setRefreshing(false);
            if (user != null) {
                String pseudo = user.getPseudo();
                if (pseudo == null || pseudo.isEmpty()) pseudo = "Utilisateur";
                tvGreeting.setText(getString(R.string.home_greeting, pseudo));
                tvStreakCount.setText(String.valueOf(user.getStreak()));
                tvXpCount.setText(String.valueOf(user.getXp()));
                tvLevelCount.setText(String.valueOf(user.getLevel()));
                updateXpDisplay(user.getXp(), user.getLevel());
            }
        });
    }
}
