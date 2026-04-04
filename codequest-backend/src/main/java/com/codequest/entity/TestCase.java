package com.codequest.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "test_cases")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class TestCase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String input;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String expectedOutput;

    @Builder.Default
    private boolean hidden = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenge_id", nullable = false)
    private Challenge challenge;
}
