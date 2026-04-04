package com.codequest.service;

import com.codequest.dto.GamificationResult;
import com.codequest.dto.SubmissionResponse;
import com.codequest.entity.*;
import com.codequest.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SubmissionService {

    private final SubmissionRepository submissionRepository;
    private final ChallengeRepository challengeRepository;
    private final TestCaseRepository testCaseRepository;
    private final DailyChallengeRepository dailyChallengeRepository;
    private final UserRepository userRepository;
    private final CodeRunnerService codeRunnerService;
    private final ScoreCalculator scoreCalculator;
    private final GamificationService gamificationService;
    private final BadgeRepository badgeRepository;
    private final UserBadgeRepository userBadgeRepository;

    public SubmissionResponse runCode(Long challengeId, String code, String language, Long userId) {
        Challenge challenge = challengeRepository.findById(challengeId)
                .orElseThrow(() -> new RuntimeException("Défi non trouvé"));

        List<TestCase> visibleTests = testCaseRepository.findByChallengeIdAndHiddenFalse(challengeId);
        return executeAndEvaluate(challenge, code, language, visibleTests, userId, false);
    }

    @Transactional
    public SubmissionResponse submitCode(Long challengeId, String code, String language, Long userId) {
        Challenge challenge = challengeRepository.findById(challengeId)
                .orElseThrow(() -> new RuntimeException("Défi non trouvé"));

        checkRateLimit(userId, challengeId);

        List<TestCase> allTests = testCaseRepository.findByChallengeId(challengeId);
        SubmissionResponse response = executeAndEvaluate(challenge, code, language, allTests, userId, true);

        return response;
    }

    private void checkRateLimit(Long userId, Long challengeId) {
        LocalDateTime tenSecondsAgo = LocalDateTime.now().minusSeconds(10);
        if (submissionRepository.existsByUserIdAndChallengeIdAndCreatedAtAfter(userId, challengeId, tenSecondsAgo)) {
            throw new RuntimeException("Veuillez attendre 10 secondes entre chaque soumission");
        }
    }

    private SubmissionResponse executeAndEvaluate(Challenge challenge, String code, String language,
                                                   List<TestCase> testCases, Long userId, boolean isSubmission) {
        int passed = 0;
        List<SubmissionResponse.TestCaseResultDto> testResults = new ArrayList<>();

        for (int i = 0; i < testCases.size(); i++) {
            TestCase tc = testCases.get(i);
            CodeRunnerService.ExecutionResult result = codeRunnerService.execute(code, language, tc.getInput());

            if (result.isTimedOut()) {
                return buildErrorResponse(code, language, challenge, userId, isSubmission,
                        Submission.Status.TIMEOUT, "Temps d'exécution dépassé", passed, testCases.size(), testResults);
            }

            if (result.getExitCode() != 0) {
                String errorType = result.getStderr().contains("SyntaxError") || result.getStderr().contains("IndentationError")
                        ? "COMPILE_ERROR" : "RUNTIME_ERROR";
                Submission.Status status = "COMPILE_ERROR".equals(errorType)
                        ? Submission.Status.COMPILE_ERROR : Submission.Status.RUNTIME_ERROR;
                return buildErrorResponse(code, language, challenge, userId, isSubmission,
                        status, result.getStderr(), passed, testCases.size(), testResults);
            }

            String actual = result.getStdout().trim();
            String expected = tc.getExpectedOutput().trim();
            boolean testPassed = actual.equals(expected);

            if (testPassed) passed++;

            testResults.add(SubmissionResponse.TestCaseResultDto.builder()
                    .input(tc.getInput())
                    .passed(testPassed)
                    .expectedOutput(tc.isHidden() ? "[caché]" : expected)
                    .actualOutput(tc.isHidden() ? "[caché]" : actual)
                    .timeMs(0)
                    .build());
        }

        boolean allPassed = (passed == testCases.size());
        Submission.Status status = allPassed ? Submission.Status.ACCEPTED : Submission.Status.WRONG_ANSWER;

        if (!isSubmission) {
            return SubmissionResponse.builder()
                    .status(status.name())
                    .output(testResults.isEmpty() ? "" : testResults.get(0).getActualOutput())
                    .testCasesPassed(passed)
                    .testCasesTotal(testCases.size())
                    .testResults(testResults)
                    .build();
        }

        int attemptNumber = (int) submissionRepository.countByUserIdAndChallengeId(userId, challenge.getId()) + 1;
        long avgExecutionTime = calculateAvgExecutionTime(code, language, testCases);

        int score = 0;
        String grade = "D";
        int xpGained = 0;
        int bonusXp = 0;
        List<GamificationResult.BadgeDto> badgesUnlocked = new ArrayList<>();

        if (allPassed) {
            score = scoreCalculator.calculateScore(
                    attemptNumber, avgExecutionTime, 0,
                    challenge.getReferenceTimeMs(), challenge.getReferenceMemoryKb()
            );
            grade = scoreCalculator.calculateGrade(score);
            xpGained = scoreCalculator.getXpForDifficulty(challenge.getDifficulty().name());

            boolean isDaily = dailyChallengeRepository
                    .findByDate(java.time.LocalDate.now())
                    .map(dc -> dc.getChallenge().getId().equals(challenge.getId()))
                    .orElse(false);
            bonusXp = scoreCalculator.calculateBonusXp(attemptNumber, isDaily);

            GamificationResult gamificationResult = gamificationService.onChallengeCompleted(userId, xpGained, bonusXp);
            if (gamificationResult.getBadgesUnlocked() != null) {
                badgesUnlocked.addAll(gamificationResult.getBadgesUnlocked());
            }

            badgesUnlocked.addAll(checkSubmissionBadges(userId, attemptNumber));
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        Submission submission = Submission.builder()
                .code(code)
                .language(Track.Language.valueOf(language.toUpperCase()))
                .status(status)
                .score(score)
                .grade(grade)
                .xpGained(xpGained)
                .bonusXp(bonusXp)
                .executionTimeMs(avgExecutionTime)
                .testCasesPassed(passed)
                .testCasesTotal(testCases.size())
                .user(user)
                .challenge(challenge)
                .build();
        submissionRepository.save(submission);

        return SubmissionResponse.builder()
                .status(status.name())
                .output(testResults.isEmpty() ? "" : testResults.get(0).getActualOutput())
                .testCasesPassed(passed)
                .testCasesTotal(testCases.size())
                .score(score)
                .grade(grade)
                .xpGained(xpGained)
                .bonusXp(bonusXp)
                .badgesUnlocked(badgesUnlocked.isEmpty() ? null : badgesUnlocked)
                .testResults(testResults)
                .build();
    }

    private SubmissionResponse buildErrorResponse(String code, String language, Challenge challenge,
                                                   Long userId, boolean isSubmission,
                                                   Submission.Status status, String errorMessage,
                                                   int passed, int total,
                                                   List<SubmissionResponse.TestCaseResultDto> testResults) {
        if (isSubmission) {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

            Submission submission = Submission.builder()
                    .code(code)
                    .language(Track.Language.valueOf(language.toUpperCase()))
                    .status(status)
                    .errorMessage(errorMessage)
                    .testCasesPassed(passed)
                    .testCasesTotal(total)
                    .user(user)
                    .challenge(challenge)
                    .build();
            submissionRepository.save(submission);
        }

        return SubmissionResponse.builder()
                .status(status.name())
                .errorMessage(errorMessage)
                .testCasesPassed(passed)
                .testCasesTotal(total)
                .testResults(testResults)
                .build();
    }

    private long calculateAvgExecutionTime(String code, String language, List<TestCase> testCases) {
        return 0;
    }

    private List<GamificationResult.BadgeDto> checkSubmissionBadges(Long userId, int attemptNumber) {
        List<GamificationResult.BadgeDto> unlocked = new ArrayList<>();
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) return unlocked;

        badgeRepository.findAll().forEach(badge -> {
            if (userBadgeRepository.existsByUserIdAndBadgeId(userId, badge.getId())) return;

            boolean earned = switch (badge.getConditionType()) {
                case FIRST_ATTEMPT_SUCCESS -> attemptNumber == 1;
                case NIGHT_SUBMIT -> {
                    LocalTime now = LocalTime.now();
                    yield now.isAfter(LocalTime.MIDNIGHT) && now.isBefore(LocalTime.of(5, 0));
                }
                default -> false;
            };

            if (earned) {
                userBadgeRepository.save(UserBadge.builder()
                        .user(user)
                        .badge(badge)
                        .build());
                log.info("Badge '{}' awarded to user {}", badge.getName(), userId);
                unlocked.add(GamificationResult.BadgeDto.builder()
                        .id(badge.getId())
                        .name(badge.getName())
                        .description(badge.getDescription())
                        .icon(badge.getIcon())
                        .isEarned(true)
                        .obtainedAt(LocalDateTime.now().toString())
                        .build());
            }
        });
        return unlocked;
    }
}
