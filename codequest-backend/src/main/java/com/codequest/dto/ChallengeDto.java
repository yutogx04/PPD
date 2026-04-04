package com.codequest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.codequest.dto.TrackDto.l;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class ChallengeDto {

    private Long id;
    private String title;
    private String description;
    private String difficulty;
    private String language;
    private String starterCode;
    private String hint;
    private int xpReward;
    private String exampleInput;
    private String exampleOutput;
    private String exampleInput2;
    private String exampleOutput2;
    private boolean isSolved;
    private int bestScore;
    private String bestGrade;
    private int attemptCount;

    public static ChallengeDto fromEntity(com.codequest.entity.Challenge c,
                                           int attemptCount, int bestScore,
                                           String bestGrade, boolean solved) {
        return fromEntity(c, attemptCount, bestScore, bestGrade, solved, "fr");
    }

    public static ChallengeDto fromEntity(com.codequest.entity.Challenge c,
                                           int attemptCount, int bestScore,
                                           String bestGrade, boolean solved, String lang) {
        return ChallengeDto.builder()
                .id(c.getId())
                .title(l(c.getTitle(), c.getTitleEn(), lang))
                .description(l(c.getDescription(), c.getDescriptionEn(), lang))
                .difficulty(c.getDifficulty().name())
                .language(c.getLanguage().name())
                .starterCode(c.getStarterCode())
                .hint(l(c.getHint(), c.getHintEn(), lang))
                .xpReward(c.getXpReward())
                .exampleInput(c.getExampleInput())
                .exampleOutput(c.getExampleOutput())
                .exampleInput2(c.getExampleInput2())
                .exampleOutput2(c.getExampleOutput2())
                .isSolved(solved)
                .bestScore(bestScore)
                .bestGrade(bestGrade)
                .attemptCount(attemptCount)
                .build();
    }
}
