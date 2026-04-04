package com.codequest.service;

import com.codequest.dto.GamificationResult;
import com.codequest.dto.SubmissionResponse;
import com.codequest.entity.Challenge;
import com.codequest.entity.Submission;
import com.codequest.entity.TestCase;
import com.codequest.entity.Track;
import com.codequest.entity.User;
import com.codequest.repository.ChallengeRepository;
import com.codequest.repository.DailyChallengeRepository;
import com.codequest.repository.SubmissionRepository;
import com.codequest.repository.TestCaseRepository;
import com.codequest.repository.UserRepository;
import com.codequest.repository.BadgeRepository;
import com.codequest.repository.UserBadgeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SubmissionServiceTest {

    @Mock
    private CodeRunnerService codeRunnerService;
    @Mock
    private ChallengeRepository challengeRepository;
    @Mock
    private SubmissionRepository submissionRepository;
    @Mock
    private TestCaseRepository testCaseRepository;
    @Mock
    private DailyChallengeRepository dailyChallengeRepository;
    @Mock
    private BadgeRepository badgeRepository;
    @Mock
    private UserBadgeRepository userBadgeRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ScoreCalculator scoreCalculator;
    @Mock
    private GamificationService gamificationService;
    @Mock
    private LeaderboardService leaderboardService;

    @InjectMocks
    private SubmissionService submissionService;

    private User mockUser;
    private Challenge mockChallenge;

    @BeforeEach
    void setUp() {
        mockUser = new User();
        mockUser.setId(1L);
        mockUser.setXp(100);

        mockChallenge = new Challenge();
        mockChallenge.setId(10L);
        mockChallenge.setDifficulty(Track.Difficulty.BEGINNER);
        mockChallenge.setReferenceTimeMs(100L);
        mockChallenge.setReferenceMemoryKb(50L);
    }

    @Test
    void submitCode_Success_AllTestsPass() throws Exception {
        when(challengeRepository.findById(10L)).thenReturn(Optional.of(mockChallenge));
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
        when(submissionRepository.existsByUserIdAndChallengeIdAndCreatedAtAfter(eq(1L), eq(10L), any())).thenReturn(false);

        TestCase mockTestCase = new TestCase();
        mockTestCase.setInput("input");
        mockTestCase.setExpectedOutput("Hello");
        mockTestCase.setHidden(false);

        when(testCaseRepository.findByChallengeId(10L)).thenReturn(Collections.singletonList(mockTestCase));

        CodeRunnerService.ExecutionResult mockRunResult = CodeRunnerService.ExecutionResult.builder()
                .stdout("Hello")
                .stderr("")
                .exitCode(0)
                .executionTimeMs(50L)
                .timedOut(false)
                .build();
        when(codeRunnerService.execute(anyString(), anyString(), anyString())).thenReturn(mockRunResult);

        when(scoreCalculator.calculateScore(anyInt(), anyLong(), anyLong(), anyLong(), anyLong())).thenReturn(100);
        when(scoreCalculator.calculateGrade(100)).thenReturn("S");
        when(scoreCalculator.getXpForDifficulty(any())).thenReturn(30);
        when(scoreCalculator.calculateBonusXp(anyInt(), anyBoolean())).thenReturn(25);

        GamificationResult gamificationResult = GamificationResult.builder()
                .xpGained(30)
                .newXpTotal(155)
                .newLevel(2)
                .leveledUp(true)
                .badgesUnlocked(Collections.emptyList())
                .build();
        
        when(gamificationService.onChallengeCompleted(anyLong(), anyInt(), anyInt())).thenReturn(gamificationResult);
        when(submissionRepository.save(any(Submission.class))).thenAnswer(i -> i.getArgument(0));

        SubmissionResponse response = submissionService.submitCode(10L, "print('Hello')", "PYTHON", 1L);

        assertNotNull(response);
        assertEquals("S", response.getGrade());
        assertEquals(100, response.getScore());
        assertEquals(30, response.getXpGained());
        assertEquals(25, response.getBonusXp());
        assertEquals("ACCEPTED", response.getStatus());

        verify(gamificationService, times(1)).onChallengeCompleted(1L, 30, 25);
    }
}
