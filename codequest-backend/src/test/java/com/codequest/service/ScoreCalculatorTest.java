package com.codequest.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ScoreCalculatorTest {

    private ScoreCalculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new ScoreCalculator();
    }

    @Test
    void calculateScore_firstAttempt_perfectPerformance() {
        
        int score = calculator.calculateScore(1, 100, 50, 100, 50);
        assertEquals(100, score);
    }

    @Test
    void calculateScore_secondAttempt_perfectPerformance() {
        
        int score = calculator.calculateScore(2, 100, 50, 100, 50);
        assertEquals(90, score);
    }

    @Test
    void calculateScore_thirdAttempt() {
        
        int score = calculator.calculateScore(3, 100, 50, 100, 50);
        assertEquals(80, score);
    }

    @Test
    void calculateScore_fourthAttemptOrMore() {
        
        int score = calculator.calculateScore(4, 100, 50, 100, 50);
        assertEquals(70, score);
    }

    @Test
    void calculateScore_fifthAttempt_sameAsFourth() {
        int score = calculator.calculateScore(5, 100, 50, 100, 50);
        assertEquals(70, score);
    }

    @Test
    void calculateScore_executionTime_upTo2xSlower() {
        
        int score = calculator.calculateScore(1, 200, 50, 100, 50);
        assertEquals(95, score);
    }

    @Test
    void calculateScore_executionTime_upTo5xSlower() {
        
        int score = calculator.calculateScore(1, 500, 50, 100, 50);
        assertEquals(90, score);
    }

    @Test
    void calculateScore_executionTime_moreThan5xSlower() {
        
        int score = calculator.calculateScore(1, 600, 50, 100, 50);
        assertEquals(85, score);
    }

    @Test
    void calculateScore_memoryUsage_upTo2xMore() {
        
        int score = calculator.calculateScore(1, 100, 100, 100, 50);
        assertEquals(98, score);
    }

    @Test
    void calculateScore_memoryUsage_moreThan2x() {
        
        int score = calculator.calculateScore(1, 100, 200, 100, 50);
        assertEquals(95, score);
    }

    @Test
    void calculateScore_zeroReferenceTime_gives15TimePoints() {
        
        int score = calculator.calculateScore(1, 500, 50, 0, 50);
        assertEquals(100, score);
    }

    @Test
    void calculateScore_zeroReferenceMemory_gives5MemoryPoints() {
        
        int score = calculator.calculateScore(1, 100, 500, 100, 0);
        assertEquals(100, score);
    }

    @Test
    void calculateScore_worstCase() {
        
        int score = calculator.calculateScore(10, 1000, 500, 100, 50);
        assertEquals(50, score);
    }

    @Test
    void calculateGrade_S_at90() {
        assertEquals("S", calculator.calculateGrade(90));
    }

    @Test
    void calculateGrade_S_at100() {
        assertEquals("S", calculator.calculateGrade(100));
    }

    @Test
    void calculateGrade_A_at70() {
        assertEquals("A", calculator.calculateGrade(70));
    }

    @Test
    void calculateGrade_A_at89() {
        assertEquals("A", calculator.calculateGrade(89));
    }

    @Test
    void calculateGrade_B_at50() {
        assertEquals("B", calculator.calculateGrade(50));
    }

    @Test
    void calculateGrade_C_at30() {
        assertEquals("C", calculator.calculateGrade(30));
    }

    @Test
    void calculateGrade_D_below30() {
        assertEquals("D", calculator.calculateGrade(29));
    }

    @Test
    void calculateGrade_D_at0() {
        assertEquals("D", calculator.calculateGrade(0));
    }

    @Test
    void getXpForDifficulty_beginner() {
        assertEquals(30, calculator.getXpForDifficulty("BEGINNER"));
    }

    @Test
    void getXpForDifficulty_intermediate() {
        assertEquals(50, calculator.getXpForDifficulty("INTERMEDIATE"));
    }

    @Test
    void getXpForDifficulty_advanced() {
        assertEquals(80, calculator.getXpForDifficulty("ADVANCED"));
    }

    @Test
    void getXpForDifficulty_caseInsensitive() {
        assertEquals(50, calculator.getXpForDifficulty("intermediate"));
    }

    @Test
    void getXpForDifficulty_unknown_defaults30() {
        assertEquals(30, calculator.getXpForDifficulty("UNKNOWN"));
    }

    @Test
    void calculateBonusXp_firstAttempt_notDaily() {
        assertEquals(25, calculator.calculateBonusXp(1, false));
    }

    @Test
    void calculateBonusXp_notFirstAttempt_daily() {
        assertEquals(50, calculator.calculateBonusXp(2, true));
    }

    @Test
    void calculateBonusXp_firstAttempt_daily() {
        assertEquals(75, calculator.calculateBonusXp(1, true));
    }

    @Test
    void calculateBonusXp_notFirstAttempt_notDaily() {
        assertEquals(0, calculator.calculateBonusXp(3, false));
    }
}
