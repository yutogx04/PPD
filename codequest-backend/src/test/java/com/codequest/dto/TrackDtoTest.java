package com.codequest.dto;

import com.codequest.entity.Track;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TrackDtoTest {

    @Test
    void fromEntity_mapsAllFields() {
        Track track = Track.builder()
                .id(1L)
                .title("Python Basics")
                .description("Learn Python")
                .difficulty(Track.Difficulty.BEGINNER)
                .language(Track.Language.PYTHON)
                .requiredLevel(1)
                .xpPerLesson(20)
                .build();

        TrackDto dto = TrackDto.fromEntity(track, 3, 10, 5, 60, false);

        assertEquals(1L, dto.getId());
        assertEquals("Python Basics", dto.getTitle());
        assertEquals("Learn Python", dto.getDescription());
        assertEquals("BEGINNER", dto.getDifficulty());
        assertEquals("PYTHON", dto.getLanguage());
        assertEquals(3, dto.getModuleCount());
        assertEquals(10, dto.getLessonCount());
        assertEquals(5, dto.getChallengeCount());
        assertEquals(60, dto.getProgressPercent());
        assertFalse(dto.isLocked());
        assertEquals(1, dto.getRequiredLevel());
        assertEquals(20, dto.getXpPerLesson());
    }

    @Test
    void fromEntity_lockedTrack() {
        Track track = Track.builder()
                .id(2L)
                .title("Advanced Java")
                .difficulty(Track.Difficulty.ADVANCED)
                .language(Track.Language.JAVA)
                .requiredLevel(5)
                .xpPerLesson(50)
                .build();

        TrackDto dto = TrackDto.fromEntity(track, 0, 0, 0, 0, true);

        assertTrue(dto.isLocked());
        assertEquals("ADVANCED", dto.getDifficulty());
        assertEquals("JAVA", dto.getLanguage());
        assertEquals(5, dto.getRequiredLevel());
    }

    @Test
    void fromEntity_intermediateJavaScript() {
        Track track = Track.builder()
                .id(3L)
                .title("JS Intermediate")
                .difficulty(Track.Difficulty.INTERMEDIATE)
                .language(Track.Language.JAVASCRIPT)
                .build();

        TrackDto dto = TrackDto.fromEntity(track, 5, 20, 10, 100, false);

        assertEquals("INTERMEDIATE", dto.getDifficulty());
        assertEquals("JAVASCRIPT", dto.getLanguage());
        assertEquals(100, dto.getProgressPercent());
    }
}
