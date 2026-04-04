package com.codequest.viewmodel;
import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.codequest.model.Track;
import com.codequest.repository.TrackRepository;
import com.codequest.util.SharedPrefManager;
import java.util.ArrayList;
import java.util.List;
public class CatalogueViewModel extends AndroidViewModel {
    private final TrackRepository trackRepository;
    private final SharedPrefManager prefManager;
    private LiveData<List<Track>> allTracks;
    private final MutableLiveData<List<Track>> filteredTracks = new MutableLiveData<>();
    private final MutableLiveData<String> activeFilter = new MutableLiveData<>("ALL");
    private String searchQuery = "";
    public CatalogueViewModel(@NonNull Application application) {
        super(application);
        trackRepository = new TrackRepository();
        prefManager = SharedPrefManager.getInstance(application);
    }
    public LiveData<List<Track>> getAllTracks() {
        if (allTracks == null) {
            allTracks = trackRepository.getTracks();
        }
        return allTracks;
    }
    public LiveData<List<Track>> getFilteredTracks() {
        return filteredTracks;
    }
    public LiveData<String> getActiveFilter() {
        return activeFilter;
    }
    public void setFilter(String filter) {
        activeFilter.setValue(filter);
        applyFilters();
    }
    public void setSearchQuery(String query) {
        this.searchQuery = query != null ? query.toLowerCase() : "";
        applyFilters();
    }
    private void applyFilters() {
        List<Track> all = allTracks != null ? allTracks.getValue() : null;
        if (all == null) return;
        List<Track> result = new ArrayList<>();
        String filter = activeFilter.getValue();
        for (Track track : all) {
            boolean matchesFilter = "ALL".equals(filter) || track.getDifficulty().equals(filter);
            boolean matchesSearch = searchQuery.isEmpty() ||
                    track.getTitle().toLowerCase().contains(searchQuery) ||
                    track.getDescription().toLowerCase().contains(searchQuery);
            if (matchesFilter && matchesSearch) {
                result.add(track);
            }
        }
        filteredTracks.setValue(result);
    }
    public boolean isTrackLocked(Track track) {
        if (!track.isLocked()) return false;
        return prefManager.getUserLevel() < track.getRequiredLevel();
    }
}
