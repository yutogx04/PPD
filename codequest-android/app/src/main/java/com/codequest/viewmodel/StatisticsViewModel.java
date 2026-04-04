package com.codequest.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.codequest.model.UserStats;
import com.codequest.repository.UserRepository;

public class StatisticsViewModel extends AndroidViewModel {
    private final UserRepository userRepository;
    private LiveData<UserStats> statsLiveData;

    public StatisticsViewModel(@NonNull Application application) {
        super(application);
        userRepository = new UserRepository(application);
    }

    public LiveData<UserStats> getUserStats() {
        if (statsLiveData == null) {
            statsLiveData = userRepository.getUserStats();
        }
        return statsLiveData;
    }

    public void refreshStats() {
        statsLiveData = userRepository.getUserStats();
    }
}
