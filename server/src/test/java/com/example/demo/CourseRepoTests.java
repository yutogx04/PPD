package com.example.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.demo.model.entities.Course;
import com.example.demo.repository.CourseRepository;

import jakarta.transaction.Transactional;

@Transactional
@SpringBootTest
public class CourseRepoTests {

  @Autowired
  private CourseRepository courseRepository;

  @Test
  public void testFindById() {
    Course course = new Course();
    courseRepository.save(course);
    Course result = courseRepository.findById(course.getId()).get();
    assertEquals(course.getId(), result.getId());
  }

  @Test
  public void testFindAll() {
    Course course = new Course();
    courseRepository.save(course);
    List<Course> result = new ArrayList<>();
    courseRepository.findAll().forEach(e -> result.add(e));
    assertEquals(result.size(), 1);
  }

  @Test
  public void testSave() {
    Course course = new Course();
    courseRepository.save(course);
    Course found = courseRepository.findById(course.getId()).get();
    assertEquals(course.getId(), found.getId());
  }

  @Test
  public void testDeleteById() {
    Course course = new Course();
    courseRepository.save(course);
    courseRepository.deleteById(course.getId());
    List<Course> result = new ArrayList<>();
    courseRepository.findAll().forEach(e -> result.add(e));
    assertEquals(result.size(), 0);
  }
}
