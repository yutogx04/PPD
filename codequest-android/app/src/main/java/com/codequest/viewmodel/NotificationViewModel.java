package com.codequest.viewmodel;
import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.codequest.util.SharedPrefManager;
public class NotificationViewModel extends AndroidViewModel {
    private final SharedPrefManager prefManager;
    private final MutableLiveData<Boolean> dailyReminder = new MutableLiveData<>();
    private final MutableLiveData<Boolean> streakAlert = new MutableLiveData<>();
    private final MutableLiveData<Boolean> badgeUnlocked = new MutableLiveData<>();
    private final MutableLiveData<Boolean> friendRequest = new MutableLiveData<>();
    private final MutableLiveData<Boolean> dailyChallenge = new MutableLiveData<>();
    private final MutableLiveData<Boolean> mutualChallenge = new MutableLiveData<>();
    private final MutableLiveData<Integer> reminderHour = new MutableLiveData<>();
    public NotificationViewModel(@NonNull Application application) {
        super(application);
        prefManager = SharedPrefManager.getInstance(application);
        dailyReminder.setValue(prefManager.isNotifDaily());
        streakAlert.setValue(prefManager.isNotifStreak());
        badgeUnlocked.setValue(prefManager.isNotifBadge());
        friendRequest.setValue(prefManager.isNotifFriend());
        dailyChallenge.setValue(prefManager.isNotifDailyChallenge());
        mutualChallenge.setValue(prefManager.isNotifMutual());
        reminderHour.setValue(prefManager.getDailyReminderHour());
    }
    public LiveData<Boolean> getDailyReminder() { return dailyReminder; }
    public LiveData<Boolean> getStreakAlert() { return streakAlert; }
    public LiveData<Boolean> getBadgeUnlocked() { return badgeUnlocked; }
    public LiveData<Boolean> getFriendRequest() { return friendRequest; }
    public LiveData<Boolean> getDailyChallenge() { return dailyChallenge; }
    public LiveData<Boolean> getMutualChallenge() { return mutualChallenge; }
    public LiveData<Integer> getReminderHour() { return reminderHour; }
    public void setDailyReminder(boolean on) { prefManager.setNotifDaily(on); dailyReminder.setValue(on); }
    public void setStreakAlert(boolean on) { prefManager.setNotifStreak(on); streakAlert.setValue(on); }
    public void setBadgeUnlocked(boolean on) { prefManager.setNotifBadge(on); badgeUnlocked.setValue(on); }
    public void setFriendRequest(boolean on) { prefManager.setNotifFriend(on); friendRequest.setValue(on); }
    public void setDailyChallenge(boolean on) { prefManager.setNotifDailyChallenge(on); dailyChallenge.setValue(on); }
    public void setMutualChallenge(boolean on) { prefManager.setNotifMutual(on); mutualChallenge.setValue(on); }
    public void setReminderHour(int hour) { prefManager.setDailyReminderHour(hour); reminderHour.setValue(hour); }
}
