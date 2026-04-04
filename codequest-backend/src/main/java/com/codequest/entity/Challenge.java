package com.codequest.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "challenges")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Challenge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String titleEn;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;

    @Column(columnDefinition = "TEXT")
    private String descriptionEn;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Track.Difficulty difficulty;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Track.Language language;

    @Column(columnDefinition = "TEXT")
    private String starterCode;

    @Column(columnDefinition = "TEXT")
    private String hint;

    @Column(columnDefinition = "TEXT")
    private String hintEn;

    @Column(columnDefinition = "TEXT")
    private String solution;

    private int xpReward;

    @Column(columnDefinition = "TEXT")
    private String exampleInput;

    @Column(columnDefinition = "TEXT")
    private String exampleOutput;

    @Column(columnDefinition = "TEXT")
    private String exampleInput2;

    @Column(columnDefinition = "TEXT")
    private String exampleOutput2;

    @Column(columnDefinition = "TEXT")
    private String referenceSolution;

    private long referenceTimeMs;

    private long referenceMemoryKb;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "module_id", nullable = false)
    private Module module;
}
