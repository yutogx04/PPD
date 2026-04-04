package com.codequest.repository;

import com.codequest.entity.TestCase;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TestCaseRepository extends JpaRepository<TestCase, Long> {

    List<TestCase> findByChallengeId(Long challengeId);

    List<TestCase> findByChallengeIdAndHiddenFalse(Long challengeId);

    long countByChallengeId(Long challengeId);
}
