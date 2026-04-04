package com.codequest.viewmodel;
import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.codequest.model.LessonSlide;
import com.codequest.model.dto.GamificationResult;
import com.codequest.repository.LessonRepository;
import java.util.List;
public class LessonViewModel extends AndroidViewModel {
    private final LessonRepository lessonRepository;
    private LiveData<List<LessonSlide>> slides;
    private final MutableLiveData<Integer> currentSlideIndex = new MutableLiveData<>(0);
    private long lessonId;
    private int totalSlides = 0;
    public LessonViewModel(@NonNull Application application) {
        super(application);
        lessonRepository = new LessonRepository();
    }
    public void loadLesson(long lessonId, int startSlideIndex) {
        this.lessonId = lessonId;
        slides = lessonRepository.getLessonSlides(lessonId);
        currentSlideIndex.setValue(startSlideIndex);
    }
    public LiveData<List<LessonSlide>> getSlides() { return slides; }
    public LiveData<Integer> getCurrentSlideIndex() { return currentSlideIndex; }
    public void setTotalSlides(int total) { this.totalSlides = total; }
    public boolean nextSlide() {
        int current = currentSlideIndex.getValue() != null ? currentSlideIndex.getValue() : 0;
        if (current < totalSlides - 1) {
            currentSlideIndex.setValue(current + 1);
            return true;
        }
        return false;
    }
    public boolean previousSlide() {
        int current = currentSlideIndex.getValue() != null ? currentSlideIndex.getValue() : 0;
        if (current > 0) {
            currentSlideIndex.setValue(current - 1);
            return true;
        }
        return false;
    }
    public boolean isLastSlide() {
        int current = currentSlideIndex.getValue() != null ? currentSlideIndex.getValue() : 0;
        return current >= totalSlides - 1;
    }
    public boolean isFirstSlide() {
        int current = currentSlideIndex.getValue() != null ? currentSlideIndex.getValue() : 0;
        return current == 0;
    }
    public int getProgressPercent() {
        if (totalSlides <= 0) return 0;
        int current = currentSlideIndex.getValue() != null ? currentSlideIndex.getValue() : 0;
        return (int) (((float) (current + 1) / totalSlides) * 100);
    }
    public LiveData<GamificationResult> completeLesson() {
        return lessonRepository.completeLesson(lessonId);
    }
    public long getLessonId() { return lessonId; }
    public int getTotalSlides() { return totalSlides; }
}