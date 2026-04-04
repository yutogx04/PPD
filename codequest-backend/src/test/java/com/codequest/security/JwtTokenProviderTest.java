package com.codequest.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class JwtTokenProviderTest {

    private JwtTokenProvider provider;

    @BeforeEach
    void setUp() {
        
        String secret = "test-secret-key-that-is-at-least-256-bits-long-for-hmac-sha256";
        long accessExp = 3600000;   
        long refreshExp = 604800000; 
        provider = new JwtTokenProvider(secret, accessExp, refreshExp);
    }

    @Test
    void generateAccessToken_returnsNonNullString() {
        String token = provider.generateAccessToken(1L, "test@email.com");
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void generateRefreshToken_returnsNonNullString() {
        String token = provider.generateRefreshToken(1L, "test@email.com");
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void getUserIdFromToken_returnsCorrectId() {
        String token = provider.generateAccessToken(42L, "user@test.com");
        Long userId = provider.getUserIdFromToken(token);
        assertEquals(42L, userId);
    }

    @Test
    void getEmailFromToken_returnsCorrectEmail() {
        String token = provider.generateAccessToken(1L, "hello@world.com");
        String email = provider.getEmailFromToken(token);
        assertEquals("hello@world.com", email);
    }

    @Test
    void validateToken_validToken_returnsTrue() {
        String token = provider.generateAccessToken(1L, "test@test.com");
        assertTrue(provider.validateToken(token));
    }

    @Test
    void validateToken_invalidToken_returnsFalse() {
        assertFalse(provider.validateToken("not.a.valid.jwt.token"));
    }

    @Test
    void validateToken_nullToken_returnsFalse() {
        assertFalse(provider.validateToken(null));
    }

    @Test
    void validateToken_emptyToken_returnsFalse() {
        assertFalse(provider.validateToken(""));
    }

    @Test
    void validateToken_tamperedToken_returnsFalse() {
        String token = provider.generateAccessToken(1L, "test@test.com");
        String tampered = token + "tampered";
        assertFalse(provider.validateToken(tampered));
    }

    @Test
    void isRefreshToken_refreshToken_returnsTrue() {
        String token = provider.generateRefreshToken(1L, "test@test.com");
        assertTrue(provider.isRefreshToken(token));
    }

    @Test
    void isRefreshToken_accessToken_returnsFalse() {
        String token = provider.generateAccessToken(1L, "test@test.com");
        assertFalse(provider.isRefreshToken(token));
    }

    @Test
    void accessAndRefreshTokensAreDifferent() {
        String access = provider.generateAccessToken(1L, "test@test.com");
        String refresh = provider.generateRefreshToken(1L, "test@test.com");
        assertNotEquals(access, refresh);
    }

    @Test
    void validateToken_expiredToken_returnsFalse() {
        
        JwtTokenProvider shortLivedProvider = new JwtTokenProvider(
            "test-secret-key-that-is-at-least-256-bits-long-for-hmac-sha256",
            0, 0
        );
        String token = shortLivedProvider.generateAccessToken(1L, "test@test.com");
        
        assertFalse(shortLivedProvider.validateToken(token));
    }
}
