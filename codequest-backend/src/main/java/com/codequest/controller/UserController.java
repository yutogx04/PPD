package com.codequest.controller;

import com.codequest.dto.AuthResponse;
import com.codequest.dto.GamificationResult;
import com.codequest.entity.User;
import com.codequest.entity.UserBadge;
import com.codequest.repository.*;
import com.codequest.entity.Submission;
import com.codequest.service.GamificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;
    private final UserBadgeRepository userBadgeRepository;
    private final UserProgressRepository userProgressRepository;
    private final SubmissionRepository submissionRepository;
    private final BadgeRepository badgeRepository;

    @GetMapping("/me")
    public ResponseEntity<AuthResponse.UserDto> getProfile(Authentication auth) {
        Long userId = (Long) auth.getPrincipal();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        return ResponseEntity.ok(AuthResponse.fromEntity(user));
    }

    @PutMapping("/me")
    public ResponseEntity<AuthResponse.UserDto> updateProfile(
            @RequestBody Map<String, String> body, Authentication auth) {
        Long userId = (Long) auth.getPrincipal();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        if (body.containsKey("pseudo")) {
            String newPseudo = body.get("pseudo");
            if (userRepository.existsByPseudo(newPseudo) && !user.getPseudo().equals(newPseudo)) {
                throw new RuntimeException("Ce pseudo est déjà pris");
            }
            user.setPseudo(newPseudo);
        }
        if (body.containsKey("avatarUrl")) {
            user.setAvatarUrl(body.get("avatarUrl"));
        }
        if (body.containsKey("bio")) {
            user.setBio(body.get("bio"));
        }

        userRepository.save(user);
        return ResponseEntity.ok(AuthResponse.fromEntity(user));
    }

    @GetMapping("/me/badges")
    public ResponseEntity<List<GamificationResult.BadgeDto>> getMyBadges(Authentication auth,
            @RequestHeader(value = "Accept-Language", defaultValue = "fr") String lang) {
        Long userId = (Long) auth.getPrincipal();
        List<UserBadge> earnedBadges = userBadgeRepository.findByUserId(userId);

        List<GamificationResult.BadgeDto> badges = badgeRepository.findAll().stream()
                .map(badge -> {
                    var earned = earnedBadges.stream()
                            .filter(ub -> ub.getBadge().getId().equals(badge.getId()))
                            .findFirst();
                    String name = "en".equals(lang) && badge.getNameEn() != null && !badge.getNameEn().isEmpty()
                            ? badge.getNameEn() : badge.getName();
                    String desc = "en".equals(lang) && badge.getDescriptionEn() != null && !badge.getDescriptionEn().isEmpty()
                            ? badge.getDescriptionEn() : badge.getDescription();
                    return GamificationResult.BadgeDto.builder()
                            .id(badge.getId())
                            .name(name)
                            .description(desc)
                            .icon(badge.getIcon())
                            .isEarned(earned.isPresent())
                            .obtainedAt(earned.map(ub -> ub.getObtainedAt().toString()).orElse(null))
                            .build();
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(badges);
    }

    @GetMapping("/me/stats")
    public ResponseEntity<Map<String, Object>> getDetailedStats(Authentication auth) {
        Long userId = (Long) auth.getPrincipal();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        long completedLessons = userProgressRepository.findByUserIdAndCompletedTrue(userId).size();
        long solvedChallenges = submissionRepository.countByUserIdAndStatus(userId, Submission.Status.ACCEPTED);
        long totalBadges = userBadgeRepository.countByUserId(userId);

        Map<String, Object> stats = new HashMap<>();
        stats.put("xp", user.getXp());
        stats.put("level", user.getLevel());
        stats.put("levelName", GamificationService.getLevelName(user.getLevel()));
        stats.put("streak", user.getStreak());
        stats.put("totalLessonsCompleted", completedLessons);
        stats.put("totalChallengesSolved", solvedChallenges);
        stats.put("totalBadges", totalBadges);
        stats.put("memberSince", user.getCreatedAt() != null ? user.getCreatedAt().toString() : "");

        return ResponseEntity.ok(stats);
    }

    @PostMapping("/me/fcm-token")
    public ResponseEntity<Void> updateFcmToken(@RequestBody Map<String, String> body, Authentication auth) {
        Long userId = (Long) auth.getPrincipal();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        user.setFcmToken(body.get("token"));
        userRepository.save(user);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<AuthResponse.UserDto>> searchUsers(
            @RequestParam("q") String query, Authentication auth) {
        List<AuthResponse.UserDto> users = userRepository.findByPseudoContainingIgnoreCase(query).stream()
                .map(AuthResponse::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(users);
    }
}
