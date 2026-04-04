package com.codequest.viewmodel;
import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.codequest.model.Challenge;
import com.codequest.model.Lesson;
import com.codequest.model.Module;
import com.codequest.model.Track;
import com.codequest.repository.ChallengeRepository;
import com.codequest.repository.LessonRepository;
import com.codequest.repository.TrackRepository;
import java.util.List;
public class TrackDetailViewModel extends AndroidViewModel {
    private final TrackRepository trackRepository;
    private final LessonRepository lessonRepository;
    private final ChallengeRepository challengeRepository;
    private LiveData<Track> track;
    private LiveData<List<Module>> modules;
    private LiveData<Module> module;
    private LiveData<List<Lesson>> lessons;
    private LiveData<List<Challenge>> challenges;
    public TrackDetailViewModel(@NonNull Application application) {
        super(application);
        trackRepository = new TrackRepository();
        lessonRepository = new LessonRepository();
        challengeRepository = new ChallengeRepository();
    }
    public void loadTrack(long trackId) {
        track = trackRepository.getTrack(trackId);
        modules = trackRepository.getModules(trackId);
    }
    public void loadModule(long moduleId) {
        lessons = lessonRepository.getLessons(moduleId);
        challenges = challengeRepository.getModuleChallenges(moduleId);
        
        androidx.lifecycle.MutableLiveData<Module> moduleData = new androidx.lifecycle.MutableLiveData<>();
        if (modules != null && modules.getValue() != null) {
            for (Module m : modules.getValue()) {
                if (m.getId() == moduleId) {
                    moduleData.setValue(m);
                    break;
                }
            }
        }
        module = moduleData;
    }
    public LiveData<Track> getTrack() { return track; }
    public LiveData<List<Module>> getModules() { return modules; }
    public LiveData<Module> getModule() { return module; }
    public LiveData<List<Lesson>> getLessons() { return lessons; }
    public LiveData<List<Challenge>> getChallenges() { return challenges; }
}
