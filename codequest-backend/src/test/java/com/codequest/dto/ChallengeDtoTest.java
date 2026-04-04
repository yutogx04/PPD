package com.codequest.dto;

import com.codequest.entity.Challenge;
import com.codequest.entity.Track;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ChallengeDtoTest {

    @Test
    void fromEntity_mapsAllFields() {
        Challenge challenge = Challenge.builder()
                .id(1L)
                .title("FizzBuzz")
                .description("Classic coding challenge")
                .difficulty(Track.Difficulty.BEGINNER)
                .language(Track.Language.PYTHON)
                .starterCode("def fizzbuzz(n):")
                .hint("Use modulo operator")
                .xpReward(30)
                .exampleInput("15")
                .exampleOutput("FizzBuzz")
                .exampleInput2("3")
                .exampleOutput2("Fizz")
                .build();

        ChallengeDto dto = ChallengeDto.fromEntity(challenge, 3, 95, "S", true);

        assertEquals(1L, dto.getId());
        assertEquals("FizzBuzz", dto.getTitle());
        assertEquals("Classic coding challenge", dto.getDescription());
        assertEquals("BEGINNER", dto.getDifficulty());
        assertEquals("PYTHON", dto.getLanguage());
        assertEquals("def fizzbuzz(n):", dto.getStarterCode());
        assertEquals("Use modulo operator", dto.getHint());
        assertEquals(30, dto.getXpReward());
        assertEquals("15", dto.getExampleInput());
        assertEquals("FizzBuzz", dto.getExampleOutput());
        assertEquals("3", dto.getExampleInput2());
        assertEquals("Fizz", dto.getExampleOutput2());
        assertTrue(dto.isSolved());
        assertEquals(95, dto.getBestScore());
        assertEquals("S", dto.getBestGrade());
        assertEquals(3, dto.getAttemptCount());
    }

    @Test
    void fromEntity_unsolved() {
        Challenge challenge = Challenge.builder()
                .id(2L)
                .title("Reverse String")
                .difficulty(Track.Difficulty.INTERMEDIATE)
                .language(Track.Language.JAVASCRIPT)
                .xpReward(50)
                .build();

        ChallengeDto dto = ChallengeDto.fromEntity(challenge, 0, 0, null, false);

        assertFalse(dto.isSolved());
        assertEquals(0, dto.getBestScore());
        assertNull(dto.getBestGrade());
        assertEquals(0, dto.getAttemptCount());
    }

    @Test
    void fromEntity_advancedJava() {
        Challenge challenge = Challenge.builder()
                .id(3L)
                .title("Binary Tree")
                .difficulty(Track.Difficulty.ADVANCED)
                .language(Track.Language.JAVA)
                .xpReward(80)
                .build();

        ChallengeDto dto = ChallengeDto.fromEntity(challenge, 5, 45, "C", false);

        assertEquals("ADVANCED", dto.getDifficulty());
        assertEquals("JAVA", dto.getLanguage());
        assertEquals(80, dto.getXpReward());
    }
}
