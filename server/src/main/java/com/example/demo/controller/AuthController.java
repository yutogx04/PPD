package com.example.demo.controller;

import com.example.demo.dto.AuthDTOs.*;
import com.example.demo.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Contrôleur REST pour tous les endpoints d'authentification.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // ─── POST /api/auth/register ─────────────────────────────

    @PostMapping("/register")
    public ResponseEntity<MessageResponse> register(@Valid @RequestBody RegisterRequest request) {
        MessageResponse response = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // ─── POST /api/auth/verify-otp ───────────────────────────

    @PostMapping("/verify-otp")
    public ResponseEntity<AuthResponse> verifyOtp(@Valid @RequestBody OtpRequest request) {
        AuthResponse response = authService.verifyOtp(request);
        return ResponseEntity.ok(response);
    }

    // ─── POST /api/auth/login ────────────────────────────────

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request,
                                              HttpServletRequest httpRequest) {
        // [POURQUOI] On extrait l'IP pour le rate limiting
        String clientIp = extractClientIp(httpRequest);
        AuthResponse response = authService.login(request, clientIp);
        return ResponseEntity.ok(response);
    }

    // ─── POST /api/auth/google ───────────────────────────────

    @PostMapping("/google")
    public ResponseEntity<AuthResponse> googleAuth(@Valid @RequestBody GoogleAuthRequest request) {
        AuthResponse response = authService.googleAuth(request);
        return ResponseEntity.ok(response);
    }

    // ─── POST /api/auth/refresh ──────────────────────────────

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refreshToken(@Valid @RequestBody RefreshRequest request) {
        TokenResponse response = authService.refreshToken(request);
        return ResponseEntity.ok(response);
    }

    // ─── POST /api/auth/forgot-password ──────────────────────

    @PostMapping("/forgot-password")
    public ResponseEntity<MessageResponse> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        MessageResponse response = authService.forgotPassword(request);
        return ResponseEntity.ok(response);
    }

    // ─── POST /api/auth/reset-password ───────────────────────

    @PostMapping("/reset-password")
    public ResponseEntity<MessageResponse> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        MessageResponse response = authService.resetPassword(request);
        return ResponseEntity.ok(response);
    }

    // ─── POST /api/auth/logout ───────────────────────────────

    @PostMapping("/logout")
    public ResponseEntity<MessageResponse> logout(@RequestHeader("Authorization") String authHeader) {
        // [POURQUOI] On extrait le token du header pour le blacklister
        String token = authHeader.substring(7); // Retirer "Bearer "
        MessageResponse response = authService.logout(token);
        return ResponseEntity.ok(response);
    }

    // ─── GET /api/auth/me ────────────────────────────────────

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getMe() {
        // [POURQUOI] Le userId est stocké dans le principal par le JwtAuthenticationFilter
        UUID userId = (UUID) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        UserResponse response = authService.getMe(userId);
        return ResponseEntity.ok(response);
    }

    // ─── Utilitaire ──────────────────────────────────────────

    /**
     * [POURQUOI] On gère le cas où un reverse proxy (Nginx, etc.) utilise
     * le header X-Forwarded-For pour transmettre l'IP réelle du client.
     */
    private String extractClientIp(HttpServletRequest request) {
        String xff = request.getHeader("X-Forwarded-For");
        if (xff != null && !xff.isEmpty()) {
            return xff.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}
