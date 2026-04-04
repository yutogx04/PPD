package com.codequest.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class OtpService {

    private final StringRedisTemplate redisTemplate;
    private static final String OTP_PREFIX = "otp:";
    private static final long OTP_TTL_MINUTES = 5;

    public String generateOtp(String email) {
        String code = String.format("%06d", new SecureRandom().nextInt(999999));
        redisTemplate.opsForValue().set(
                OTP_PREFIX + email,
                code,
                OTP_TTL_MINUTES,
                TimeUnit.MINUTES
        );
        return code;
    }

    public boolean verifyOtp(String email, String code) {
        String stored = redisTemplate.opsForValue().get(OTP_PREFIX + email);
        if (stored != null && stored.equals(code)) {
            redisTemplate.delete(OTP_PREFIX + email);
            return true;
        }
        return false;
    }
}
