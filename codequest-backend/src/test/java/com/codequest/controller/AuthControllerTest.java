package com.codequest.controller;

import com.codequest.dto.AuthResponse;
import com.codequest.dto.LoginRequest;
import com.codequest.dto.RegisterRequest;
import com.codequest.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    private AuthResponse mockResponse;

    @BeforeEach
    void setUp() {
        mockResponse = AuthResponse.builder()
                .accessToken("access_token")
                .refreshToken("refresh_token")
                .user(AuthResponse.UserDto.builder().id(1L).pseudo("tester").build())
                .build();
    }

    @Test
    void register_returnsOk() {
        RegisterRequest request = new RegisterRequest();
        request.setEmail("test@test.com");
        request.setPassword("password123");
        request.setPseudo("tester");

        when(authService.register(any(RegisterRequest.class))).thenReturn(mockResponse);

        ResponseEntity<AuthResponse> response = authController.register(request);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("access_token", response.getBody().getAccessToken());
        assertEquals("tester", response.getBody().getUser().getPseudo());
        verify(authService).register(any(RegisterRequest.class));
    }

    @Test
    void login_returnsOk() {
        LoginRequest request = new LoginRequest();
        request.setEmail("test@test.com");
        request.setPassword("password123");

        when(authService.login(any(LoginRequest.class))).thenReturn(mockResponse);

        ResponseEntity<AuthResponse> response = authController.login(request);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        verify(authService).login(any(LoginRequest.class));
    }

    @Test
    void verifyEmail_returnsOk() {
        when(authService.verifyEmail("test@test.com", "123456")).thenReturn(mockResponse);

        ResponseEntity<AuthResponse> response = authController.verifyEmail(
                Map.of("email", "test@test.com", "otpCode", "123456"));

        assertEquals(200, response.getStatusCode().value());
        verify(authService).verifyEmail("test@test.com", "123456");
    }

    @Test
    void refreshToken_returnsOk() {
        when(authService.refreshToken("old_refresh")).thenReturn(mockResponse);

        ResponseEntity<AuthResponse> response = authController.refreshToken(
                Map.of("refreshToken", "old_refresh"));

        assertEquals(200, response.getStatusCode().value());
        verify(authService).refreshToken("old_refresh");
    }

    @Test
    void forgotPassword_returnsOk() {
        doNothing().when(authService).forgotPassword("test@test.com");

        ResponseEntity<Void> response = authController.forgotPassword(
                Map.of("email", "test@test.com"));

        assertEquals(200, response.getStatusCode().value());
        verify(authService).forgotPassword("test@test.com");
    }

    @Test
    void googleSignIn_delegatesToService() {
        when(authService.googleSignIn("google_id_token")).thenReturn(mockResponse);

        ResponseEntity<AuthResponse> response = authController.googleSignIn(
                Map.of("idToken", "google_id_token"));

        assertEquals(200, response.getStatusCode().value());
        verify(authService).googleSignIn("google_id_token");
    }
}
