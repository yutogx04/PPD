package com.codequest.dto;

import com.codequest.entity.Challenge;
import com.codequest.entity.DailyChallenge;
import com.codequest.entity.Track;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DailyChallengeDtoTest {

    @Test
    void fromEntity_mapsAllFields() {
        Challenge challenge = Challenge.builder()
                .id(1L)
                .title("FizzBuzz")
                .language(Track.Language.PYTHON)
                .difficulty(Track.Difficulty.BEGINNER)
                .xpReward(30)
                .build();

        DailyChallenge dc = DailyChallenge.builder()
                .id(1L)
                .challenge(challenge)
                .build();

        DailyChallengeDto dto = DailyChallengeDto.fromEntity(dc, false, "en");

        assertEquals(1L, dto.getChallengeId());
        assertEquals("FizzBuzz", dto.getTitle());
        assertEquals("PYTHON", dto.getLanguage());
        assertEquals("BEGINNER", dto.getDifficulty());
        assertEquals(30, dto.getXpReward());
        assertFalse(dto.isCompleted());
    }

    @Test
    void fromEntity_completed() {
        Challenge challenge = Challenge.builder()
                .id(2L)
                .title("Sort Array")
                .language(Track.Language.JAVASCRIPT)
                .difficulty(Track.Difficulty.INTERMEDIATE)
                .xpReward(50)
                .build();

        DailyChallenge dc = DailyChallenge.builder()
                .id(2L)
                .challenge(challenge)
                .build();

        DailyChallengeDto dto = DailyChallengeDto.fromEntity(dc, true, "en");

        assertTrue(dto.isCompleted());
        assertEquals("JAVASCRIPT", dto.getLanguage());
        assertEquals("INTERMEDIATE", dto.getDifficulty());
    }

    @Test
    void fromEntity_advancedJava() {
        Challenge challenge = Challenge.builder()
                .id(3L)
                .title("Binary Search")
                .language(Track.Language.JAVA)
                .difficulty(Track.Difficulty.ADVANCED)
                .xpReward(80)
                .build();

        DailyChallenge dc = DailyChallenge.builder()
                .id(3L)
                .challenge(challenge)
                .build();

        DailyChallengeDto dto = DailyChallengeDto.fromEntity(dc, false, "en");

        assertEquals("JAVA", dto.getLanguage());
        assertEquals("ADVANCED", dto.getDifficulty());
        assertEquals(80, dto.getXpReward());
    }
}
