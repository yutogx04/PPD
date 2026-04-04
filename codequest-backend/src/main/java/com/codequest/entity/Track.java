package com.codequest.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "tracks")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Track {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(length = 500)
    private String description;

    private String titleEn;

    @Column(length = 500)
    private String descriptionEn;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Difficulty difficulty;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Language language;

    @Builder.Default
    private int requiredLevel = 1;

    @Builder.Default
    private int xpPerLesson = 20;

    @OneToMany(mappedBy = "track", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OrderBy("orderIndex ASC")
    private List<Module> modules;

    public enum Difficulty {
        BEGINNER, INTERMEDIATE, ADVANCED
    }

    public enum Language {
        PYTHON, JAVASCRIPT, JAVA
    }
}
