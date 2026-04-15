package com.example.demo.model.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "submissions")
public class Submission {

    @Id
    private String id;

    // External user service — not a DB-level FK
    private String userId;

    private String challengeId;

    private String language;
    private String code;

    // PENDING, ACCEPTED, WRONG_ANSWER, TIME_LIMIT, RUNTIME_ERROR
    private String status;

    private int score;
    private String grade;

    private int executionTimeMs;
    private int memoryUsedMb;
    private int attemptNumber;
    private int passedTests;
    private int totalTests;

    private LocalDateTime submittedAt;
}