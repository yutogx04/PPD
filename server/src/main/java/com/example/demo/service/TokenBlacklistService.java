package com.example.demo.service;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * Service de blacklist de tokens JWT via Redis.
 * [POURQUOI] Redis est utilisé car les tokens blacklistés doivent être
 * partagés entre toutes les instances du serveur (scalabilité horizontale),
 * et le TTL automatique de Redis nettoie les tokens expirés sans cron job.
 */
@Service
public class TokenBlacklistService {

    private static final String BLACKLIST_PREFIX = "jwt:blacklist:";

    private final StringRedisTemplate redisTemplate;

    public TokenBlacklistService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * Ajoute un token à la blacklist avec un TTL correspondant
     * au temps restant avant expiration du token.
     *
     * @param token          Le JWT à blacklister
     * @param remainingMs    Millisecondes restantes avant expiration
     */
    public void blacklistToken(String token, long remainingMs) {
        if (remainingMs <= 0) {
            // [POURQUOI] Pas besoin de blacklister un token déjà expiré
            return;
        }
        String key = BLACKLIST_PREFIX + token;
        redisTemplate.opsForValue().set(key, "blacklisted", remainingMs, TimeUnit.MILLISECONDS);
    }

    /**
     * Vérifie si un token est dans la blacklist.
     */
    public boolean isBlacklisted(String token) {
        String key = BLACKLIST_PREFIX + token;
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }
}
