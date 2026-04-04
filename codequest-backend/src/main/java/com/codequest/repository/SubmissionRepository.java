package com.codequest.repository;

import com.codequest.entity.Submission;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface SubmissionRepository extends JpaRepository<Submission, Long> {

    List<Submission> findByUserIdAndChallengeId(Long userId, Long challengeId);

    long countByUserIdAndChallengeId(Long userId, Long challengeId);

    Optional<Submission> findTopByUserIdAndChallengeIdAndStatusOrderByScoreDesc(
            Long userId, Long challengeId, Submission.Status status);

    Optional<Submission> findTopByUserIdAndChallengeIdOrderByCreatedAtDesc(
            Long userId, Long challengeId);

    boolean existsByUserIdAndChallengeIdAndCreatedAtAfter(
            Long userId, Long challengeId, LocalDateTime after);

    long countByUserIdAndStatus(Long userId, Submission.Status status);

    long countByStatus(Submission.Status status);

    long countByChallengeId(Long challengeId);

    long countByChallengeIdAndStatus(Long challengeId, Submission.Status status);

    List<Submission> findTop20ByOrderByCreatedAtDesc();
}
