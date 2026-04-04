package com.codequest.service;

import com.codequest.dto.LeaderboardEntry;
import com.codequest.entity.User;
import com.codequest.repository.FriendshipRepository;
import com.codequest.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class LeaderboardService {

    private final UserRepository userRepository;
    private final FriendshipRepository friendshipRepository;
    private final StringRedisTemplate redisTemplate;

    private static final String GLOBAL_KEY = "leaderboard:global";
    private static final String WEEKLY_KEY_PREFIX = "leaderboard:weekly:";

    public List<LeaderboardEntry> getGlobalLeaderboard(Long currentUserId) {
        List<User> users = userRepository.findAllByOrderByXpDesc();
        return buildLeaderboard(users, currentUserId);
    }

    public List<LeaderboardEntry> getWeeklyLeaderboard(Long currentUserId) {
        return getGlobalLeaderboard(currentUserId);
    }

    public List<LeaderboardEntry> getFriendsLeaderboard(Long userId) {
        List<Long> friendIds = friendshipRepository.findAcceptedFriendships(userId).stream()
                .map(f -> f.getSender().getId().equals(userId)
                        ? f.getReceiver().getId()
                        : f.getSender().getId())
                .collect(Collectors.toList());

        friendIds.add(userId);

        List<User> friends = userRepository.findAllById(friendIds);
        friends.sort((a, b) -> Integer.compare(b.getXp(), a.getXp()));

        return buildLeaderboard(friends, userId);
    }

    public void updateUserScore(Long userId, int xp) {
        try {
            redisTemplate.opsForZSet().add(GLOBAL_KEY, userId.toString(), xp);

            String weekKey = WEEKLY_KEY_PREFIX + getCurrentWeekKey();
            redisTemplate.opsForZSet().incrementScore(weekKey, userId.toString(), xp);
            redisTemplate.expire(weekKey, 8, TimeUnit.DAYS);
        } catch (Exception e) {
            log.warn("Failed to update Redis leaderboard: {}", e.getMessage());
        }
    }

    private List<LeaderboardEntry> buildLeaderboard(List<User> users, Long currentUserId) {
        List<LeaderboardEntry> entries = new ArrayList<>();
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            entries.add(LeaderboardEntry.builder()
                    .rank(i + 1)
                    .pseudo(user.getPseudo())
                    .avatarUrl(user.getAvatarUrl())
                    .xp(user.getXp())
                    .level(user.getLevel())
                    .isCurrentUser(user.getId().equals(currentUserId))
                    .build());
        }
        return entries;
    }

    private String getCurrentWeekKey() {
        LocalDate monday = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        return monday.toString();
    }
}
