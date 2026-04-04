package com.codequest.service;

import org.springframework.stereotype.Component;

@Component
public class ScoreCalculator {

    public int calculateScore(int attemptNumber, long executionTimeMs, long memoryUsedKb,
                               long referenceTimeMs, long referenceMemoryKb) {

        int exactitudePoints = 50;

        int tentativesPoints;
        switch (attemptNumber) {
            case 1: tentativesPoints = 30; break;
            case 2: tentativesPoints = 20; break;
            case 3: tentativesPoints = 10; break;
            default: tentativesPoints = 0;
        }

        int tempsPoints = calculateTimePoints(executionTimeMs, referenceTimeMs);
        int memoirePoints = calculateMemoryPoints(memoryUsedKb, referenceMemoryKb);

        return exactitudePoints + tentativesPoints + tempsPoints + memoirePoints;
    }

    private int calculateTimePoints(long executionTimeMs, long referenceTimeMs) {
        if (referenceTimeMs <= 0) return 15;
        double ratio = (double) executionTimeMs / referenceTimeMs;
        if (ratio <= 1.0) return 15;
        if (ratio <= 2.0) return 10;
        if (ratio <= 5.0) return 5;
        return 0;
    }

    private int calculateMemoryPoints(long memoryUsedKb, long referenceMemoryKb) {
        if (referenceMemoryKb <= 0) return 5;
        double ratio = (double) memoryUsedKb / referenceMemoryKb;
        if (ratio <= 1.0) return 5;
        if (ratio <= 2.0) return 3;
        return 0;
    }

    public String calculateGrade(int score) {
        if (score >= 90) return "S";
        if (score >= 70) return "A";
        if (score >= 50) return "B";
        if (score >= 30) return "C";
        return "D";
    }

    public int getXpForDifficulty(String difficulty) {
        return switch (difficulty.toUpperCase()) {
            case "BEGINNER" -> 30;
            case "INTERMEDIATE" -> 50;
            case "ADVANCED" -> 80;
            default -> 30;
        };
    }

    public int calculateBonusXp(int attemptNumber, boolean isDailyChallenge) {
        int bonus = 0;
        if (attemptNumber == 1) bonus += 25;
        if (isDailyChallenge) bonus += 50;
        return bonus;
    }
}
