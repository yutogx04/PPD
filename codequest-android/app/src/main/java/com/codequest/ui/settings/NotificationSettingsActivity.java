package com.codequest.ui.settings;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Switch;
import androidx.appcompat.app.AppCompatActivity;
import com.codequest.ui.base.BaseActivity;
import androidx.lifecycle.ViewModelProvider;
import com.codequest.R;
import com.codequest.viewmodel.NotificationViewModel;
public class NotificationSettingsActivity extends BaseActivity {
    private NotificationViewModel viewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_settings);
        viewModel = new ViewModelProvider(this).get(NotificationViewModel.class);
        ImageView ivBack = findViewById(R.id.ivBack);
        Switch switchDaily = findViewById(R.id.switchDaily);
        Switch switchStreak = findViewById(R.id.switchStreak);
        Switch switchBadge = findViewById(R.id.switchBadge);
        Switch switchFriend = findViewById(R.id.switchFriend);
        Switch switchDailyChallenge = findViewById(R.id.switchDailyChallenge);
        Switch switchMutual = findViewById(R.id.switchMutual);
        ivBack.setOnClickListener(v -> finish());
        viewModel.getDailyReminder().observe(this, switchDaily::setChecked);
        viewModel.getStreakAlert().observe(this, switchStreak::setChecked);
        viewModel.getBadgeUnlocked().observe(this, switchBadge::setChecked);
        viewModel.getFriendRequest().observe(this, switchFriend::setChecked);
        viewModel.getDailyChallenge().observe(this, switchDailyChallenge::setChecked);
        viewModel.getMutualChallenge().observe(this, switchMutual::setChecked);
        switchDaily.setOnCheckedChangeListener((b, on) -> viewModel.setDailyReminder(on));
        switchStreak.setOnCheckedChangeListener((b, on) -> viewModel.setStreakAlert(on));
        switchBadge.setOnCheckedChangeListener((b, on) -> viewModel.setBadgeUnlocked(on));
        switchFriend.setOnCheckedChangeListener((b, on) -> viewModel.setFriendRequest(on));
        switchDailyChallenge.setOnCheckedChangeListener((b, on) -> viewModel.setDailyChallenge(on));
        switchMutual.setOnCheckedChangeListener((b, on) -> viewModel.setMutualChallenge(on));
    }
}

