package com.codequest.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "badges")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Badge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(length = 300)
    private String description;

    private String nameEn;

    @Column(length = 300)
    private String descriptionEn;

    private String icon;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ConditionType conditionType;

    private int conditionValue;

    public enum ConditionType {
        TOTAL_LESSONS,
        TOTAL_CHALLENGES,
        FIRST_ATTEMPT_SUCCESS,
        STREAK_DAYS,
        TOTAL_TRACKS,
        NIGHT_SUBMIT,
        TRACK_COMPLETE,
        ACHIEVE_LEVEL
    }
}
