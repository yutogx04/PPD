package com.codequest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class SubmissionResponse {

    private String status;
    private String output;
    private String errorMessage;
    private int testCasesPassed;
    private int testCasesTotal;
    private int score;
    private String grade;
    private int xpGained;
    private int bonusXp;
    private List<GamificationResult.BadgeDto> badgesUnlocked;
    private List<TestCaseResultDto> testResults;

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class TestCaseResultDto {
        private String input;
        private String expectedOutput;
        private String actualOutput;
        private boolean passed;
        private long timeMs;
    }
}
