package com.example.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.demo.model.Challenge;
import com.example.demo.repository.ChallengeRepository;

import jakarta.transaction.Transactional;

@Transactional
@SpringBootTest
public class ChallengeRepoTests {

  @Autowired
  private ChallengeRepository challengeRepository;

  @Test
  public void testFindById() {
    Challenge challenge = new Challenge();
    challengeRepository.save(challenge);
    Challenge result = challengeRepository.findById(challenge.getId()).get();
    assertEquals(challenge.getId(), result.getId());
  }

  @Test
  public void testFindAll() {
    Challenge challenge = new Challenge();
    challengeRepository.save(challenge);
    List<Challenge> result = new ArrayList<>();
    challengeRepository.findAll().forEach(e -> result.add(e));
    assertEquals(result.size(), 1);
  }

  @Test
  public void testSave() {
    Challenge challenge = new Challenge();
    challengeRepository.save(challenge);
    Challenge found = challengeRepository.findById(challenge.getId()).get();
    assertEquals(challenge.getId(), found.getId());
  }

  @Test
  public void testDeleteById() {
    Challenge challenge = new Challenge();
    challengeRepository.save(challenge);
    challengeRepository.deleteById(challenge.getId());
    List<Challenge> result = new ArrayList<>();
    challengeRepository.findAll().forEach(e -> result.add(e));
    assertEquals(result.size(), 0);
  }
}
