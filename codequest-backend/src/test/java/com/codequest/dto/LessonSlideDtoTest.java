package com.codequest.dto;

import com.codequest.entity.LessonSlide;
import com.codequest.entity.QuizQuestion;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LessonSlideDtoTest {

    @Test
    void fromEntity_textSlide() {
        LessonSlide slide = LessonSlide.builder()
                .id(1L)
                .orderIndex(0)
                .contentType(LessonSlide.ContentType.TEXT)
                .contentText("Variables store data values.")
                .explanation("Key concept")
                .build();

        LessonSlideDto dto = LessonSlideDto.fromEntity(slide);

        assertEquals(1L, dto.getId());
        assertEquals(0, dto.getOrderIndex());
        assertEquals("TEXT", dto.getContentType());
        assertEquals("Variables store data values.", dto.getContentText());
        assertEquals("Key concept", dto.getExplanation());
        assertNull(dto.getCodeSnippet());
        assertNull(dto.getQuizQuestion());
    }

    @Test
    void fromEntity_codeSlide() {
        LessonSlide slide = LessonSlide.builder()
                .id(2L)
                .orderIndex(1)
                .contentType(LessonSlide.ContentType.CODE)
                .contentText("Example of a for loop")
                .codeSnippet("for i in range(10):\n    print(i)")
                .codeLanguage("python")
                .build();

        LessonSlideDto dto = LessonSlideDto.fromEntity(slide);

        assertEquals("CODE", dto.getContentType());
        assertEquals("for i in range(10):\n    print(i)", dto.getCodeSnippet());
        assertEquals("python", dto.getCodeLanguage());
    }

    @Test
    void fromEntity_qcmSlideWithQuiz() {
        QuizQuestion quiz = QuizQuestion.builder()
                .id(10L)
                .questionText("What is 2+2?")
                .option1("3")
                .option2("4")
                .option3("5")
                .option4("6")
                .correctOption(2)
                .explanation("Basic addition")
                .build();

        LessonSlide slide = LessonSlide.builder()
                .id(3L)
                .orderIndex(2)
                .contentType(LessonSlide.ContentType.QCM)
                .contentText("Test your knowledge")
                .quizQuestion(quiz)
                .build();

        LessonSlideDto dto = LessonSlideDto.fromEntity(slide);

        assertEquals("QCM", dto.getContentType());
        assertNotNull(dto.getQuizQuestion());
        assertEquals(10L, dto.getQuizQuestion().getId());
        assertEquals("What is Java?", dto.getQuizQuestion().getQuestion());
        assertEquals("A programming language", dto.getQuizQuestion().getCorrectAnswer());
        assertEquals("A snake", dto.getQuizQuestion().getWrongAnswer1());
        assertEquals("A planet", dto.getQuizQuestion().getWrongAnswer2());
        assertEquals("A car", dto.getQuizQuestion().getWrongAnswer3());
        assertEquals("Basic addition", dto.getQuizQuestion().getExplanation());
    }

    @Test
    void fromEntity_nullQuizQuestion_givesNullInDto() {
        LessonSlide slide = LessonSlide.builder()
                .id(4L)
                .orderIndex(0)
                .contentType(LessonSlide.ContentType.TEXT)
                .contentText("Plain text")
                .build();

        LessonSlideDto dto = LessonSlideDto.fromEntity(slide);

        assertNull(dto.getQuizQuestion());
    }
}
