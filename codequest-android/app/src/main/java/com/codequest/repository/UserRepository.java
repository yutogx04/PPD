package com.codequest.repository;
import android.content.Context;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.codequest.model.Badge;
import com.codequest.model.User;
import com.codequest.model.UserStats;
import com.codequest.network.RetrofitClient;
import com.codequest.util.SharedPrefManager;
import java.util.Arrays;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class UserRepository {
    private final boolean useMockData = false;
    private SharedPrefManager prefManager;
    public UserRepository() {
    }
    public UserRepository(Context context) {
        this.prefManager = SharedPrefManager.getInstance(context);
    }
    public LiveData<User> getProfile() {
        MutableLiveData<User> data = new MutableLiveData<>();
        if (useMockData) {
            
            String pseudo = "Utilisateur";
            int xp = 0;
            int level = 1;
            int streak = 0;
            long userId = 1;
            if (prefManager != null) {
                pseudo = prefManager.getUserPseudo();
                if (pseudo == null || pseudo.isEmpty()) pseudo = "Utilisateur";
                xp = prefManager.getUserXp();
                level = prefManager.getUserLevel();
                streak = prefManager.getUserStreak();
                userId = prefManager.getUserId();
            }
            User user = new User(userId, pseudo, xp, level, streak);
            user.setEmail(pseudo.toLowerCase() + "@email.com");
            user.setBio("Apprenti développeur passionné");
            user.setTotalLessonsCompleted(0);
            user.setTotalChallengesSolved(0);
            data.setValue(user);
            return data;
        }
        RetrofitClient.getApi().getProfile().enqueue(new Callback<User>() {
            @Override public void onResponse(Call<User> call, Response<User> r) {
                User user = r.body();
                if (user != null && prefManager != null) {
                    prefManager.saveUserInfo(user.getId(), user.getPseudo(),
                            user.getXp(), user.getLevel(), user.getStreak());
                }
                data.setValue(user);
            }
            @Override public void onFailure(Call<User> call, Throwable t) { data.setValue(null); }
        });
        return data;
    }
    public LiveData<List<Badge>> getBadges() {
        MutableLiveData<List<Badge>> data = new MutableLiveData<>();
        if (useMockData) {
            List<Badge> badges = Arrays.asList(
                    new Badge(1, "Premier Pas", "Compléter sa 1ère leçon", "user_plus", false),
                    new Badge(2, "Coder", "Résoudre son 1er défi", "code", false),
                    new Badge(3, "Assidu", "Maintenir un streak de 7 jours", "fire", false),
                    new Badge(4, "Perfectionniste", "Réussir un défi du 1er essai", "bullseye", false),
                    new Badge(5, "Endurant", "Maintenir un streak de 30 jours", "heart_pulse", false),
                    new Badge(6, "Défi Relevé", "Résoudre 10 défis", "sword", false),
                    new Badge(7, "Explorateur", "Commencer 2 parcours différents", "compass", false),
                    new Badge(8, "Nuit Code", "Soumettre une solution après minuit", "moon", false),
                    new Badge(9, "Maître Python", "Terminer le parcours Python", "snake", false),
                    new Badge(10, "Élite", "Atteindre le niveau 6", "trophy", false)
            );
            data.setValue(badges);
            return data;
        }
        RetrofitClient.getApi().getMyBadges().enqueue(new Callback<List<Badge>>() {
            @Override public void onResponse(Call<List<Badge>> call, Response<List<Badge>> r) { data.setValue(r.body()); }
            @Override public void onFailure(Call<List<Badge>> call, Throwable t) { data.setValue(null); }
        });
        return data;
    }
    public LiveData<List<User>> searchUsers(String query) {
        MutableLiveData<List<User>> data = new MutableLiveData<>();
        if (useMockData) {
            List<User> users = Arrays.asList(
                    new User(2, "DevNinja", 3200, 5, 25),
                    new User(3, "PyMaster", 1890, 4, 8)
            );
            data.setValue(users);
            return data;
        }
        RetrofitClient.getApi().searchUsers(query).enqueue(new Callback<List<User>>() {
            @Override public void onResponse(Call<List<User>> call, Response<List<User>> r) { data.setValue(r.body()); }
            @Override public void onFailure(Call<List<User>> call, Throwable t) { data.setValue(null); }
        });
        return data;
    }

    public LiveData<UserStats> getUserStats() {
        MutableLiveData<UserStats> data = new MutableLiveData<>();
        if (useMockData) {
            data.setValue(new UserStats());
            return data;
        }
        RetrofitClient.getApi().getDetailedStats().enqueue(new Callback<UserStats>() {
            @Override public void onResponse(Call<UserStats> call, Response<UserStats> r) { data.setValue(r.body()); }
            @Override public void onFailure(Call<UserStats> call, Throwable t) { data.setValue(null); }
        });
        return data;
    }
}
