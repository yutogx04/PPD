package com.codequest.repository;

import com.codequest.entity.LessonSlide;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LessonSlideRepository extends JpaRepository<LessonSlide, Long> {

    List<LessonSlide> findByLessonIdOrderByOrderIndexAsc(Long lessonId);

    long countByLessonId(Long lessonId);
}
