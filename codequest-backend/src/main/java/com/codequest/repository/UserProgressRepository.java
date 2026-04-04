package com.codequest.repository;

import com.codequest.entity.UserProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface UserProgressRepository extends JpaRepository<UserProgress, Long> {

    Optional<UserProgress> findByUserIdAndLessonId(Long userId, Long lessonId);

    List<UserProgress> findByUserIdAndCompletedTrue(Long userId);

    long countByUserIdAndCompletedTrueAndLessonModuleTrackId(Long userId, Long trackId);

    long countByUserIdAndCompletedTrueAndLessonModuleId(Long userId, Long moduleId);

    Optional<UserProgress> findTopByUserIdAndCompletedFalseOrderByLessonOrderIndexAsc(Long userId);
}
