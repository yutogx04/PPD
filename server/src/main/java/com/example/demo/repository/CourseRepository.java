package com.example.demo.repository;

import com.example.demo.model.entities.Course;
import org.springframework.data.repository.CrudRepository;

public interface CourseRepository extends CrudRepository<Course , Long> {
}
