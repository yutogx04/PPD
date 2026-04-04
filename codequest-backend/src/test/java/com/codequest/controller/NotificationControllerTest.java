package com.codequest.controller;

import com.codequest.entity.User;
import com.codequest.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationControllerTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private NotificationController notificationController;

    private UsernamePasswordAuthenticationToken auth() {
        return new UsernamePasswordAuthenticationToken(1L, null, List.of());
    }

    @Test
    void registerFcmToken_returnsOk() {
        User user = User.builder().id(1L).pseudo("testuser").build();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        ResponseEntity<Void> response = notificationController.registerFcmToken(
                Map.of("token", "firebase_token_abc"), auth());

        assertEquals(200, response.getStatusCode().value());
        assertEquals("firebase_token_abc", user.getFcmToken());
        verify(userRepository).save(user);
    }
}
