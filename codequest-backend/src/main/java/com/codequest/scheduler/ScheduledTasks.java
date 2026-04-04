package com.codequest.scheduler;

import com.codequest.entity.Challenge;
import com.codequest.entity.DailyChallenge;
import com.codequest.entity.User;
import com.codequest.repository.ChallengeRepository;
import com.codequest.repository.DailyChallengeRepository;
import com.codequest.repository.UserRepository;
import com.codequest.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;

@Component
@RequiredArgsConstructor
@Slf4j
public class ScheduledTasks {

    private final DailyChallengeRepository dailyChallengeRepository;
    private final ChallengeRepository challengeRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void selectDailyChallenge() {
        LocalDate today = LocalDate.now();

        if (dailyChallengeRepository.findByDate(today).isPresent()) {
            log.info("Daily challenge already set for {}", today);
            return;
        }

        List<Challenge> allChallenges = challengeRepository.findAll();
        if (allChallenges.isEmpty()) {
            log.warn("No challenges available for daily challenge selection");
            return;
        }

        Challenge selected = allChallenges.get(new Random().nextInt(allChallenges.size()));
        DailyChallenge daily = DailyChallenge.builder()
                .challenge(selected)
                .date(today)
                .build();
        dailyChallengeRepository.save(daily);
        log.info("Daily challenge set: '{}' (id={}) for {}", selected.getTitle(), selected.getId(), today);
    }

    @Scheduled(cron = "0 1 0 * * *")
    @Transactional
    public void resetBrokenStreaks() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        List<User> allUsers = userRepository.findAll();

        int resetCount = 0;
        for (User user : allUsers) {
            if (user.getStreak() > 0 && user.getLastActivityDate() != null
                    && user.getLastActivityDate().isBefore(yesterday)) {
                user.setStreak(0);
                userRepository.save(user);
                resetCount++;
            }
        }
        log.info("Reset {} broken streaks", resetCount);
    }

    @Scheduled(cron = "0 0 9 * * *")
    public void sendDailyReminder() {
        List<User> users = userRepository.findAll();
        int sent = 0;
        for (User user : users) {
            if (user.getFcmToken() != null && !user.getFcmToken().isEmpty()) {
                notificationService.sendToUser(user,
                        "🎯 Rappel quotidien",
                        "N'oubliez pas de coder aujourd'hui pour maintenir votre streak !",
                        "daily_reminder");
                sent++;
            }
        }
        log.info("Sent daily reminder to {} users", sent);
    }

    @Scheduled(cron = "0 0 20 * * *")
    public void sendStreakAlert() {
        LocalDate today = LocalDate.now();
        List<User> users = userRepository.findAll();
        int sent = 0;
        for (User user : users) {
            if (user.getStreak() > 0
                    && user.getFcmToken() != null && !user.getFcmToken().isEmpty()
                    && (user.getLastActivityDate() == null || user.getLastActivityDate().isBefore(today))) {
                notificationService.sendToUser(user,
                        "🔥 Streak en danger !",
                        "Vous avez un streak de " + user.getStreak() + " jours. Complétez une leçon pour le garder !",
                        "streak_alert");
                sent++;
            }
        }
        log.info("Sent streak alert to {} users", sent);
    }
}
