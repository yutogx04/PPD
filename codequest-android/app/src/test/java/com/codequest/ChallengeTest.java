package com.codequest;
import com.codequest.model.Challenge;
import com.codequest.util.Constants;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
public class ChallengeTest {
    private Challenge challenge;
    @Before
    public void setUp() {
        challenge = new Challenge(1L, "Inverser une chaîne",
                "Écrivez une fonction...", "EASY", "PYTHON",
                "def reverse(s):\n    pass", 30);
    }
    @Test
    public void constructor_setsAllFields() {
        assertEquals(1L, challenge.getId());
        assertEquals("Inverser une chaîne", challenge.getTitle());
        assertEquals("EASY", challenge.getDifficulty());
        assertEquals("PYTHON", challenge.getLanguage());
        assertEquals("def reverse(s):\n    pass", challenge.getStarterCode());
        assertEquals(30, challenge.getXpReward());
    }
    @Test
    public void defaultConstructor_createsEmpty() {
        Challenge empty = new Challenge();
        assertEquals(0, empty.getId());
        assertNull(empty.getTitle());
        assertFalse(empty.isSolved());
        assertEquals(0, empty.getAttemptCount());
    }
    @Test
    public void isHintAvailable_0attempts_false() {
        challenge.setAttemptCount(0);
        assertFalse(challenge.isHintAvailable());
    }
    @Test
    public void isHintAvailable_2attempts_false() {
        challenge.setAttemptCount(2);
        assertFalse(challenge.isHintAvailable());
    }
    @Test
    public void isHintAvailable_3attempts_true() {
        challenge.setAttemptCount(3);
        assertTrue(challenge.isHintAvailable());
    }
    @Test
    public void isHintAvailable_5attempts_true() {
        challenge.setAttemptCount(5);
        assertTrue(challenge.isHintAvailable());
    }
    @Test
    public void isSolutionAvailable_0attempts_false() {
        challenge.setAttemptCount(0);
        assertFalse(challenge.isSolutionAvailable());
    }
    @Test
    public void isSolutionAvailable_4attempts_false() {
        challenge.setAttemptCount(4);
        assertFalse(challenge.isSolutionAvailable());
    }
    @Test
    public void isSolutionAvailable_5attempts_true() {
        challenge.setAttemptCount(5);
        assertTrue(challenge.isSolutionAvailable());
    }
    @Test
    public void isSolutionAvailable_10attempts_true() {
        challenge.setAttemptCount(10);
        assertTrue(challenge.isSolutionAvailable());
    }
    @Test
    public void hintAvailableBeforeSolution() {
        challenge.setAttemptCount(3);
        assertTrue(challenge.isHintAvailable());
        assertFalse(challenge.isSolutionAvailable());
    }
    @Test
    public void isSolved_defaultFalse() {
        assertFalse(challenge.isSolved());
    }
    @Test
    public void setSolved_makes_isSolved_true() {
        challenge.setSolved(true);
        assertTrue(challenge.isSolved());
    }
    @Test
    public void bestScore_defaultZero() {
        assertEquals(0, challenge.getBestScore());
    }
    @Test
    public void setBestScore_setBestGrade_works() {
        challenge.setBestScore(95);
        challenge.setBestGrade("S");
        assertEquals(95, challenge.getBestScore());
        assertEquals("S", challenge.getBestGrade());
    }
    @Test
    public void setExamples_works() {
        challenge.setExampleInput("hello");
        challenge.setExampleOutput("olleh");
        challenge.setExampleInput2("world");
        challenge.setExampleOutput2("dlrow");
        assertEquals("hello", challenge.getExampleInput());
        assertEquals("olleh", challenge.getExampleOutput());
        assertEquals("world", challenge.getExampleInput2());
        assertEquals("dlrow", challenge.getExampleOutput2());
    }
    @Test
    public void xpReward_easyChallenge_matchesCDC() {
        challenge.setXpReward(Constants.XP_CHALLENGE_EASY);
        assertEquals(30, challenge.getXpReward());
    }
    @Test
    public void xpReward_mediumChallenge_matchesCDC() {
        challenge.setXpReward(Constants.XP_CHALLENGE_MEDIUM);
        assertEquals(50, challenge.getXpReward());
    }
    @Test
    public void xpReward_hardChallenge_matchesCDC() {
        challenge.setXpReward(Constants.XP_CHALLENGE_HARD);
        assertEquals(80, challenge.getXpReward());
    }
}
