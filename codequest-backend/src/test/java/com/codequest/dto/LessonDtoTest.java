package com.codequest.dto;

import com.codequest.entity.Lesson;
import com.codequest.entity.LessonSlide;
import com.codequest.entity.Module;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LessonDtoTest {

    @Test
    void fromEntity_mapsAllFields() {
        Module module = Module.builder().id(10L).build();
        Lesson lesson = Lesson.builder()
                .id(1L)
                .title("Intro to Variables")
                .type(Lesson.LessonType.THEORY)
                .orderIndex(0)
                .xpReward(25)
                .durationMinutes(10)
                .module(module)
                .slides(List.of(
                        LessonSlide.builder().build(),
                        LessonSlide.builder().build()
                ))
                .build();

        LessonDto dto = LessonDto.fromEntity(lesson, true, false, 1);

        assertEquals(1L, dto.getId());
        assertEquals(10L, dto.getModuleId());
        assertEquals("Intro to Variables", dto.getTitle());
        assertEquals("THEORY", dto.getLessonType());
        assertEquals(0, dto.getOrderIndex());
        assertEquals(25, dto.getXpReward());
        assertEquals(10, dto.getEstimatedMinutes());
        assertTrue(dto.isCompleted());
        assertFalse(dto.isFavorite());
        assertEquals(1, dto.getSavedSlideIndex());
        assertEquals(2, dto.getSlideCount());
    }

    @Test
    void fromEntity_nullModule_givesNullModuleId() {
        Lesson lesson = Lesson.builder()
                .id(2L)
                .title("Test")
                .type(Lesson.LessonType.PRACTICE)
                .build();

        LessonDto dto = LessonDto.fromEntity(lesson, false, true, 0);

        assertNull(dto.getModuleId());
        assertEquals("PRACTICE", dto.getLessonType());
        assertTrue(dto.isFavorite());
    }

    @Test
    void fromEntity_nullSlides_givesZeroSlideCount() {
        Lesson lesson = Lesson.builder()
                .id(3L)
                .title("No slides")
                .type(Lesson.LessonType.THEORY)
                .build();

        LessonDto dto = LessonDto.fromEntity(lesson, false, false, 0);

        assertEquals(0, dto.getSlideCount());
    }

    @Test
    void fromEntity_favoriteCompleted() {
        Module module = Module.builder().id(5L).build();
        Lesson lesson = Lesson.builder()
                .id(4L)
                .title("Fav lesson")
                .type(Lesson.LessonType.THEORY)
                .module(module)
                .build();

        LessonDto dto = LessonDto.fromEntity(lesson, true, true, 5);

        assertTrue(dto.isCompleted());
        assertTrue(dto.isFavorite());
        assertEquals(5, dto.getSavedSlideIndex());
    }
}
