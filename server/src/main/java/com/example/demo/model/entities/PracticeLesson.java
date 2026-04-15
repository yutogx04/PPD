package com.example.demo.model.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "lessons")
public class PracticeLesson extends Lesson {

    private String instructions;
    private String starterCode;
    private String solution;

    // BEGINNER, INTERMEDIATE, ADVANCED
    private String difficulty;

    // Unlocked progressively after failed attempts
    private List<String> hints;

    // Embedded test cases — no separate collection
    private List<TestCase> testCases;
}