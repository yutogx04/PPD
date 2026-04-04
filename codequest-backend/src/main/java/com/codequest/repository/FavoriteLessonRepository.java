package com.codequest.repository;

import com.codequest.entity.FavoriteLesson;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface FavoriteLessonRepository extends JpaRepository<FavoriteLesson, Long> {

    List<FavoriteLesson> findByUserId(Long userId);

    Optional<FavoriteLesson> findByUserIdAndLessonId(Long userId, Long lessonId);

    boolean existsByUserIdAndLessonId(Long userId, Long lessonId);
}
