package com.codequest.viewmodel;
import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.codequest.model.LeaderboardEntry;
import com.codequest.repository.LeaderboardRepository;
import java.util.List;
public class LeaderboardViewModel extends AndroidViewModel {
    private final LeaderboardRepository leaderboardRepository;
    private LiveData<List<LeaderboardEntry>> globalBoard;
    private LiveData<List<LeaderboardEntry>> weeklyBoard;
    private LiveData<List<LeaderboardEntry>> friendsBoard;
    private final MutableLiveData<Integer> activeTab = new MutableLiveData<>(0);

    public LeaderboardViewModel(@NonNull Application application) {
        super(application);
        leaderboardRepository = new LeaderboardRepository(application.getApplicationContext());
        globalBoard = leaderboardRepository.getGlobalLeaderboard();
        weeklyBoard = leaderboardRepository.getWeeklyLeaderboard();
        friendsBoard = leaderboardRepository.getFriendsLeaderboard();
    }

    public LiveData<List<LeaderboardEntry>> getGlobalBoard() { return globalBoard; }
    public LiveData<List<LeaderboardEntry>> getWeeklyBoard() { return weeklyBoard; }
    public LiveData<List<LeaderboardEntry>> getFriendsBoard() { return friendsBoard; }
    public LiveData<Integer> getActiveTab() { return activeTab; }
    public void setActiveTab(int tab) { activeTab.setValue(tab); }
}