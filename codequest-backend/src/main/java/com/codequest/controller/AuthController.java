package com.codequest.controller;

import com.codequest.dto.AuthResponse;
import com.codequest.dto.LoginRequest;
import com.codequest.dto.RegisterRequest;
import com.codequest.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/verify-email")
    public ResponseEntity<AuthResponse> verifyEmail(@RequestBody Map<String, String> body) {
        return ResponseEntity.ok(
                authService.verifyEmail(body.get("email"), body.get("otpCode"))
        );
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(@RequestBody Map<String, String> body) {
        return ResponseEntity.ok(
                authService.refreshToken(body.get("refreshToken"))
        );
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<Void> forgotPassword(@RequestBody Map<String, String> body) {
        authService.forgotPassword(body.get("email"));
        return ResponseEntity.ok().build();
    }

    @PostMapping("/google")
    public ResponseEntity<AuthResponse> googleSignIn(@RequestBody Map<String, String> body) {
        return ResponseEntity.ok(
                authService.googleSignIn(body.get("idToken"))
        );
    }
}
