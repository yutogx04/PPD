package com.codequest.controller;

import com.codequest.entity.User;
import com.codequest.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final UserRepository userRepository;

    @PostMapping("/register-token")
    public ResponseEntity<Void> registerFcmToken(@RequestBody Map<String, String> body,
                                                   Authentication auth) {
        Long userId = (Long) auth.getPrincipal();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        user.setFcmToken(body.get("token"));
        userRepository.save(user);
        return ResponseEntity.ok().build();
    }
}
