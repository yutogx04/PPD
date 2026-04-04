package com.codequest.config;

import com.codequest.entity.*;
import com.codequest.entity.Module;
import com.codequest.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SeederHelper {

    final ModuleRepository moduleRepository;
    final LessonRepository lessonRepository;
    final LessonSlideRepository lessonSlideRepository;
    final QuizQuestionRepository quizQuestionRepository;
    final ChallengeRepository challengeRepository;
    final TestCaseRepository testCaseRepository;

    public Module createModule(Track track, int order, String title, String titleEn,
                               String description, String descriptionEn) {
        return moduleRepository.save(Module.builder()
                .title(title).titleEn(titleEn)
                .description(description).descriptionEn(descriptionEn)
                .orderIndex(order).track(track).build());
    }

    public void seedLesson(Module module, int order, String title, String titleEn,
                           Lesson.LessonType type,
                           String text, String textEn,
                           String code, String lang,
                           String explanation, String explanationEn) {
        Lesson lesson = lessonRepository.save(Lesson.builder()
                .title(title).titleEn(titleEn).type(type).durationMinutes(5)
                .xpReward(module.getTrack().getXpPerLesson())
                .orderIndex(order).module(module).build());

        if (text != null) {
            lessonSlideRepository.save(LessonSlide.builder()
                    .orderIndex(0).contentType(LessonSlide.ContentType.TEXT)
                    .contentText(text).contentTextEn(textEn)
                    .lesson(lesson).build());
        }
        if (code != null) {
            lessonSlideRepository.save(LessonSlide.builder()
                    .orderIndex(1).contentType(LessonSlide.ContentType.CODE)
                    .codeSnippet(code).codeLanguage(lang)
                    .explanation(explanation).explanationEn(explanationEn)
                    .lesson(lesson).build());
        }
    }

    public void seedQuiz(Module module, int order, String quizTitle, String quizTitleEn,
                         String[][] questions) {
        Lesson lesson = lessonRepository.save(Lesson.builder()
                .title(quizTitle).titleEn(quizTitleEn).type(Lesson.LessonType.THEORY)
                .durationMinutes(3).xpReward(module.getTrack().getXpPerLesson())
                .orderIndex(order).module(module).build());

        for (int i = 0; i < questions.length; i++) {
            String[] q = questions[i];
            LessonSlide quizSlide = lessonSlideRepository.save(LessonSlide.builder()
                    .orderIndex(i).contentType(LessonSlide.ContentType.QCM)
                    .lesson(lesson).build());

            quizQuestionRepository.save(QuizQuestion.builder()
                    .questionText(q[0]).questionTextEn(q[1])
                    .option1(q[2]).option1En(q[3])
                    .option2(q[4]).option2En(q[5])
                    .option3(q[6]).option3En(q[7])
                    .option4(q[8]).option4En(q[9])
                    .correctOption(Integer.parseInt(q[10]))
                    .explanation(q[11]).explanationEn(q[12])
                    .slide(quizSlide).build());
        }
    }

    public void seedChallenge(Module module, String title, String titleEn,
                              String desc, String descEn,
                              String language, String difficulty, String starter,
                              String solution, String in1, String out1,
                              String in2, String out2, int xp) {
        Challenge challenge = challengeRepository.save(Challenge.builder()
                .title(title).titleEn(titleEn)
                .description(desc).descriptionEn(descEn)
                .difficulty(Track.Difficulty.valueOf(difficulty))
                .language(Track.Language.valueOf(language))
                .starterCode(starter).solution(solution).referenceSolution(solution)
                .hint("Essayez de décomposer le problème en étapes simples.")
                .hintEn("Try to break the problem down into simple steps.")
                .xpReward(xp)
                .exampleInput(in1).exampleOutput(out1)
                .exampleInput2(in2).exampleOutput2(out2)
                .referenceTimeMs(100).referenceMemoryKb(8192)
                .module(module).build());

        testCaseRepository.save(TestCase.builder().input(in1).expectedOutput(out1).hidden(false).challenge(challenge).build());
        if (in2 != null && !in2.isEmpty()) {
            testCaseRepository.save(TestCase.builder().input(in2).expectedOutput(out2).hidden(false).challenge(challenge).build());
        }

        if (in2 != null && !in2.isEmpty()) {
            testCaseRepository.save(TestCase.builder().input(in2).expectedOutput(out2).hidden(true).challenge(challenge).build());
        } else {
            testCaseRepository.save(TestCase.builder().input(in1).expectedOutput(out1).hidden(true).challenge(challenge).build());
        }
    }
}
