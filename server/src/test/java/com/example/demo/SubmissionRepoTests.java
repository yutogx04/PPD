package com.example.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.demo.model.Submission;
import com.example.demo.repository.SubmissionRepository;

import jakarta.transaction.Transactional;

@Transactional
@SpringBootTest
public class SubmissionRepoTests {

  @Autowired
  private SubmissionRepository submissionRepository;

  @Test
  public void testFindById() {
    Submission submission = new Submission();
    submissionRepository.save(submission);
    Submission result = submissionRepository.findById(submission.getId()).get();
    assertEquals(submission.getId(), result.getId());
  }

  @Test
  public void testFindAll() {
    Submission submission = new Submission();
    submissionRepository.save(submission);
    List<Submission> result = new ArrayList<>();
    submissionRepository.findAll().forEach(e -> result.add(e));
    assertEquals(result.size(), 1);
  }

  @Test
  public void testSave() {
    Submission submission = new Submission();
    submissionRepository.save(submission);
    Submission found = submissionRepository.findById(submission.getId()).get();
    assertEquals(submission.getId(), found.getId());
  }

  @Test
  public void testDeleteById() {
    Submission submission = new Submission();
    submissionRepository.save(submission);
    submissionRepository.deleteById(submission.getId());
    List<Submission> result = new ArrayList<>();
    submissionRepository.findAll().forEach(e -> result.add(e));
    assertEquals(result.size(), 0);
  }
}
