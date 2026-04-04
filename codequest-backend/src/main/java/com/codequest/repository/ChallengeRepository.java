package com.codequest.repository;

import com.codequest.entity.Challenge;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ChallengeRepository extends JpaRepository<Challenge, Long> {

    List<Challenge> findByModuleId(Long moduleId);

    long countByModuleTrackId(Long trackId);
}
