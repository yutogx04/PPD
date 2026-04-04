package com.codequest.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "lessons")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String titleEn;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private LessonType type = LessonType.THEORY;

    @Builder.Default
    private int durationMinutes = 5;

    @Builder.Default
    private int xpReward = 20;

    private int orderIndex;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "module_id", nullable = false)
    private Module module;

    @OneToMany(mappedBy = "lesson", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OrderBy("orderIndex ASC")
    private List<LessonSlide> slides;

    public enum LessonType {
        THEORY, PRACTICE
    }
}
