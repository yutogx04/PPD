package com.codequest.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "app_settings")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Builder.Default
    private String appName = "CodeQuest";

    @Column(nullable = false)
    @Builder.Default
    private String backendUrl = "http://localhost:8080";

    @Column(nullable = false)
    @Builder.Default
    private String defaultLanguage = "Français";

    @Column(nullable = false)
    @Builder.Default
    private String dailyReminderTime = "20:00";

    @Builder.Default
    private int streakAlertHours = 2;

    @Column(nullable = false)
    @Builder.Default
    private String dailyChallengeTime = "08:00";

    @Builder.Default
    private int sandboxTimeoutSec = 5;

    @Builder.Default
    private int sandboxMemoryMb = 64;

    @Builder.Default
    private int sandboxRateLimit = 1;

    @Column(nullable = false)
    @Builder.Default
    private String primaryColor = "#6C63FF";
}
