package com.example.demo.model.entities;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "challenges")
@Entity
public class Challenge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    private String title;
    private String description;
    private String exampleInput;
    private String exampleOutput;
    private String difficulty;

    private int timeLimitSeconds;
    private int memoryLimitMb;
    private int baseXpReward;

    private boolean isDailyChallenge;
    private LocalDate dailyDate;
    private boolean published;

    // Embedded — no separate collection
    private List<TestCase> testCases;

    private LocalDateTime createdAt;
}