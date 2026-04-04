package com.codequest.dto;

import com.codequest.entity.User;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AuthResponseTest {

    @Test
    void fromEntity_mapsAllFields() {
        User user = User.builder()
                .id(1L)
                .email("test@codequest.com")
                .pseudo("tester")
                .avatarUrl("https://avatar.com/img.png")
                .bio("Hello World")
                .xp(500)
                .level(3)
                .streak(7)
                .totalLessonsCompleted(10)
                .totalChallengesSolved(5)
                .role(User.Role.USER)
                .build();

        AuthResponse.UserDto dto = AuthResponse.fromEntity(user);

        assertEquals(1L, dto.getId());
        assertEquals("test@codequest.com", dto.getEmail());
        assertEquals("tester", dto.getPseudo());
        assertEquals("https://avatar.com/img.png", dto.getAvatarUrl());
        assertEquals("Hello World", dto.getBio());
        assertEquals(500, dto.getXp());
        assertEquals(3, dto.getLevel());
        assertEquals(7, dto.getStreak());
        assertEquals(10, dto.getTotalLessonsCompleted());
        assertEquals(5, dto.getTotalChallengesSolved());
        assertEquals("USER", dto.getRole());
    }

    @Test
    void fromEntity_adminRole() {
        User user = User.builder()
                .id(2L)
                .email("admin@codequest.com")
                .pseudo("admin")
                .role(User.Role.ADMIN)
                .build();

        AuthResponse.UserDto dto = AuthResponse.fromEntity(user);

        assertEquals("ADMIN", dto.getRole());
    }

    @Test
    void fromEntity_nullOptionalFields() {
        User user = User.builder()
                .id(3L)
                .email("min@test.com")
                .pseudo("min")
                .role(User.Role.USER)
                .build();

        AuthResponse.UserDto dto = AuthResponse.fromEntity(user);

        assertNull(dto.getAvatarUrl());
        assertNull(dto.getBio());
        assertEquals(0, dto.getXp());
        assertEquals(0, dto.getStreak());
    }

    @Test
    void authResponse_builder() {
        AuthResponse response = AuthResponse.builder()
                .accessToken("access_123")
                .refreshToken("refresh_456")
                .user(AuthResponse.UserDto.builder().id(1L).build())
                .build();

        assertEquals("access_123", response.getAccessToken());
        assertEquals("refresh_456", response.getRefreshToken());
        assertEquals(1L, response.getUser().getId());
    }
}
