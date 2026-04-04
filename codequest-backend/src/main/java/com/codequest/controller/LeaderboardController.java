package com.codequest.controller;

import com.codequest.dto.LeaderboardEntry;
import com.codequest.service.LeaderboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/leaderboard")
@RequiredArgsConstructor
public class LeaderboardController {

    private final LeaderboardService leaderboardService;

    @GetMapping
    public ResponseEntity<List<LeaderboardEntry>> getGlobalLeaderboard(Authentication auth) {
        Long userId = (Long) auth.getPrincipal();
        return ResponseEntity.ok(leaderboardService.getGlobalLeaderboard(userId));
    }

    @GetMapping("/weekly")
    public ResponseEntity<List<LeaderboardEntry>> getWeeklyLeaderboard(Authentication auth) {
        Long userId = (Long) auth.getPrincipal();
        return ResponseEntity.ok(leaderboardService.getWeeklyLeaderboard(userId));
    }

    @GetMapping("/friends")
    public ResponseEntity<List<LeaderboardEntry>> getFriendsLeaderboard(Authentication auth) {
        Long userId = (Long) auth.getPrincipal();
        return ResponseEntity.ok(leaderboardService.getFriendsLeaderboard(userId));
    }
}
