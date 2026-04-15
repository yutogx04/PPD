package com.example.demo.repository;

import com.example.demo.model.entities.Challenge;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ChallengeRepository extends MongoRepository<Challenge, String> {
/*
    List<Challenge> findByDifficulty(String difficulty);

    List<Challenge> findByPublishedTrue();

    Optional<Challenge> findByIsDailyChallengeTrue_AndDailyDate(LocalDate date);

    List<Challenge> findByDifficultyAndPublishedTrue(String difficulty);

    boolean existsByTitleIgnoreCase(String title);

 */

}