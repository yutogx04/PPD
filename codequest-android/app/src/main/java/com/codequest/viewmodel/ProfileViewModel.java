package com.codequest.viewmodel;
import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.codequest.model.Badge;
import com.codequest.model.User;
import com.codequest.repository.AuthRepository;
import com.codequest.repository.UserRepository;
import java.util.List;
public class ProfileViewModel extends AndroidViewModel {
    private final UserRepository userRepository;
    private final AuthRepository authRepository;
    private LiveData<User> profile;
    private LiveData<List<Badge>> badges;
    public ProfileViewModel(@NonNull Application application) {
        super(application);
        userRepository = new UserRepository(application);
        authRepository = new AuthRepository(application);
    }
    public LiveData<User> getProfile() {
        if (profile == null) {
            profile = userRepository.getProfile();
        }
        return profile;
    }
    public LiveData<List<Badge>> getBadges() {
        if (badges == null) {
            badges = userRepository.getBadges();
        }
        return badges;
    }
    public int getEarnedBadgeCount(List<Badge> allBadges) {
        if (allBadges == null) return 0;
        int count = 0;
        for (Badge b : allBadges) {
            if (b.isEarned()) count++;
        }
        return count;
    }
    public void logout() {
        authRepository.logout();
    }
    public void refresh() {
        profile = userRepository.getProfile();
        badges = userRepository.getBadges();
    }
}
