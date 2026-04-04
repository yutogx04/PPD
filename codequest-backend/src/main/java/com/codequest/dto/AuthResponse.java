package com.codequest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class AuthResponse {

    private String accessToken;
    private String refreshToken;
    private UserDto user;

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class UserDto {
        private Long id;
        private String email;
        private String pseudo;
        private String avatarUrl;
        private String bio;
        private int xp;
        private int level;
        private int streak;
        private int totalLessonsCompleted;
        private int totalChallengesSolved;
        private String role;
    }

    public static UserDto fromEntity(com.codequest.entity.User user) {
        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .pseudo(user.getPseudo())
                .avatarUrl(user.getAvatarUrl())
                .bio(user.getBio())
                .xp(user.getXp())
                .level(user.getLevel())
                .streak(user.getStreak())
                .totalLessonsCompleted(user.getTotalLessonsCompleted())
                .totalChallengesSolved(user.getTotalChallengesSolved())
                .role(user.getRole().name())
                .build();
    }
}
