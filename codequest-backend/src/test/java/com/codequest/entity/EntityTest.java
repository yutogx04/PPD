package com.codequest.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EntityTest {

    @Test
    void user_builderDefaults() {
        User user = User.builder().build();
        assertEquals(0, user.getXp());
        assertEquals(1, user.getLevel());
        assertEquals(0, user.getStreak());
        assertFalse(user.isEmailVerified());
        assertEquals(User.Role.USER, user.getRole());
        assertTrue(user.isEnabled());
    }

    @Test
    void user_builderWithValues() {
        User user = User.builder()
                .id(1L)
                .email("test@test.com")
                .pseudo("tester")
                .password("hashed")
                .xp(500)
                .level(3)
                .streak(7)
                .emailVerified(true)
                .role(User.Role.ADMIN)
                .enabled(false)
                .build();
        assertEquals(1L, user.getId());
        assertEquals("test@test.com", user.getEmail());
        assertEquals("tester", user.getPseudo());
        assertEquals("hashed", user.getPassword());
        assertEquals(500, user.getXp());
        assertEquals(3, user.getLevel());
        assertEquals(7, user.getStreak());
        assertTrue(user.isEmailVerified());
        assertEquals(User.Role.ADMIN, user.getRole());
        assertFalse(user.isEnabled());
    }

    @Test
    void user_roleEnum_hasUserAndAdmin() {
        assertEquals(2, User.Role.values().length);
        assertNotNull(User.Role.valueOf("USER"));
        assertNotNull(User.Role.valueOf("ADMIN"));
    }

    @Test
    void user_settersWork() {
        User user = new User();
        user.setId(5L);
        user.setEmail("a@b.com");
        user.setPseudo("abc");
        user.setXp(100);
        user.setLevel(2);
        user.setStreak(3);
        assertEquals(5L, user.getId());
        assertEquals("a@b.com", user.getEmail());
        assertEquals("abc", user.getPseudo());
        assertEquals(100, user.getXp());
        assertEquals(2, user.getLevel());
        assertEquals(3, user.getStreak());
    }

    @Test
    void badge_conditionType_has8Values() {
        assertEquals(8, Badge.ConditionType.values().length);
    }

    @Test
    void badge_conditionType_allValuesExist() {
        assertNotNull(Badge.ConditionType.valueOf("TOTAL_LESSONS"));
        assertNotNull(Badge.ConditionType.valueOf("TOTAL_CHALLENGES"));
        assertNotNull(Badge.ConditionType.valueOf("FIRST_ATTEMPT_SUCCESS"));
        assertNotNull(Badge.ConditionType.valueOf("STREAK_DAYS"));
        assertNotNull(Badge.ConditionType.valueOf("TOTAL_TRACKS"));
        assertNotNull(Badge.ConditionType.valueOf("NIGHT_SUBMIT"));
        assertNotNull(Badge.ConditionType.valueOf("TRACK_COMPLETE"));
        assertNotNull(Badge.ConditionType.valueOf("ACHIEVE_LEVEL"));
    }

    @Test
    void badge_builder() {
        Badge badge = Badge.builder()
                .id(1L)
                .name("Premier Pas")
                .description("Complete first lesson")
                .icon("🎯")
                .conditionType(Badge.ConditionType.TOTAL_LESSONS)
                .conditionValue(1)
                .build();
        assertEquals("Premier Pas", badge.getName());
        assertEquals(Badge.ConditionType.TOTAL_LESSONS, badge.getConditionType());
        assertEquals(1, badge.getConditionValue());
    }

    @Test
    void track_difficulty_has3Values() {
        assertEquals(3, Track.Difficulty.values().length);
        assertNotNull(Track.Difficulty.valueOf("BEGINNER"));
        assertNotNull(Track.Difficulty.valueOf("INTERMEDIATE"));
        assertNotNull(Track.Difficulty.valueOf("ADVANCED"));
    }

    @Test
    void track_language_has3Values() {
        assertEquals(3, Track.Language.values().length);
        assertNotNull(Track.Language.valueOf("PYTHON"));
        assertNotNull(Track.Language.valueOf("JAVASCRIPT"));
        assertNotNull(Track.Language.valueOf("JAVA"));
    }

    @Test
    void track_builderDefaults() {
        Track track = Track.builder().build();
        assertEquals(1, track.getRequiredLevel());
        assertEquals(20, track.getXpPerLesson());
    }

    @Test
    void track_builderWithValues() {
        Track track = Track.builder()
                .id(1L)
                .title("Python Basics")
                .difficulty(Track.Difficulty.BEGINNER)
                .language(Track.Language.PYTHON)
                .requiredLevel(3)
                .xpPerLesson(50)
                .build();
        assertEquals("Python Basics", track.getTitle());
        assertEquals(Track.Difficulty.BEGINNER, track.getDifficulty());
        assertEquals(Track.Language.PYTHON, track.getLanguage());
        assertEquals(3, track.getRequiredLevel());
        assertEquals(50, track.getXpPerLesson());
    }

    @Test
    void lesson_builderDefaults() {
        Lesson lesson = Lesson.builder().build();
        assertEquals(Lesson.LessonType.THEORY, lesson.getType());
        assertEquals(5, lesson.getDurationMinutes());
        assertEquals(20, lesson.getXpReward());
    }

    @Test
    void lesson_lessonType_has2Values() {
        assertEquals(2, Lesson.LessonType.values().length);
        assertNotNull(Lesson.LessonType.valueOf("THEORY"));
        assertNotNull(Lesson.LessonType.valueOf("PRACTICE"));
    }

    @Test
    void lessonSlide_contentType_has3Values() {
        assertEquals(3, LessonSlide.ContentType.values().length);
        assertNotNull(LessonSlide.ContentType.valueOf("TEXT"));
        assertNotNull(LessonSlide.ContentType.valueOf("CODE"));
        assertNotNull(LessonSlide.ContentType.valueOf("QCM"));
    }

    @Test
    void submission_status_has6Values() {
        assertEquals(6, Submission.Status.values().length);
        assertNotNull(Submission.Status.valueOf("ACCEPTED"));
        assertNotNull(Submission.Status.valueOf("WRONG_ANSWER"));
        assertNotNull(Submission.Status.valueOf("RUNTIME_ERROR"));
        assertNotNull(Submission.Status.valueOf("COMPILE_ERROR"));
        assertNotNull(Submission.Status.valueOf("TIMEOUT"));
        assertNotNull(Submission.Status.valueOf("MEMORY_LIMIT"));
    }

    @Test
    void submission_builderDefaults() {
        Submission submission = Submission.builder().build();
        assertEquals(0, submission.getScore());
        assertEquals(0, submission.getXpGained());
        assertEquals(0, submission.getBonusXp());
    }

    @Test
    void friendship_status_has3Values() {
        assertEquals(3, Friendship.FriendshipStatus.values().length);
        assertNotNull(Friendship.FriendshipStatus.valueOf("PENDING"));
        assertNotNull(Friendship.FriendshipStatus.valueOf("ACCEPTED"));
        assertNotNull(Friendship.FriendshipStatus.valueOf("REJECTED"));
    }

    @Test
    void friendship_builderDefault() {
        Friendship friendship = Friendship.builder().build();
        assertEquals(Friendship.FriendshipStatus.PENDING, friendship.getStatus());
    }

    @Test
    void testCase_builderDefault() {
        TestCase testCase = TestCase.builder().build();
        assertFalse(testCase.isHidden());
    }

    @Test
    void testCase_builderWithValues() {
        TestCase testCase = TestCase.builder()
                .input("5")
                .expectedOutput("25")
                .hidden(true)
                .build();
        assertEquals("5", testCase.getInput());
        assertEquals("25", testCase.getExpectedOutput());
        assertTrue(testCase.isHidden());
    }

    @Test
    void userProgress_builderDefaults() {
        UserProgress progress = UserProgress.builder().build();
        assertEquals(0, progress.getSlideIndex());
        assertFalse(progress.isCompleted());
    }

    @Test
    void module_builderWithValues() {
        Module module = Module.builder()
                .id(1L)
                .title("Variables et Types")
                .description("Learn about variables")
                .orderIndex(0)
                .build();
        assertEquals("Variables et Types", module.getTitle());
        assertEquals(0, module.getOrderIndex());
    }

    @Test
    void quizQuestion_builderWithValues() {
        QuizQuestion q = QuizQuestion.builder()
                .id(1L)
                .questionText("What is 2+2?")
                .option1("3")
                .option2("4")
                .option3("5")
                .option4("6")
                .correctOption(2)
                .explanation("Basic math")
                .build();
        assertEquals("What is 2+2?", q.getQuestionText());
        assertEquals(2, q.getCorrectOption());
        assertEquals("4", q.getOption2());
    }

    @Test
    void dailyChallenge_builder() {
        DailyChallenge dc = DailyChallenge.builder()
                .id(1L)
                .build();
        assertEquals(1L, dc.getId());
    }

    @Test
    void favoriteLesson_builder() {
        FavoriteLesson fav = FavoriteLesson.builder()
                .id(1L)
                .build();
        assertEquals(1L, fav.getId());
    }

    @Test
    void fcmToken_builder() {
        FcmToken fcm = FcmToken.builder()
                .token("firebase_token_123")
                .build();
        assertEquals("firebase_token_123", fcm.getToken());
    }
}
