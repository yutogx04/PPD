package com.codequest.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.codequest.model.Track;
import com.codequest.repository.TrackRepository;
import java.util.List;

public class CertificatesViewModel extends AndroidViewModel {
    private final TrackRepository trackRepository;
    private LiveData<List<Track>> tracksLiveData;

    public CertificatesViewModel(@NonNull Application application) {
        super(application);
        trackRepository = new TrackRepository();
    }

    public LiveData<List<Track>> getTracks() {
        if (tracksLiveData == null) {
            tracksLiveData = trackRepository.getTracks();
        }
        return tracksLiveData;
    }

    public void refreshTracks() {
        tracksLiveData = trackRepository.getTracks();
    }
}
