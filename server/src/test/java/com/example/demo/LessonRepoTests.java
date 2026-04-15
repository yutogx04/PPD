package com.example.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import com.example.demo.model.entities.Lesson;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.demo.model.entities.Lesson;
import com.example.demo.repository.LessonRepository;

import jakarta.transaction.Transactional;

@Transactional
@SpringBootTest
public class LessonRepoTests {

  @Autowired
  private LessonRepository lessonRepository;

  @Test
  public void testFindById() {
    Lesson lesson = new Lesson();
    lessonRepository.save(lesson);
    Lesson result = lessonRepository.findById(lesson.getId()).get();
    assertEquals(lesson.getId(), result.getId());
  }

  @Test
  public void testFindAll() {
    Lesson lesson = new Lesson();
    lessonRepository.save(lesson);
    List<Lesson> result = new ArrayList<>();
    lessonRepository.findAll().forEach(e -> result.add(e));
    assertEquals(result.size(), 1);
  }

  @Test
  public void testSave() {
    Lesson lesson = new Lesson();
    lessonRepository.save(lesson);
    Lesson found = lessonRepository.findById(lesson.getId()).get();
    assertEquals(lesson.getId(), found.getId());
  }

  @Test
  public void testDeleteById() {
    Lesson lesson = new Lesson();
    lessonRepository.save(lesson);
    lessonRepository.deleteById(lesson.getId());
    List<Lesson> result = new ArrayList<>();
    lessonRepository.findAll().forEach(e -> result.add(e));
    assertEquals(result.size(), 0);
  }
}
