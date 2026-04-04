package com.codequest.repository;

import com.codequest.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LessonRepository extends JpaRepository<Lesson, Long> {

    List<Lesson> findByModuleIdOrderByOrderIndexAsc(Long moduleId);

    long countByModuleTrackId(Long trackId);

    long countByModuleId(Long moduleId);
}
