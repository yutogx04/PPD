package com.codequest.viewmodel;
import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.codequest.model.DailyChallenge;
import com.codequest.model.Lesson;
import com.codequest.model.Track;
import com.codequest.model.User;
import com.codequest.repository.ChallengeRepository;
import com.codequest.repository.TrackRepository;
import com.codequest.repository.UserRepository;
import com.codequest.util.SharedPrefManager;
import java.util.List;
public class HomeViewModel extends AndroidViewModel {
    private final UserRepository userRepository;
    private final TrackRepository trackRepository;
    private final ChallengeRepository challengeRepository;
    private final SharedPrefManager prefManager;
    private LiveData<User> userProfile;
    private LiveData<List<Track>> tracks;
    private LiveData<DailyChallenge> dailyChallenge;
    public HomeViewModel(@NonNull Application application) {
        super(application);
        userRepository = new UserRepository(application);
        trackRepository = new TrackRepository();
        challengeRepository = new ChallengeRepository();
        prefManager = SharedPrefManager.getInstance(application);
    }
    public LiveData<User> getUserProfile() {
        if (userProfile == null) {
            userProfile = userRepository.getProfile();
        }
        return userProfile;
    }
    public LiveData<List<Track>> getTracks() {
        if (tracks == null) {
            tracks = trackRepository.getTracks();
        }
        return tracks;
    }
    public LiveData<DailyChallenge> getDailyChallenge() {
        if (dailyChallenge == null) {
            dailyChallenge = challengeRepository.getDailyChallenge();
        }
        return dailyChallenge;
    }
    public String getUserPseudo() {
        return prefManager.getUserPseudo();
    }
    public int getUserStreak() {
        return prefManager.getUserStreak();
    }
    public int getUserXp() {
        return prefManager.getUserXp();
    }
    public int getUserLevel() {
        return prefManager.getUserLevel();
    }
    public void refresh() {
        userProfile = userRepository.getProfile();
        tracks = trackRepository.getTracks();
        dailyChallenge = challengeRepository.getDailyChallenge();
    }
}
