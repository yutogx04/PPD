package com.codequest.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(unique = true, nullable = false, length = 30)
    private String pseudo;

    @Column(nullable = false)
    private String password;

    private String avatarUrl;

    @Column(length = 200)
    private String bio;

    @Builder.Default
    private int xp = 0;

    @Builder.Default
    private int level = 1;

    @Builder.Default
    private int streak = 0;

    @Builder.Default
    private int totalLessonsCompleted = 0;

    @Builder.Default
    private int totalChallengesSolved = 0;

    private LocalDate lastActivityDate;

    @Builder.Default
    private boolean emailVerified = false;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Role role = Role.USER;

    @Builder.Default
    private boolean enabled = true;

    private LocalDateTime createdAt;

    @Column(length = 512)
    private String fcmToken;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public enum Role {
        USER, ADMIN
    }
}
