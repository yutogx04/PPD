package com.codequest;
import com.codequest.util.Constants;
import org.junit.Test;
import static org.junit.Assert.*;
public class ConstantsTest {
    @Test
    public void xpThresholds_areInAscendingOrder() {
        assertTrue(Constants.LEVEL_1_XP < Constants.LEVEL_2_XP);
        assertTrue(Constants.LEVEL_2_XP < Constants.LEVEL_3_XP);
        assertTrue(Constants.LEVEL_3_XP < Constants.LEVEL_4_XP);
        assertTrue(Constants.LEVEL_4_XP < Constants.LEVEL_5_XP);
        assertTrue(Constants.LEVEL_5_XP < Constants.LEVEL_6_XP);
    }
    @Test
    public void xpThresholds_matchCDC() {
        assertEquals(0, Constants.LEVEL_1_XP);
        assertEquals(200, Constants.LEVEL_2_XP);
        assertEquals(500, Constants.LEVEL_3_XP);
        assertEquals(1000, Constants.LEVEL_4_XP);
        assertEquals(2000, Constants.LEVEL_5_XP);
        assertEquals(4000, Constants.LEVEL_6_XP);
    }
    @Test
    public void getLevelForXp_zeroXp_returnsLevel1() {
        assertEquals(1, Constants.getLevelForXp(0));
    }
    @Test
    public void getLevelForXp_exactThreshold_returnsCorrectLevel() {
        assertEquals(2, Constants.getLevelForXp(200));
        assertEquals(3, Constants.getLevelForXp(500));
        assertEquals(4, Constants.getLevelForXp(1000));
        assertEquals(5, Constants.getLevelForXp(2000));
        assertEquals(6, Constants.getLevelForXp(4000));
    }
    @Test
    public void getLevelForXp_betweenThresholds_returnsLowerLevel() {
        assertEquals(1, Constants.getLevelForXp(100));
        assertEquals(2, Constants.getLevelForXp(350));
        assertEquals(3, Constants.getLevelForXp(750));
        assertEquals(4, Constants.getLevelForXp(1500));
        assertEquals(5, Constants.getLevelForXp(3000));
    }
    @Test
    public void getLevelForXp_veryHighXp_returnsLevel6() {
        assertEquals(6, Constants.getLevelForXp(10000));
        assertEquals(6, Constants.getLevelForXp(999999));
    }
    @Test
    public void getXpForNextLevel_returnsCorrectValues() {
        assertEquals(200, Constants.getXpForNextLevel(1));
        assertEquals(500, Constants.getXpForNextLevel(2));
        assertEquals(1000, Constants.getXpForNextLevel(3));
        assertEquals(2000, Constants.getXpForNextLevel(4));
        assertEquals(4000, Constants.getXpForNextLevel(5));
    }
    @Test
    public void getXpForNextLevel_maxLevel_returnsMaxXp() {
        assertEquals(4000, Constants.getXpForNextLevel(6));
    }
    @Test
    public void getGrade_score100_returnsS() {
        assertEquals("S", Constants.getGrade(100));
    }
    @Test
    public void getGrade_score90_returnsS() {
        assertEquals("S", Constants.getGrade(90));
    }
    @Test
    public void getGrade_score89_returnsA() {
        assertEquals("A", Constants.getGrade(89));
    }
    @Test
    public void getGrade_score70_returnsA() {
        assertEquals("A", Constants.getGrade(70));
    }
    @Test
    public void getGrade_score69_returnsB() {
        assertEquals("B", Constants.getGrade(69));
    }
    @Test
    public void getGrade_score50_returnsB() {
        assertEquals("B", Constants.getGrade(50));
    }
    @Test
    public void getGrade_score49_returnsC() {
        assertEquals("C", Constants.getGrade(49));
    }
    @Test
    public void getGrade_score30_returnsC() {
        assertEquals("C", Constants.getGrade(30));
    }
    @Test
    public void getGrade_score29_returnsD() {
        assertEquals("D", Constants.getGrade(29));
    }
    @Test
    public void getGrade_score0_returnsD() {
        assertEquals("D", Constants.getGrade(0));
    }
    @Test
    public void gradeBoundaries_matchCDC() {
        assertEquals(90, Constants.GRADE_S_MIN);
        assertEquals(70, Constants.GRADE_A_MIN);
        assertEquals(50, Constants.GRADE_B_MIN);
        assertEquals(30, Constants.GRADE_C_MIN);
    }
    @Test
    public void xpRewards_arePositive() {
        assertTrue(Constants.XP_LESSON_COMPLETE > 0);
        assertTrue(Constants.XP_MODULE_COMPLETE > 0);
        assertTrue(Constants.XP_CHALLENGE_EASY > 0);
        assertTrue(Constants.XP_CHALLENGE_MEDIUM > 0);
        assertTrue(Constants.XP_CHALLENGE_HARD > 0);
        assertTrue(Constants.XP_FIRST_ATTEMPT_BONUS > 0);
        assertTrue(Constants.XP_DAILY_CHALLENGE_BONUS > 0);
        assertTrue(Constants.XP_STREAK_7_DAYS_BONUS > 0);
    }
    @Test
    public void xpRewards_difficultyScaling() {
        assertTrue(Constants.XP_CHALLENGE_EASY < Constants.XP_CHALLENGE_MEDIUM);
        assertTrue(Constants.XP_CHALLENGE_MEDIUM < Constants.XP_CHALLENGE_HARD);
    }
    @Test
    public void xpRewards_matchCDC() {
        assertEquals(20, Constants.XP_LESSON_COMPLETE);
        assertEquals(50, Constants.XP_MODULE_COMPLETE);
        assertEquals(30, Constants.XP_CHALLENGE_EASY);
        assertEquals(50, Constants.XP_CHALLENGE_MEDIUM);
        assertEquals(80, Constants.XP_CHALLENGE_HARD);
        assertEquals(25, Constants.XP_FIRST_ATTEMPT_BONUS);
        assertEquals(50, Constants.XP_DAILY_CHALLENGE_BONUS);
        assertEquals(100, Constants.XP_STREAK_7_DAYS_BONUS);
    }
    @Test
    public void rateLimits_submitCooldown_is10Seconds() {
        assertEquals(10000, Constants.SUBMIT_COOLDOWN_MS);
    }
    @Test
    public void rateLimits_maxCodeLength_is10000() {
        assertEquals(10000, Constants.MAX_CODE_LENGTH);
    }
    @Test
    public void challengeThresholds_hintAfter3() {
        assertEquals(3, Constants.HINT_THRESHOLD);
    }
    @Test
    public void challengeThresholds_solutionAfter5() {
        assertEquals(5, Constants.SOLUTION_THRESHOLD);
    }
    @Test
    public void challengeThresholds_hintBeforeSolution() {
        assertTrue(Constants.HINT_THRESHOLD < Constants.SOLUTION_THRESHOLD);
    }
    @Test
    public void trackLocking_javaRequiresLevel5() {
        assertEquals(5, Constants.JAVA_REQUIRED_LEVEL);
    }
}
