package com.codequest.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "daily_challenges")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class DailyChallenge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "challenge_id", nullable = false)
    private Challenge challenge;

    @Column(nullable = false, unique = true)
    private LocalDate date;
}
