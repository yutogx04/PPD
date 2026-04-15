package com.example.demo.model.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "lessons")
public abstract class Lesson {

    @Id
    private String id;

    private String title;
    private String lessonType;      // "THEORY" or "PRACTICE" — discriminator field
    private int estimatedMinutes;
    private String language;        // e.g. Python, Java
    private int orderIndex;
    private boolean published;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
