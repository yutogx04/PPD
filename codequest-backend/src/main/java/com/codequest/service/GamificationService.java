package com.codequest.service;

import com.codequest.dto.GamificationResult;
import com.codequest.entity.Badge;
import com.codequest.entity.Lesson;
import com.codequest.entity.User;
import com.codequest.entity.UserBadge;
import com.codequest.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GamificationService {

    private final UserRepository userRepository;
    private final BadgeRepository badgeRepository;
    private final UserBadgeRepository userBadgeRepository;
    private final UserProgressRepository userProgressRepository;
    private final SubmissionRepository submissionRepository;
    private final LessonRepository lessonRepository;

    private static final int[] LEVEL_THRESHOLDS = {0, 200, 500, 1000, 2000, 4000};
    private static final String[] LEVEL_NAMES = {"Beginner", "Novice", "Apprentice", "Developer", "Expert", "Master"};

    private static final int MODULE_COMPLETION_BONUS_XP = 50;
    private static final int STREAK_7_BONUS_XP = 100;

    @Transactional
    public GamificationResult onLessonCompleted(Long userId, int xpReward, Lesson lesson) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        int totalXp = xpReward;
        int oldLevel = user.getLevel();
        int oldStreak = user.getStreak();

        user.setXp(user.getXp() + xpReward);
        user.setTotalLessonsCompleted(user.getTotalLessonsCompleted() + 1);
        updateStreak(user);

        int moduleBonus = checkModuleCompletion(userId, lesson);
        if (moduleBonus > 0) {
            user.setXp(user.getXp() + moduleBonus);
            totalXp += moduleBonus;
        }

        int streakBonus = checkStreakBonus(user, oldStreak);
        if (streakBonus > 0) {
            user.setXp(user.getXp() + streakBonus);
            totalXp += streakBonus;
        }

        int newLevel = calculateLevel(user.getXp());
        user.setLevel(newLevel);
        userRepository.save(user);

        List<GamificationResult.BadgeDto> unlocked = checkAndAwardBadges(user);

        return GamificationResult.builder()
                .xpGained(totalXp)
                .newXpTotal(user.getXp())
                .newLevel(newLevel)
                .leveledUp(newLevel > oldLevel)
                .badgesUnlocked(unlocked)
                .build();
    }

    @Transactional
    public GamificationResult onChallengeCompleted(Long userId, int xpGained, int bonusXp) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        int totalXp = xpGained + bonusXp;
        int oldLevel = user.getLevel();
        int oldStreak = user.getStreak();

        user.setXp(user.getXp() + totalXp);
        user.setTotalChallengesSolved(user.getTotalChallengesSolved() + 1);
        updateStreak(user);

        int streakBonus = checkStreakBonus(user, oldStreak);
        if (streakBonus > 0) {
            user.setXp(user.getXp() + streakBonus);
            totalXp += streakBonus;
        }

        int newLevel = calculateLevel(user.getXp());
        user.setLevel(newLevel);
        userRepository.save(user);

        List<GamificationResult.BadgeDto> unlocked = checkAndAwardBadges(user);

        return GamificationResult.builder()
                .xpGained(totalXp)
                .newXpTotal(user.getXp())
                .newLevel(newLevel)
                .leveledUp(newLevel > oldLevel)
                .badgesUnlocked(unlocked)
                .build();
    }

    private int checkModuleCompletion(Long userId, Lesson lesson) {
        Long moduleId = lesson.getModule().getId();
        long totalLessonsInModule = lessonRepository.countByModuleId(moduleId);
        long completedInModule = userProgressRepository.countByUserIdAndCompletedTrueAndLessonModuleId(userId, moduleId);

        if (completedInModule >= totalLessonsInModule) {
            return MODULE_COMPLETION_BONUS_XP;
        }
        return 0;
    }

    private int checkStreakBonus(User user, int oldStreak) {
        if (oldStreak < 7 && user.getStreak() >= 7) {
            return STREAK_7_BONUS_XP;
        }
        return 0;
    }

    private void updateStreak(User user) {
        LocalDate today = LocalDate.now();
        LocalDate lastActivity = user.getLastActivityDate();

        if (lastActivity == null || lastActivity.isBefore(today.minusDays(1))) {
            user.setStreak(1);
        } else if (lastActivity.equals(today.minusDays(1))) {
            user.setStreak(user.getStreak() + 1);
        }
        user.setLastActivityDate(today);
    }

    public static int calculateLevel(int xp) {
        for (int i = LEVEL_THRESHOLDS.length - 1; i >= 0; i--) {
            if (xp >= LEVEL_THRESHOLDS[i]) {
                return i + 1;
            }
        }
        return 1;
    }

    public static String getLevelName(int level) {
        if (level >= 1 && level <= LEVEL_NAMES.length) {
            return LEVEL_NAMES[level - 1];
        }
        return LEVEL_NAMES[LEVEL_NAMES.length - 1];
    }

    private List<GamificationResult.BadgeDto> checkAndAwardBadges(User user) {
        List<GamificationResult.BadgeDto> unlocked = new ArrayList<>();
        List<Badge> allBadges = badgeRepository.findAll();

        for (Badge badge : allBadges) {
            if (userBadgeRepository.existsByUserIdAndBadgeId(user.getId(), badge.getId())) {
                continue;
            }

            boolean earned = switch (badge.getConditionType()) {
                case TOTAL_LESSONS -> user.getTotalLessonsCompleted() >= badge.getConditionValue();
                case TOTAL_CHALLENGES -> user.getTotalChallengesSolved() >= badge.getConditionValue();
                case FIRST_ATTEMPT_SUCCESS -> false; 
                case STREAK_DAYS -> user.getStreak() >= badge.getConditionValue();
                case TOTAL_TRACKS -> false; 
                case NIGHT_SUBMIT -> false; 
                case TRACK_COMPLETE -> false; 
                case ACHIEVE_LEVEL -> user.getLevel() >= badge.getConditionValue();
            };

            if (earned) {
                UserBadge userBadge = UserBadge.builder()
                        .user(user)
                        .badge(badge)
                        .build();
                userBadgeRepository.save(userBadge);

                unlocked.add(GamificationResult.BadgeDto.builder()
                        .id(badge.getId())
                        .name(badge.getName())
                        .description(badge.getDescription())
                        .icon(badge.getIcon())
                        .isEarned(true)
                        .obtainedAt(LocalDateTime.now().toString())
                        .build());
            }
        }
        return unlocked;
    }
}
