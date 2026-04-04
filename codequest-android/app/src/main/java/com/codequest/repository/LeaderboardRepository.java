package com.codequest.repository;
import android.content.Context;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.codequest.model.LeaderboardEntry;
import com.codequest.network.RetrofitClient;
import com.codequest.util.SharedPrefManager;
import java.util.Arrays;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class LeaderboardRepository {
    private final boolean useMockData = false;
    private String userPseudo = "Vous";

    public LeaderboardRepository() {}

    public LeaderboardRepository(Context context) {
        if (context != null) {
            SharedPrefManager prefs = SharedPrefManager.getInstance(context);
            String pseudo = prefs.getUserPseudo();
            if (pseudo != null && !pseudo.isEmpty()) {
                this.userPseudo = pseudo;
            }
        }
    }

    public LiveData<List<LeaderboardEntry>> getGlobalLeaderboard() {
        MutableLiveData<List<LeaderboardEntry>> data = new MutableLiveData<>();
        if (useMockData) {
            data.setValue(getMockLeaderboard());
            return data;
        }
        RetrofitClient.getApi().getGlobalLeaderboard().enqueue(new Callback<List<LeaderboardEntry>>() {
            @Override public void onResponse(Call<List<LeaderboardEntry>> c, Response<List<LeaderboardEntry>> r) { data.setValue(r.body()); }
            @Override public void onFailure(Call<List<LeaderboardEntry>> c, Throwable t) { data.setValue(null); }
        });
        return data;
    }
    public LiveData<List<LeaderboardEntry>> getWeeklyLeaderboard() {
        MutableLiveData<List<LeaderboardEntry>> data = new MutableLiveData<>();
        if (useMockData) {
            data.setValue(getMockLeaderboard());
            return data;
        }
        RetrofitClient.getApi().getWeeklyLeaderboard().enqueue(new Callback<List<LeaderboardEntry>>() {
            @Override public void onResponse(Call<List<LeaderboardEntry>> c, Response<List<LeaderboardEntry>> r) { data.setValue(r.body()); }
            @Override public void onFailure(Call<List<LeaderboardEntry>> c, Throwable t) { data.setValue(null); }
        });
        return data;
    }
    public LiveData<List<LeaderboardEntry>> getFriendsLeaderboard() {
        MutableLiveData<List<LeaderboardEntry>> data = new MutableLiveData<>();
        if (useMockData) {
            List<LeaderboardEntry> friends = Arrays.asList(
                    new LeaderboardEntry(1, "DevNinja", 8, 3200, false),
                    new LeaderboardEntry(2, "HackMaster", 7, 2150, false),
                    new LeaderboardEntry(3, userPseudo, 3, 840, true)
            );
            data.setValue(friends);
            return data;
        }
        RetrofitClient.getApi().getFriendsLeaderboard().enqueue(new Callback<List<LeaderboardEntry>>() {
            @Override public void onResponse(Call<List<LeaderboardEntry>> c, Response<List<LeaderboardEntry>> r) { data.setValue(r.body()); }
            @Override public void onFailure(Call<List<LeaderboardEntry>> c, Throwable t) { data.setValue(null); }
        });
        return data;
    }
    private List<LeaderboardEntry> getMockLeaderboard() {
        return Arrays.asList(
                new LeaderboardEntry(1, "DevNinja", 8, 3200, false),
                new LeaderboardEntry(2, "HackMaster", 7, 2150, false),
                new LeaderboardEntry(3, "PyMaster", 6, 1890, false),
                new LeaderboardEntry(4, "AlgoKing", 7, 1750, false),
                new LeaderboardEntry(5, "CodeRunner", 6, 1620, false),
                new LeaderboardEntry(6, "BugSlayer", 5, 1480, false),
                new LeaderboardEntry(23, userPseudo, 3, 840, true)
        );
    }
}
