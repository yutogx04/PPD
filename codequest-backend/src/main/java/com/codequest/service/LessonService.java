package com.codequest.service;

import com.codequest.dto.GamificationResult;
import com.codequest.dto.LessonDto;
import com.codequest.dto.LessonSlideDto;
import com.codequest.entity.FavoriteLesson;
import com.codequest.entity.Lesson;
import com.codequest.entity.User;
import com.codequest.entity.UserProgress;
import com.codequest.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LessonService {

    private final LessonRepository lessonRepository;
    private final LessonSlideRepository lessonSlideRepository;
    private final UserProgressRepository userProgressRepository;
    private final FavoriteLessonRepository favoriteLessonRepository;
    private final UserRepository userRepository;
    private final GamificationService gamificationService;

    public LessonDto getLesson(Long lessonId, Long userId) {
        return getLesson(lessonId, userId, "fr");
    }

    public LessonDto getLesson(Long lessonId, Long userId, String lang) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new RuntimeException("Leçon non trouvée"));

        var progress = userProgressRepository.findByUserIdAndLessonId(userId, lessonId);
        boolean completed = progress.map(UserProgress::isCompleted).orElse(false);
        boolean favorite = favoriteLessonRepository.existsByUserIdAndLessonId(userId, lessonId);
        int savedSlideIndex = progress.map(UserProgress::getSlideIndex).orElse(0);

        return LessonDto.fromEntity(lesson, completed, favorite, savedSlideIndex, lang);
    }

    public List<LessonDto> getLessonsByModule(Long moduleId, Long userId) {
        return getLessonsByModule(moduleId, userId, "fr");
    }

    public List<LessonDto> getLessonsByModule(Long moduleId, Long userId, String lang) {
        return lessonRepository.findByModuleIdOrderByOrderIndexAsc(moduleId).stream()
                .map(lesson -> {
                    var progress = userProgressRepository.findByUserIdAndLessonId(userId, lesson.getId());
                    boolean completed = progress.map(UserProgress::isCompleted).orElse(false);
                    boolean favorite = favoriteLessonRepository.existsByUserIdAndLessonId(userId, lesson.getId());
                    int savedSlideIndex = progress.map(UserProgress::getSlideIndex).orElse(0);
                    return LessonDto.fromEntity(lesson, completed, favorite, savedSlideIndex, lang);
                })
                .collect(Collectors.toList());
    }

    public List<LessonSlideDto> getSlides(Long lessonId) {
        return getSlides(lessonId, "fr");
    }

    public List<LessonSlideDto> getSlides(Long lessonId, String lang) {
        return lessonSlideRepository.findByLessonIdOrderByOrderIndexAsc(lessonId).stream()
                .map(slide -> LessonSlideDto.fromEntity(slide, lang))
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateProgress(Long lessonId, Long userId, int slideIndex) {
        UserProgress progress = userProgressRepository.findByUserIdAndLessonId(userId, lessonId)
                .orElseGet(() -> {
                    User user = userRepository.findById(userId)
                            .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
                    Lesson lesson = lessonRepository.findById(lessonId)
                            .orElseThrow(() -> new RuntimeException("Leçon non trouvée"));
                    return UserProgress.builder()
                            .user(user)
                            .lesson(lesson)
                            .build();
                });
        progress.setSlideIndex(slideIndex);
        userProgressRepository.save(progress);
    }

    @Transactional
    public GamificationResult completeLesson(Long lessonId, Long userId) {
        UserProgress progress = userProgressRepository.findByUserIdAndLessonId(userId, lessonId)
                .orElseGet(() -> {
                    User user = userRepository.findById(userId)
                            .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
                    Lesson lesson = lessonRepository.findById(lessonId)
                            .orElseThrow(() -> new RuntimeException("Leçon non trouvée"));
                    return UserProgress.builder()
                            .user(user)
                            .lesson(lesson)
                            .build();
                });

        if (progress.isCompleted()) {
            throw new RuntimeException("Leçon déjà complétée");
        }

        progress.setCompleted(true);
        progress.setCompletedAt(LocalDateTime.now());
        userProgressRepository.save(progress);

        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new RuntimeException("Leçon non trouvée"));

        return gamificationService.onLessonCompleted(userId, lesson.getXpReward(), lesson);
    }

    @Transactional
    public void toggleFavorite(Long lessonId, Long userId) {
        var existing = favoriteLessonRepository.findByUserIdAndLessonId(userId, lessonId);
        if (existing.isPresent()) {
            favoriteLessonRepository.delete(existing.get());
        } else {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
            Lesson lesson = lessonRepository.findById(lessonId)
                    .orElseThrow(() -> new RuntimeException("Leçon non trouvée"));
            favoriteLessonRepository.save(FavoriteLesson.builder()
                    .user(user)
                    .lesson(lesson)
                    .build());
        }
    }

    public List<LessonDto> getFavorites(Long userId) {
        return getFavorites(userId, "fr");
    }

    public List<LessonDto> getFavorites(Long userId, String lang) {
        return favoriteLessonRepository.findByUserId(userId).stream()
                .map(fav -> {
                    Lesson lesson = fav.getLesson();
                    var progress = userProgressRepository.findByUserIdAndLessonId(userId, lesson.getId());
                    boolean completed = progress.map(UserProgress::isCompleted).orElse(false);
                    int savedSlideIndex = progress.map(UserProgress::getSlideIndex).orElse(0);
                    return LessonDto.fromEntity(lesson, completed, true, savedSlideIndex, lang);
                })
                .collect(Collectors.toList());
    }

    public LessonDto getContinueLesson(Long userId) {
        return getContinueLesson(userId, "fr");
    }

    public LessonDto getContinueLesson(Long userId, String lang) {
        return userProgressRepository.findTopByUserIdAndCompletedFalseOrderByLessonOrderIndexAsc(userId)
                .map(progress -> {
                    Lesson lesson = progress.getLesson();
                    boolean favorite = favoriteLessonRepository.existsByUserIdAndLessonId(userId, lesson.getId());
                    return LessonDto.fromEntity(lesson, false, favorite, progress.getSlideIndex(), lang);
                })
                .orElse(null);
    }
}
