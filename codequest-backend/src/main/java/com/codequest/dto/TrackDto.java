package com.codequest.dto;

import com.codequest.entity.Track;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class TrackDto {

    private Long id;
    private String title;
    private String description;
    private String difficulty;
    private String language;
    private int moduleCount;
    private int lessonCount;
    private int challengeCount;
    private int progressPercent;
    private boolean isLocked;
    private int requiredLevel;
    private int xpPerLesson;

    public static TrackDto fromEntity(Track track, int moduleCount, int lessonCount,
                                       int challengeCount, int progressPercent, boolean isLocked) {
        return fromEntity(track, moduleCount, lessonCount, challengeCount, progressPercent, isLocked, "fr");
    }

    public static TrackDto fromEntity(Track track, int moduleCount, int lessonCount,
                                       int challengeCount, int progressPercent, boolean isLocked,
                                       String lang) {
        return TrackDto.builder()
                .id(track.getId())
                .title(l(track.getTitle(), track.getTitleEn(), lang))
                .description(l(track.getDescription(), track.getDescriptionEn(), lang))
                .difficulty(track.getDifficulty().name())
                .language(track.getLanguage().name())
                .moduleCount(moduleCount)
                .lessonCount(lessonCount)
                .challengeCount(challengeCount)
                .progressPercent(progressPercent)
                .isLocked(isLocked)
                .requiredLevel(track.getRequiredLevel())
                .xpPerLesson(track.getXpPerLesson())
                .build();
    }

    public static String l(String fr, String en, String lang) {
        return "en".equals(lang) && en != null && !en.isEmpty() ? en : fr;
    }
}
