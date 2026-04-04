package com.codequest.service;

import com.codequest.dto.GamificationResult;
import com.codequest.dto.LessonDto;
import com.codequest.entity.Lesson;
import com.codequest.entity.User;
import com.codequest.entity.UserProgress;
import com.codequest.repository.LessonRepository;
import com.codequest.repository.UserProgressRepository;
import com.codequest.repository.UserRepository;
import com.codequest.repository.FavoriteLessonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LessonServiceTest {

    @Mock
    private LessonRepository lessonRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserProgressRepository userProgressRepository;
    @Mock
    private FavoriteLessonRepository favoriteLessonRepository;
    @Mock
    private GamificationService gamificationService;
    @Mock
    private LeaderboardService leaderboardService;

    @InjectMocks
    private LessonService lessonService;

    private User mockUser;
    private Lesson mockLesson;

    @BeforeEach
    void setUp() {
        mockUser = new User();
        mockUser.setId(1L);

        mockLesson = new Lesson();
        mockLesson.setId(10L);
        mockLesson.setXpReward(50);
        mockLesson.setTitle("Python Basics");
    }

    @Test
    void getLesson_Success() {
        when(lessonRepository.findById(10L)).thenReturn(Optional.of(mockLesson));
        when(userProgressRepository.findByUserIdAndLessonId(1L, 10L)).thenReturn(Optional.empty());

        LessonDto result = lessonService.getLesson(10L, 1L, "fr");

        assertNotNull(result);
        assertEquals("Python Basics", result.getTitle());
        assertFalse(result.isCompleted());
    }

    @Test
    void completeLesson_FirstTime() {
        when(lessonRepository.findById(10L)).thenReturn(Optional.of(mockLesson));
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
        when(userProgressRepository.findByUserIdAndLessonId(1L, 10L)).thenReturn(Optional.empty());

        UserProgress newProgress = new UserProgress();
        newProgress.setCompleted(true);
        when(userProgressRepository.save(any(UserProgress.class))).thenReturn(newProgress);

        GamificationResult gResult = GamificationResult.builder().xpGained(50).newXpTotal(50).newLevel(1).build();
        when(gamificationService.onLessonCompleted(eq(1L), eq(50), any(Lesson.class))).thenReturn(gResult);

        GamificationResult result = lessonService.completeLesson(10L, 1L);

        assertNotNull(result);
        assertEquals(50, result.getXpGained());
        verify(gamificationService, times(1)).onLessonCompleted(eq(1L), eq(50), any(Lesson.class));
        verify(userProgressRepository, times(1)).save(any(UserProgress.class));
    }

    @Test
    void completeLesson_AlreadyCompleted() {

        
        UserProgress existingProgress = new UserProgress();
        existingProgress.setCompleted(true);
        when(userProgressRepository.findByUserIdAndLessonId(1L, 10L)).thenReturn(Optional.of(existingProgress));

        assertThrows(RuntimeException.class, () -> lessonService.completeLesson(10L, 1L));
        verify(gamificationService, never()).onLessonCompleted(anyLong(), anyInt(), any());
    }
}
