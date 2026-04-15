package com.example.demo.repository;
import com.example.demo.model.entities.Lesson;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LessonRepository extends MongoRepository<Lesson, String> {

    // Queries by discriminator field — returns PracticeLesson or TheoryLesson polymorphically
    List<Lesson> findByLessonType(String lessonType);

    List<Lesson> findByPublishedTrueOrderByOrderIndexAsc();

    List<Lesson> findByLanguageAndPublishedTrue(String language);

    // Convenience methods
    default List<Lesson> findAllPracticeLessons() {
        return findByLessonType("PRACTICE");
    }

    default List<Lesson> findAllTheoryLessons() {
        return findByLessonType("THEORY");
    }
}