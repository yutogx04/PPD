package com.codequest.ui.profile;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.codequest.R;
import com.codequest.ui.adapter.BadgeAdapter;
import com.codequest.ui.auth.LoginActivity;
import com.codequest.ui.main.MainActivity;
import com.codequest.ui.settings.NotificationSettingsActivity;
import com.codequest.util.SharedPrefManager;
import com.codequest.viewmodel.ProfileViewModel;
public class ProfileFragment extends Fragment {
    private ProfileViewModel viewModel;
    private TextView tvPseudo, tvLevel, tvStatXp, tvStatStreak, tvStatLessons, tvStatChallenges;
    private TextView tvLevelBadge, tvXpProgress, tvNextLevel;
    private ProgressBar profileXpBar;
    private RecyclerView rvBadges;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        initViews(view);
        setupMenu(view);
        observeData();
    }
    private void initViews(View view) {
        tvPseudo = view.findViewById(R.id.tvUsername);
        tvLevel = view.findViewById(R.id.tvLevelText);
        tvLevelBadge = view.findViewById(R.id.tvLevelBadge);
        tvStatXp = view.findViewById(R.id.tvProfileXp);
        tvStatStreak = view.findViewById(R.id.tvProfileStreak);
        tvStatLessons = view.findViewById(R.id.tvProfileLessons);
        tvStatChallenges = view.findViewById(R.id.tvProfileChallenges);
        tvXpProgress = view.findViewById(R.id.tvProfileXpProgress);
        tvNextLevel = view.findViewById(R.id.tvNextLevel);
        profileXpBar = view.findViewById(R.id.profileXpBar);
        rvBadges = view.findViewById(R.id.rvBadges);
        rvBadges.setLayoutManager(new GridLayoutManager(getContext(), 4));
    }
    private void setupMenu(View view) {
        
        view.findViewById(R.id.menuStats).setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), StatisticsActivity.class);
            startActivity(intent);
        });
        
        view.findViewById(R.id.menuCerts).setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), CertificatesActivity.class);
            startActivity(intent);
        });
        
        view.findViewById(R.id.menuNotifications).setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), NotificationSettingsActivity.class);
            startActivity(intent);
        });
        
        Switch switchDarkMode = view.findViewById(R.id.switchDarkMode);
        SharedPrefManager prefs = SharedPrefManager.getInstance(requireContext());
        switchDarkMode.setChecked(prefs.isDarkMode());
        switchDarkMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            prefs.setDarkMode(isChecked);
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        });
        
        TextView tvCurrentLang = view.findViewById(R.id.tvCurrentLang);
        String currentLang = prefs.getLanguage();
        tvCurrentLang.setText("fr".equals(currentLang) ? "Français" : "English");
        view.findViewById(R.id.menuLanguage).setOnClickListener(v -> {
            String[] languages = {"Français", "English"};
            String[] codes = {"fr", "en"};
            int currentIndex = "fr".equals(prefs.getLanguage()) ? 0 : 1;
            new AlertDialog.Builder(requireContext())
                .setTitle(R.string.language_dialog_title)
                .setSingleChoiceItems(languages, currentIndex, (dialog, which) -> {
                    String selectedLang = codes[which];
                    if (!selectedLang.equals(prefs.getLanguage())) {
                        prefs.setLanguage(selectedLang);
                        dialog.dismiss();
                        
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    } else {
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(R.string.btn_cancel, null)
                .show();
        });
        
        view.findViewById(R.id.btnLogout).setOnClickListener(v -> {
            viewModel.logout();
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }
    private void observeData() {
        viewModel.getProfile().observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                tvPseudo.setText(user.getPseudo());
                tvLevel.setText(getString(R.string.profile_level_format, user.getLevel(), user.getLevelTitle()));
                
                if (tvLevelBadge != null) {
                    tvLevelBadge.setText(String.valueOf(user.getLevel()));
                }
                tvStatXp.setText(String.valueOf(user.getXp()));
                tvStatStreak.setText(String.valueOf(user.getStreak()));
                tvStatLessons.setText(String.valueOf(user.getTotalLessonsCompleted()));
                tvStatChallenges.setText(String.valueOf(user.getTotalChallengesSolved()));
                
                int xpForNextLevel = (user.getLevel() + 1) * 200;
                int progressPercent = (xpForNextLevel > 0) ? (user.getXp() * 100 / xpForNextLevel) : 0;
                if (progressPercent > 100) progressPercent = 100;
                if (tvXpProgress != null) {
                    tvXpProgress.setText(String.format("%d / %d", user.getXp(), xpForNextLevel));
                }
                if (tvNextLevel != null) {
                    tvNextLevel.setText(getString(R.string.profile_next_level, user.getLevel() + 1));
                }
                if (profileXpBar != null) {
                    profileXpBar.setProgress(progressPercent);
                }
            }
        });
        viewModel.getBadges().observe(getViewLifecycleOwner(), badges -> {
            if (badges != null) {
                rvBadges.setAdapter(new BadgeAdapter(badges));
            }
        });
    }
}
