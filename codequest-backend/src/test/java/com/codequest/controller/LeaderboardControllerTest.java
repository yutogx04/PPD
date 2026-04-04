package com.codequest.controller;

import com.codequest.dto.LeaderboardEntry;
import com.codequest.service.LeaderboardService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LeaderboardControllerTest {

    @Mock
    private LeaderboardService leaderboardService;

    @InjectMocks
    private LeaderboardController leaderboardController;

    private UsernamePasswordAuthenticationToken auth() {
        return new UsernamePasswordAuthenticationToken(1L, null, List.of());
    }

    @Test
    void getGlobalLeaderboard_returnsOk() {
        LeaderboardEntry entry = LeaderboardEntry.builder()
                .rank(1).pseudo("TopPlayer").xp(1000).level(5).build();
        when(leaderboardService.getGlobalLeaderboard(1L)).thenReturn(List.of(entry));

        ResponseEntity<List<LeaderboardEntry>> response =
                leaderboardController.getGlobalLeaderboard(auth());

        assertEquals(200, response.getStatusCode().value());
        assertEquals("TopPlayer", response.getBody().get(0).getPseudo());
        assertEquals(1000, response.getBody().get(0).getXp());
    }

    @Test
    void getWeeklyLeaderboard_returnsOk() {
        when(leaderboardService.getWeeklyLeaderboard(1L)).thenReturn(List.of());

        ResponseEntity<List<LeaderboardEntry>> response =
                leaderboardController.getWeeklyLeaderboard(auth());

        assertEquals(200, response.getStatusCode().value());
        assertTrue(response.getBody().isEmpty());
    }

    @Test
    void getFriendsLeaderboard_returnsOk() {
        when(leaderboardService.getFriendsLeaderboard(1L)).thenReturn(List.of());

        ResponseEntity<List<LeaderboardEntry>> response =
                leaderboardController.getFriendsLeaderboard(auth());

        assertEquals(200, response.getStatusCode().value());
        assertTrue(response.getBody().isEmpty());
    }
}
