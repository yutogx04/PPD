package com.codequest.controller;

import com.codequest.dto.FriendDto;
import com.codequest.service.FriendService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FriendControllerTest {

    @Mock
    private FriendService friendService;

    @InjectMocks
    private FriendController friendController;

    private UsernamePasswordAuthenticationToken auth() {
        return new UsernamePasswordAuthenticationToken(1L, null, List.of());
    }

    @Test
    void sendFriendRequest_returnsOk() {
        doNothing().when(friendService).sendFriendRequest(1L, 2L);

        ResponseEntity<Void> response = friendController.sendFriendRequest(
                Map.of("targetUserId", 2L), auth());

        assertEquals(200, response.getStatusCode().value());
        verify(friendService).sendFriendRequest(1L, 2L);
    }

    @Test
    void acceptFriend_returnsOk() {
        doNothing().when(friendService).acceptFriendRequest(5L, 1L);

        ResponseEntity<Void> response = friendController.acceptFriend(5L, auth());

        assertEquals(200, response.getStatusCode().value());
        verify(friendService).acceptFriendRequest(5L, 1L);
    }

    @Test
    void rejectFriend_returnsOk() {
        doNothing().when(friendService).rejectFriendRequest(5L, 1L);

        ResponseEntity<Void> response = friendController.rejectFriend(5L, auth());

        assertEquals(200, response.getStatusCode().value());
        verify(friendService).rejectFriendRequest(5L, 1L);
    }

    @Test
    void getFriends_returnsOk() {
        FriendDto dto = FriendDto.builder().id(2L).pseudo("Ami").build();
        when(friendService.getFriends(1L)).thenReturn(List.of(dto));

        ResponseEntity<List<FriendDto>> response = friendController.getFriends(auth());

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Ami", response.getBody().get(0).getPseudo());
    }

    @Test
    void getFriendRequests_returnsOk() {
        when(friendService.getPendingRequests(1L)).thenReturn(List.of());

        ResponseEntity<List<FriendDto>> response = friendController.getFriendRequests(auth());

        assertEquals(200, response.getStatusCode().value());
        assertTrue(response.getBody().isEmpty());
    }

    @Test
    void removeFriend_returnsOk() {
        doNothing().when(friendService).removeFriend(5L, 1L);

        ResponseEntity<Void> response = friendController.removeFriend(5L, auth());

        assertEquals(200, response.getStatusCode().value());
        verify(friendService).removeFriend(5L, 1L);
    }
}
