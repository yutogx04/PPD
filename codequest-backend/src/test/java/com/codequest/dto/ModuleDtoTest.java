package com.codequest.dto;

import com.codequest.entity.Challenge;
import com.codequest.entity.Lesson;
import com.codequest.entity.Module;
import com.codequest.entity.Track;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ModuleDtoTest {

    @Test
    void fromEntity_withCompletedLessons() {
        Track track = Track.builder().id(1L).xpPerLesson(30).build();
        Module module = Module.builder()
                .id(1L)
                .title("Variables et Types")
                .description("Learn about variables")
                .orderIndex(0)
                .track(track)
                .lessons(List.of(
                        Lesson.builder().build(),
                        Lesson.builder().build(),
                        Lesson.builder().build()
                ))
                .challenges(List.of(
                        Challenge.builder().build()
                ))
                .build();

        ModuleDto dto = ModuleDto.fromEntity(module, 2);

        assertEquals(1L, dto.getId());
        assertEquals(1L, dto.getTrackId());
        assertEquals("Variables et Types", dto.getTitle());
        assertEquals("Learn about variables", dto.getDescription());
        assertEquals(0, dto.getOrderIndex());
        assertEquals(30, dto.getXpReward());
        assertEquals(3, dto.getLessonCount());
        assertEquals(1, dto.getChallengeCount());
        assertEquals(2, dto.getCompletedLessons());
    }

    @Test
    void fromEntity_withoutCompletedLessons_defaultsZero() {
        Track track = Track.builder().id(2L).xpPerLesson(20).build();
        Module module = Module.builder()
                .id(2L)
                .title("Fonctions")
                .track(track)
                .build();

        ModuleDto dto = ModuleDto.fromEntity(module);

        assertEquals(0, dto.getCompletedLessons());
        assertEquals(0, dto.getLessonCount());
        assertEquals(0, dto.getChallengeCount());
    }

    @Test
    void fromEntity_nullTrack_givesNullTrackIdAndDefault20Xp() {
        Module module = Module.builder()
                .id(3L)
                .title("No Track")
                .build();

        ModuleDto dto = ModuleDto.fromEntity(module);

        assertNull(dto.getTrackId());
        assertEquals(20, dto.getXpReward()); 
    }

    @Test
    void fromEntity_nullLessonsAndChallenges_giveZeroCounts() {
        Module module = Module.builder()
                .id(4L)
                .title("Empty Module")
                .track(Track.builder().id(1L).build())
                .build();

        ModuleDto dto = ModuleDto.fromEntity(module);

        assertEquals(0, dto.getLessonCount());
        assertEquals(0, dto.getChallengeCount());
    }
}
