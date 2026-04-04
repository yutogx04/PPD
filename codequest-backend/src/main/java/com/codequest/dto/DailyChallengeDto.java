package com.codequest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class DailyChallengeDto {

    private long challengeId;
    private String title;
    private String language;
    private String difficulty;
    private int xpReward;
    private boolean isCompleted;

    public static DailyChallengeDto fromEntity(com.codequest.entity.DailyChallenge dc, boolean completed, String lang) {
        var challenge = dc.getChallenge();
        return DailyChallengeDto.builder()
                .challengeId(challenge.getId())
                .title(com.codequest.dto.TrackDto.l(challenge.getTitle(), challenge.getTitleEn(), lang))
                .language(challenge.getLanguage().name())
                .difficulty(challenge.getDifficulty().name())
                .xpReward(challenge.getXpReward())
                .isCompleted(completed)
                .build();
    }
}
