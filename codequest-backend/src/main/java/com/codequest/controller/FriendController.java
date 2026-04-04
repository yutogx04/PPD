package com.codequest.controller;

import com.codequest.dto.FriendDto;
import com.codequest.service.FriendService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/friends")
@RequiredArgsConstructor
public class FriendController {

    private final FriendService friendService;

    @PostMapping("/request")
    public ResponseEntity<Void> sendFriendRequest(@RequestBody Map<String, Long> body,
                                                    Authentication auth) {
        Long userId = (Long) auth.getPrincipal();
        friendService.sendFriendRequest(userId, body.get("targetUserId"));
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/accept")
    public ResponseEntity<Void> acceptFriend(@PathVariable Long id, Authentication auth) {
        Long userId = (Long) auth.getPrincipal();
        friendService.acceptFriendRequest(id, userId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<Void> rejectFriend(@PathVariable Long id, Authentication auth) {
        Long userId = (Long) auth.getPrincipal();
        friendService.rejectFriendRequest(id, userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<FriendDto>> getFriends(Authentication auth) {
        Long userId = (Long) auth.getPrincipal();
        return ResponseEntity.ok(friendService.getFriends(userId));
    }

    @GetMapping("/requests")
    public ResponseEntity<List<FriendDto>> getFriendRequests(Authentication auth) {
        Long userId = (Long) auth.getPrincipal();
        return ResponseEntity.ok(friendService.getPendingRequests(userId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeFriend(@PathVariable Long id, Authentication auth) {
        Long userId = (Long) auth.getPrincipal();
        friendService.removeFriend(id, userId);
        return ResponseEntity.ok().build();
    }
}
