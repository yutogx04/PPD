package com.example.demo.model.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "lessons")
public class TheoryLesson extends Lesson {

    // Each slide: { "text": "...", "code": "..." }
    private List<Map<String, String>> slides;

    // Each quiz: { "question": "...", "options": [...], "answer": "..." }
    private List<Map<String, Object>> quizzes;
}
