package com.codequest.dto;

import com.codequest.entity.Badge;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class GamificationResult {

    private int xpGained;
    private int newXpTotal;
    private int newLevel;
    private boolean leveledUp;
    private List<BadgeDto> badgesUnlocked;

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class BadgeDto {
        private Long id;
        private String name;
        private String description;
        private String icon;
        private boolean isEarned;
        private String obtainedAt;
    }
}
