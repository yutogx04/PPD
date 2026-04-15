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
@Document(collection = "progress")
public class Progress {

    @Id
    private String id;

    // External user service — not a DB-level FK
    private String userId;

    private String lessonId;
    private String challengeId;

    // "LESSON" or "CHALLENGE"
    private String contentType;

    private String status;
    private int lastPosition;
    private int attempts;
    private int bestScore;
    private boolean completed;

    private LocalDateTime completedAt;
    private LocalDateTime updatedAt;
}