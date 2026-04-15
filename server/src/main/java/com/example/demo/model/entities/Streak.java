package com.example.demo.model.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "streaks")
public class Streak {

    @Id
    private String id;

    // External user service — not a DB-level FK
    private String userId;

    private int currentStreak;
    private int longestStreak;
    private LocalDate lastActivityDate;

    // e.g. { "2025-03-01": true, "2025-03-02": false }
    private Map<String, Boolean> weeklyActivity;
}