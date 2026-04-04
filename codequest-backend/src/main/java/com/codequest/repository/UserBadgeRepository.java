package com.codequest.repository;

import com.codequest.entity.UserBadge;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface UserBadgeRepository extends JpaRepository<UserBadge, Long> {

    List<UserBadge> findByUserId(Long userId);

    boolean existsByUserIdAndBadgeId(Long userId, Long badgeId);

    long countByUserId(Long userId);

    long countByBadgeId(Long badgeId);
}
