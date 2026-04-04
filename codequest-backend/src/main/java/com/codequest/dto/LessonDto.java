package com.codequest.dto;

import com.codequest.entity.Lesson;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class LessonDto {

    private Long id;
    private Long moduleId;
    private String title;
    private String lessonType;
    private int orderIndex;
    private int xpReward;
    private int estimatedMinutes;
    private boolean isCompleted;
    private boolean isFavorite;
    private int savedSlideIndex;
    private int slideCount;

    public static LessonDto fromEntity(Lesson lesson, boolean completed, boolean favorite, int savedSlideIndex) {
        return fromEntity(lesson, completed, favorite, savedSlideIndex, "fr");
    }

    public static LessonDto fromEntity(Lesson lesson, boolean completed, boolean favorite, int savedSlideIndex, String lang) {
        return LessonDto.builder()
                .id(lesson.getId())
                .moduleId(lesson.getModule() != null ? lesson.getModule().getId() : null)
                .title(TrackDto.l(lesson.getTitle(), lesson.getTitleEn(), lang))
                .lessonType(lesson.getType().name())
                .orderIndex(lesson.getOrderIndex())
                .xpReward(lesson.getXpReward())
                .estimatedMinutes(lesson.getDurationMinutes())
                .isCompleted(completed)
                .isFavorite(favorite)
                .savedSlideIndex(savedSlideIndex)
                .slideCount(lesson.getSlides() != null ? lesson.getSlides().size() : 0)
                .build();
    }
}
