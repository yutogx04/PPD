package com.codequest.repository;

import com.codequest.entity.FcmToken;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FcmTokenRepository extends JpaRepository<FcmToken, Long> {

    List<FcmToken> findByUserId(Long userId);

    void deleteByUserIdAndToken(Long userId, String token);
}
