package com.codequest.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "submissions")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Submission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String code;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Track.Language language;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @Builder.Default
    private int score = 0;

    private String grade;

    @Builder.Default
    private int xpGained = 0;

    @Builder.Default
    private int bonusXp = 0;

    private long executionTimeMs;

    private long memoryUsedKb;

    private String errorMessage;

    private int testCasesPassed;

    private int testCasesTotal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenge_id", nullable = false)
    private Challenge challenge;

    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public enum Status {
        ACCEPTED, WRONG_ANSWER, RUNTIME_ERROR, COMPILE_ERROR, TIMEOUT, MEMORY_LIMIT
    }
}
