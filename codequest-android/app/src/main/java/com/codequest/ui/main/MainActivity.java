package com.codequest.ui.main;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.codequest.ui.base.BaseActivity;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import com.codequest.R;
import com.codequest.ui.challenge.EditorActivity;
import com.codequest.util.LocaleHelper;

public class MainActivity extends BaseActivity {

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.setLocale(newBase, LocaleHelper.getLanguage(newBase)));
    }

    private NavController navController;

    private LinearLayout navHome, navCatalogue, navLeaderboard, navProfile;
    private ImageView navHomeIcon, navCatalogueIcon, navLeaderboardIcon, navProfileIcon;
    private TextView navHomeLabel, navCatalogueLabel, navLeaderboardLabel, navProfileLabel;
    private ImageView navCenterFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NavHostFragment navHostFragment = (NavHostFragment)
                getSupportFragmentManager().findFragmentById(R.id.navHostFragment);
        if (navHostFragment != null) {
            navController = navHostFragment.getNavController();
        }

        navHome = findViewById(R.id.navHome);
        navCatalogue = findViewById(R.id.navCatalogue);
        navLeaderboard = findViewById(R.id.navLeaderboard);
        navProfile = findViewById(R.id.navProfile);
        navCenterFab = findViewById(R.id.navCenterFab);

        navHomeIcon = findViewById(R.id.navHomeIcon);
        navCatalogueIcon = findViewById(R.id.navCatalogueIcon);
        navLeaderboardIcon = findViewById(R.id.navLeaderboardIcon);
        navProfileIcon = findViewById(R.id.navProfileIcon);

        navHomeLabel = findViewById(R.id.navHomeLabel);
        navCatalogueLabel = findViewById(R.id.navCatalogueLabel);
        navLeaderboardLabel = findViewById(R.id.navLeaderboardLabel);
        navProfileLabel = findViewById(R.id.navProfileLabel);

        navHome.setOnClickListener(v -> navigateTo(R.id.nav_home));
        navCatalogue.setOnClickListener(v -> navigateTo(R.id.nav_catalogue));
        navLeaderboard.setOnClickListener(v -> navigateTo(R.id.nav_leaderboard));
        navProfile.setOnClickListener(v -> navigateTo(R.id.nav_profile));

        navCenterFab.setOnClickListener(v -> {
            Intent intent = new Intent(this, EditorActivity.class);
            startActivity(intent);
        });

        updateNavState(R.id.nav_home);
    }

    private void navigateTo(int destinationId) {
        if (navController != null && navController.getCurrentDestination() != null
                && navController.getCurrentDestination().getId() != destinationId) {
            navController.navigate(destinationId);
        }
        updateNavState(destinationId);
    }

    private void updateNavState(int activeId) {
        int active = ContextCompat.getColor(this, R.color.nav_active);
        int inactive = ContextCompat.getColor(this, R.color.nav_inactive);

        navHomeIcon.setColorFilter(inactive);
        navCatalogueIcon.setColorFilter(inactive);
        navLeaderboardIcon.setColorFilter(inactive);
        navProfileIcon.setColorFilter(inactive);

        navHomeLabel.setTextColor(inactive);
        navCatalogueLabel.setTextColor(inactive);
        navLeaderboardLabel.setTextColor(inactive);
        navProfileLabel.setTextColor(inactive);

        if (activeId == R.id.nav_home) {
            navHomeIcon.setColorFilter(active);
            navHomeLabel.setTextColor(active);
        } else if (activeId == R.id.nav_catalogue) {
            navCatalogueIcon.setColorFilter(active);
            navCatalogueLabel.setTextColor(active);
        } else if (activeId == R.id.nav_leaderboard) {
            navLeaderboardIcon.setColorFilter(active);
            navLeaderboardLabel.setTextColor(active);
        } else if (activeId == R.id.nav_profile) {
            navProfileIcon.setColorFilter(active);
            navProfileLabel.setTextColor(active);
        }
    }
}

