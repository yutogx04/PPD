package com.codequest.dto;

import com.codequest.entity.LessonSlide;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.codequest.dto.TrackDto.l;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class LessonSlideDto {

    private Long id;
    private int orderIndex;
    private String contentType;
    private String contentText;
    private String codeSnippet;
    private String codeLanguage;
    private String explanation;
    private QuizQuestionDto quizQuestion;

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class QuizQuestionDto {
        private Long id;
        private String question;
        private String correctAnswer;
        private String wrongAnswer1;
        private String wrongAnswer2;
        private String wrongAnswer3;
        private String explanation;
    }

    public static LessonSlideDto fromEntity(LessonSlide slide) {
        return fromEntity(slide, "fr");
    }

    public static LessonSlideDto fromEntity(LessonSlide slide, String lang) {
        LessonSlideDto.LessonSlideDtoBuilder builder = LessonSlideDto.builder()
                .id(slide.getId())
                .orderIndex(slide.getOrderIndex())
                .contentType(slide.getContentType().name())
                .contentText(l(slide.getContentText(), slide.getContentTextEn(), lang))
                .codeSnippet(slide.getCodeSnippet())
                .codeLanguage(slide.getCodeLanguage())
                .explanation(l(slide.getExplanation(), slide.getExplanationEn(), lang));

        if (slide.getQuizQuestion() != null) {
            var q = slide.getQuizQuestion();
            String opt1 = l(q.getOption1(), q.getOption1En(), lang);
            String opt2 = l(q.getOption2(), q.getOption2En(), lang);
            String opt3 = l(q.getOption3(), q.getOption3En(), lang);
            String opt4 = l(q.getOption4(), q.getOption4En(), lang);

            String correctAnswer = (q.getCorrectOption() == 1) ? opt1 :
                                   (q.getCorrectOption() == 2) ? opt2 :
                                   (q.getCorrectOption() == 3) ? opt3 : opt4;
            
            java.util.List<String> wrongs = new java.util.ArrayList<>();
            if (q.getCorrectOption() != 1) wrongs.add(opt1);
            if (q.getCorrectOption() != 2) wrongs.add(opt2);
            if (q.getCorrectOption() != 3) wrongs.add(opt3);
            if (q.getCorrectOption() != 4) wrongs.add(opt4);

            builder.quizQuestion(QuizQuestionDto.builder()
                    .id(q.getId())
                    .question(l(q.getQuestionText(), q.getQuestionTextEn(), lang))
                    .correctAnswer(correctAnswer)
                    .wrongAnswer1(wrongs.size() > 0 ? wrongs.get(0) : "Wrong 1")
                    .wrongAnswer2(wrongs.size() > 1 ? wrongs.get(1) : "Wrong 2")
                    .wrongAnswer3(wrongs.size() > 2 ? wrongs.get(2) : "Wrong 3")
                    .explanation(l(q.getExplanation(), q.getExplanationEn(), lang))
                    .build());
        }

        return builder.build();
    }
}
