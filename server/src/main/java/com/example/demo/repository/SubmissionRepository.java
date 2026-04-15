package com.example.demo.repository;

import com.example.demo.model.entities.Submission;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubmissionRepository extends MongoRepository<Submission, String> {

    List<Submission> findByUserId(String userId);

    List<Submission> findByChallengeId(String challengeId);

    List<Submission> findByUserIdAndChallengeId(String userId, String challengeId);

    List<Submission> findByUserIdOrderBySubmittedAtDesc(String userId);

    Optional<Submission> findTopByUserIdAndChallengeIdOrderByScoreDesc(String userId, String challengeId);

    long countByUserIdAndStatus(String userId, String status);
}
