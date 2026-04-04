package com.codequest.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "quiz_questions")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class QuizQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String questionText;

    @Column(nullable = false)
    private String option1;

    @Column(nullable = false)
    private String option2;

    @Column(nullable = false)
    private String option3;

    @Column(nullable = false)
    private String option4;

    private int correctOption;

    @Column(columnDefinition = "TEXT")
    private String explanation;

    @Column(columnDefinition = "TEXT")
    private String questionTextEn;

    private String option1En;
    private String option2En;
    private String option3En;
    private String option4En;

    @Column(columnDefinition = "TEXT")
    private String explanationEn;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "slide_id", nullable = false)
    private LessonSlide slide;
}
