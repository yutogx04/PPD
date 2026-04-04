package com.codequest.dto;

import com.codequest.entity.Module;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class ModuleDto {

    private Long id;
    private Long trackId;
    private String title;
    private String description;
    private int orderIndex;
    private int xpReward;
    private int lessonCount;
    private int challengeCount;
    private int completedLessons;

    public static ModuleDto fromEntity(Module module) {
        return fromEntity(module, 0, "fr");
    }

    public static ModuleDto fromEntity(Module module, int completedLessons) {
        return fromEntity(module, completedLessons, "fr");
    }

    public static ModuleDto fromEntity(Module module, int completedLessons, String lang) {
        return ModuleDto.builder()
                .id(module.getId())
                .trackId(module.getTrack() != null ? module.getTrack().getId() : null)
                .title(TrackDto.l(module.getTitle(), module.getTitleEn(), lang))
                .description(TrackDto.l(module.getDescription(), module.getDescriptionEn(), lang))
                .orderIndex(module.getOrderIndex())
                .xpReward(module.getTrack() != null ? module.getTrack().getXpPerLesson() : 20)
                .lessonCount(module.getLessons() != null ? module.getLessons().size() : 0)
                .challengeCount(module.getChallenges() != null ? module.getChallenges().size() : 0)
                .completedLessons(completedLessons)
                .build();
    }
}
