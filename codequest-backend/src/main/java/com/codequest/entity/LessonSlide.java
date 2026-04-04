package com.codequest.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "lesson_slides")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class LessonSlide {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int orderIndex;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ContentType contentType;

    @Column(columnDefinition = "TEXT")
    private String contentText;

    @Column(columnDefinition = "TEXT")
    private String codeSnippet;

    private String codeLanguage;

    @Column(columnDefinition = "TEXT")
    private String explanation;

    @Column(columnDefinition = "TEXT")
    private String contentTextEn;

    @Column(columnDefinition = "TEXT")
    private String explanationEn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lesson_id", nullable = false)
    private Lesson lesson;

    @OneToOne(mappedBy = "slide", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private QuizQuestion quizQuestion;

    public enum ContentType {
        TEXT, CODE, QCM
    }
}
