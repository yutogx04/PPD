package com.codequest.controller;

import com.codequest.entity.*;
import com.codequest.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserRepository userRepository;
    private final TrackRepository trackRepository;
    private final ModuleRepository moduleRepository;
    private final LessonRepository lessonRepository;
    private final LessonSlideRepository lessonSlideRepository;
    private final ChallengeRepository challengeRepository;
    private final SubmissionRepository submissionRepository;
    private final BadgeRepository badgeRepository;
    private final UserBadgeRepository userBadgeRepository;
    private final TestCaseRepository testCaseRepository;
    private final AppSettingsRepository appSettingsRepository;

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getDashboardStats(Authentication auth) {
        checkAdmin(auth);

        long totalUsers = userRepository.count();
        long totalLessons = lessonRepository.count();
        long totalSubmissions = submissionRepository.count();
        long acceptedSubmissions = submissionRepository.countByStatus(Submission.Status.ACCEPTED);
        double successRate = totalSubmissions > 0 ? Math.round((double) acceptedSubmissions / totalSubmissions * 100) : 0;

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalUsers", totalUsers);
        stats.put("totalLessons", totalLessons);
        stats.put("totalSubmissions", totalSubmissions);
        stats.put("successRate", successRate);

        return ResponseEntity.ok(stats);
    }

    @GetMapping("/stats/top-challenges")
    public ResponseEntity<List<Map<String, Object>>> getTopChallenges(Authentication auth) {
        checkAdmin(auth);

        List<Challenge> challenges = challengeRepository.findAll();
        List<Map<String, Object>> result = challenges.stream()
                .map(c -> {
                    long count = submissionRepository.countByChallengeId(c.getId());
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", c.getId());
                    map.put("title", c.getTitle());
                    map.put("submissions", count);
                    return map;
                })
                .sorted((a, b) -> Long.compare((long) b.get("submissions"), (long) a.get("submissions")))
                .limit(5)
                .collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }

    @GetMapping("/stats/recent-submissions")
    public ResponseEntity<List<Map<String, Object>>> getRecentSubmissions(Authentication auth) {
        checkAdmin(auth);

        List<Submission> subs = submissionRepository.findTop20ByOrderByCreatedAtDesc();
        List<Map<String, Object>> result = subs.stream()
                .map(s -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", s.getId());
                    map.put("user", s.getUser().getPseudo());
                    map.put("challenge", s.getChallenge().getTitle());
                    map.put("status", s.getStatus().name());
                    map.put("language", s.getLanguage().name());
                    map.put("executionTimeMs", s.getExecutionTimeMs());
                    map.put("createdAt", s.getCreatedAt() != null ? s.getCreatedAt().toString() : "");
                    return map;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }

    @GetMapping("/tracks")
    public ResponseEntity<List<Map<String, Object>>> getAllTracks(Authentication auth) {
        checkAdmin(auth);
        List<Map<String, Object>> result = trackRepository.findAll().stream()
                .map(t -> {
                    long moduleCount = moduleRepository.countByTrackId(t.getId());
                    long lessonCount = lessonRepository.countByModuleTrackId(t.getId());
                    long challengeCount = challengeRepository.countByModuleTrackId(t.getId());
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", t.getId());
                    map.put("title", t.getTitle());
                    map.put("description", t.getDescription());
                    map.put("language", t.getLanguage().name());
                    map.put("difficulty", t.getDifficulty().name());
                    map.put("requiredLevel", t.getRequiredLevel());
                    map.put("xpPerLesson", t.getXpPerLesson());
                    map.put("modules", moduleCount);
                    map.put("lessons", lessonCount);
                    map.put("challenges", challengeCount);
                    return map;
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @PostMapping("/tracks")
    public ResponseEntity<Track> createTrack(@RequestBody Map<String, Object> body, Authentication auth) {
        checkAdmin(auth);
        Track track = Track.builder()
                .title((String) body.get("title"))
                .description((String) body.get("description"))
                .language(Track.Language.valueOf((String) body.get("language")))
                .difficulty(Track.Difficulty.valueOf((String) body.get("difficulty")))
                .requiredLevel(body.containsKey("requiredLevel") ? (int) body.get("requiredLevel") : 1)
                .xpPerLesson(body.containsKey("xpPerLesson") ? (int) body.get("xpPerLesson") : 20)
                .build();
        return ResponseEntity.ok(trackRepository.save(track));
    }

    @PutMapping("/tracks/{id}")
    public ResponseEntity<Track> updateTrack(@PathVariable Long id, @RequestBody Map<String, Object> body, Authentication auth) {
        checkAdmin(auth);
        Track track = trackRepository.findById(id).orElseThrow(() -> new RuntimeException("Track not found"));
        if (body.containsKey("title")) track.setTitle((String) body.get("title"));
        if (body.containsKey("description")) track.setDescription((String) body.get("description"));
        if (body.containsKey("language")) track.setLanguage(Track.Language.valueOf((String) body.get("language")));
        if (body.containsKey("difficulty")) track.setDifficulty(Track.Difficulty.valueOf((String) body.get("difficulty")));
        return ResponseEntity.ok(trackRepository.save(track));
    }

    @DeleteMapping("/tracks/{id}")
    public ResponseEntity<Void> deleteTrack(@PathVariable Long id, Authentication auth) {
        checkAdmin(auth);
        trackRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/modules")
    public ResponseEntity<List<Map<String, Object>>> getAllModules(Authentication auth) {
        checkAdmin(auth);
        List<Map<String, Object>> result = moduleRepository.findAll().stream()
                .map(m -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", m.getId());
                    map.put("title", m.getTitle());
                    map.put("trackId", m.getTrack().getId());
                    map.put("trackName", m.getTrack().getTitle());
                    return map;
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/lessons")
    public ResponseEntity<List<Map<String, Object>>> getAllLessons(Authentication auth) {
        checkAdmin(auth);
        List<Map<String, Object>> result = lessonRepository.findAll().stream()
                .map(l -> {
                    long slideCount = lessonSlideRepository.countByLessonId(l.getId());
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", l.getId());
                    map.put("title", l.getTitle());
                    map.put("type", l.getType().name());
                    map.put("xpReward", l.getXpReward());
                    map.put("durationMinutes", l.getDurationMinutes());
                    map.put("slides", slideCount);
                    map.put("moduleId", l.getModule().getId());
                    map.put("moduleName", l.getModule().getTitle());
                    map.put("trackName", l.getModule().getTrack().getTitle());
                    return map;
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @PostMapping("/lessons")
    public ResponseEntity<Lesson> createLesson(@RequestBody Map<String, Object> body, Authentication auth) {
        checkAdmin(auth);
        com.codequest.entity.Module module = moduleRepository.findById(Long.valueOf(body.get("moduleId").toString()))
                .orElseThrow(() -> new RuntimeException("Module not found"));
        Lesson lesson = Lesson.builder()
                .title((String) body.get("title"))
                .type(body.containsKey("type") ? Lesson.LessonType.valueOf((String) body.get("type")) : Lesson.LessonType.THEORY)
                .xpReward(body.containsKey("xpReward") ? (int) body.get("xpReward") : 20)
                .durationMinutes(body.containsKey("durationMinutes") ? (int) body.get("durationMinutes") : 5)
                .module(module)
                .build();
        return ResponseEntity.ok(lessonRepository.save(lesson));
    }

    @PutMapping("/lessons/{id}")
    public ResponseEntity<Lesson> updateLesson(@PathVariable Long id, @RequestBody Map<String, Object> body, Authentication auth) {
        checkAdmin(auth);
        Lesson lesson = lessonRepository.findById(id).orElseThrow(() -> new RuntimeException("Lesson not found"));
        if (body.containsKey("title")) lesson.setTitle((String) body.get("title"));
        if (body.containsKey("type")) lesson.setType(Lesson.LessonType.valueOf((String) body.get("type")));
        if (body.containsKey("xpReward")) lesson.setXpReward(((Number) body.get("xpReward")).intValue());
        if (body.containsKey("durationMinutes")) lesson.setDurationMinutes(((Number) body.get("durationMinutes")).intValue());
        if (body.containsKey("moduleId")) {
            com.codequest.entity.Module module = moduleRepository.findById(Long.valueOf(body.get("moduleId").toString()))
                    .orElseThrow(() -> new RuntimeException("Module not found"));
            lesson.setModule(module);
        }
        return ResponseEntity.ok(lessonRepository.save(lesson));
    }

    @DeleteMapping("/lessons/{id}")
    public ResponseEntity<Void> deleteLesson(@PathVariable Long id, Authentication auth) {
        checkAdmin(auth);
        lessonRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/challenges")
    public ResponseEntity<List<Map<String, Object>>> getAllChallenges(Authentication auth) {
        checkAdmin(auth);
        List<Map<String, Object>> result = challengeRepository.findAll().stream()
                .map(c -> {
                    long subCount = submissionRepository.countByChallengeId(c.getId());
                    long acceptedCount = submissionRepository.countByChallengeIdAndStatus(c.getId(), Submission.Status.ACCEPTED);
                    long testCount = testCaseRepository.countByChallengeId(c.getId());
                    double rate = subCount > 0 ? Math.round((double) acceptedCount / subCount * 100) : 0;
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", c.getId());
                    map.put("title", c.getTitle());
                    map.put("description", c.getDescription());
                    map.put("difficulty", c.getDifficulty().name());
                    map.put("language", c.getLanguage().name());
                    map.put("xpReward", c.getXpReward());
                    map.put("starterCode", c.getStarterCode());
                    map.put("testCases", testCount);
                    map.put("submissions", subCount);
                    map.put("successRate", rate + "%");
                    map.put("trackName", c.getModule().getTrack().getTitle());
                    return map;
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @PostMapping("/challenges")
    public ResponseEntity<Challenge> createChallenge(@RequestBody Map<String, Object> body, Authentication auth) {
        checkAdmin(auth);
        com.codequest.entity.Module module = moduleRepository.findById(Long.valueOf(body.get("moduleId").toString()))
                .orElseThrow(() -> new RuntimeException("Module not found"));
        Challenge challenge = Challenge.builder()
                .title((String) body.get("title"))
                .description((String) body.get("description"))
                .difficulty(Track.Difficulty.valueOf((String) body.get("difficulty")))
                .language(Track.Language.valueOf((String) body.get("language")))
                .starterCode((String) body.getOrDefault("starterCode", ""))
                .xpReward(body.containsKey("xpReward") ? (int) body.get("xpReward") : 30)
                .module(module)
                .build();
        return ResponseEntity.ok(challengeRepository.save(challenge));
    }

    @PutMapping("/challenges/{id}")
    public ResponseEntity<Challenge> updateChallenge(@PathVariable Long id, @RequestBody Map<String, Object> body, Authentication auth) {
        checkAdmin(auth);
        Challenge challenge = challengeRepository.findById(id).orElseThrow(() -> new RuntimeException("Challenge not found"));
        if (body.containsKey("title")) challenge.setTitle((String) body.get("title"));
        if (body.containsKey("description")) challenge.setDescription((String) body.get("description"));
        if (body.containsKey("difficulty")) challenge.setDifficulty(Track.Difficulty.valueOf((String) body.get("difficulty")));
        if (body.containsKey("language")) challenge.setLanguage(Track.Language.valueOf((String) body.get("language")));
        if (body.containsKey("starterCode")) challenge.setStarterCode((String) body.get("starterCode"));
        if (body.containsKey("xpReward")) challenge.setXpReward(((Number) body.get("xpReward")).intValue());
        if (body.containsKey("moduleId")) {
            com.codequest.entity.Module module = moduleRepository.findById(Long.valueOf(body.get("moduleId").toString()))
                    .orElseThrow(() -> new RuntimeException("Module not found"));
            challenge.setModule(module);
        }
        return ResponseEntity.ok(challengeRepository.save(challenge));
    }

    @DeleteMapping("/challenges/{id}")
    public ResponseEntity<Void> deleteChallenge(@PathVariable Long id, Authentication auth) {
        checkAdmin(auth);
        challengeRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/users")
    public ResponseEntity<List<Map<String, Object>>> getAllUsers(Authentication auth) {
        checkAdmin(auth);
        List<Map<String, Object>> result = userRepository.findAll().stream()
                .map(u -> {
                    long badges = userBadgeRepository.countByUserId(u.getId());
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", u.getId());
                    map.put("pseudo", u.getPseudo());
                    map.put("email", u.getEmail());
                    map.put("level", u.getLevel());
                    map.put("xp", u.getXp());
                    map.put("streak", u.getStreak());
                    map.put("totalLessonsCompleted", u.getTotalLessonsCompleted());
                    map.put("totalChallengesSolved", u.getTotalChallengesSolved());
                    map.put("role", u.getRole().name());
                    map.put("enabled", u.isEnabled());
                    map.put("badges", badges);
                    return map;
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @PutMapping("/users/{id}/role")
    public ResponseEntity<Void> updateUserRole(@PathVariable Long id, @RequestBody Map<String, String> body, Authentication auth) {
        checkAdmin(auth);
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        user.setRole(User.Role.valueOf(body.get("role")));
        userRepository.save(user);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/users/{id}/toggle-active")
    public ResponseEntity<Void> toggleUserActive(@PathVariable Long id, Authentication auth) {
        checkAdmin(auth);
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        user.setEnabled(!user.isEnabled());
        userRepository.save(user);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/badges")
    public ResponseEntity<List<Map<String, Object>>> getAllBadges(Authentication auth) {
        checkAdmin(auth);
        List<Map<String, Object>> result = badgeRepository.findAll().stream()
                .map(b -> {
                    long holders = userBadgeRepository.countByBadgeId(b.getId());
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", b.getId());
                    map.put("name", b.getName());
                    map.put("description", b.getDescription());
                    map.put("icon", b.getIcon());
                    map.put("conditionType", b.getConditionType().name());
                    map.put("conditionValue", b.getConditionValue());
                    map.put("holders", holders);
                    return map;
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @PostMapping("/badges")
    public ResponseEntity<Badge> createBadge(@RequestBody Map<String, Object> body, Authentication auth) {
        checkAdmin(auth);
        Badge badge = Badge.builder()
                .name((String) body.get("name"))
                .description((String) body.get("description"))
                .icon((String) body.get("icon"))
                .conditionType(Badge.ConditionType.valueOf((String) body.get("conditionType")))
                .conditionValue(body.containsKey("conditionValue") ? (int) body.get("conditionValue") : 0)
                .build();
        return ResponseEntity.ok(badgeRepository.save(badge));
    }

    @PutMapping("/badges/{id}")
    public ResponseEntity<Badge> updateBadge(@PathVariable Long id, @RequestBody Map<String, Object> body, Authentication auth) {
        checkAdmin(auth);
        Badge badge = badgeRepository.findById(id).orElseThrow(() -> new RuntimeException("Badge not found"));
        if (body.containsKey("name")) badge.setName((String) body.get("name"));
        if (body.containsKey("description")) badge.setDescription((String) body.get("description"));
        if (body.containsKey("icon")) badge.setIcon((String) body.get("icon"));
        if (body.containsKey("conditionType")) badge.setConditionType(Badge.ConditionType.valueOf((String) body.get("conditionType")));
        if (body.containsKey("conditionValue")) badge.setConditionValue(((Number) body.get("conditionValue")).intValue());
        return ResponseEntity.ok(badgeRepository.save(badge));
    }

    @DeleteMapping("/badges/{id}")
    public ResponseEntity<Void> deleteBadge(@PathVariable Long id, Authentication auth) {
        checkAdmin(auth);
        badgeRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/settings")
    public ResponseEntity<Map<String, Object>> getSettings(Authentication auth) {
        checkAdmin(auth);
        com.codequest.entity.AppSettings settings = appSettingsRepository.findAll().stream()
                .findFirst()
                .orElseGet(() -> appSettingsRepository.save(com.codequest.entity.AppSettings.builder().build()));

        Map<String, Object> result = new HashMap<>();
        result.put("appName", settings.getAppName());
        result.put("backendUrl", settings.getBackendUrl());
        result.put("defaultLanguage", settings.getDefaultLanguage());
        result.put("dailyReminderTime", settings.getDailyReminderTime());
        result.put("streakAlertHours", settings.getStreakAlertHours());
        result.put("dailyChallengeTime", settings.getDailyChallengeTime());
        result.put("sandboxTimeoutSec", settings.getSandboxTimeoutSec());
        result.put("sandboxMemoryMb", settings.getSandboxMemoryMb());
        result.put("sandboxRateLimit", settings.getSandboxRateLimit());
        result.put("primaryColor", settings.getPrimaryColor());
        return ResponseEntity.ok(result);
    }

    @PutMapping("/settings")
    public ResponseEntity<Map<String, Object>> updateSettings(@RequestBody Map<String, Object> body, Authentication auth) {
        checkAdmin(auth);
        com.codequest.entity.AppSettings settings = appSettingsRepository.findAll().stream()
                .findFirst()
                .orElseGet(() -> appSettingsRepository.save(com.codequest.entity.AppSettings.builder().build()));

        if (body.containsKey("appName")) settings.setAppName((String) body.get("appName"));
        if (body.containsKey("backendUrl")) settings.setBackendUrl((String) body.get("backendUrl"));
        if (body.containsKey("defaultLanguage")) settings.setDefaultLanguage((String) body.get("defaultLanguage"));
        if (body.containsKey("dailyReminderTime")) settings.setDailyReminderTime((String) body.get("dailyReminderTime"));
        if (body.containsKey("streakAlertHours")) settings.setStreakAlertHours(((Number) body.get("streakAlertHours")).intValue());
        if (body.containsKey("dailyChallengeTime")) settings.setDailyChallengeTime((String) body.get("dailyChallengeTime"));
        if (body.containsKey("sandboxTimeoutSec")) settings.setSandboxTimeoutSec(((Number) body.get("sandboxTimeoutSec")).intValue());
        if (body.containsKey("sandboxMemoryMb")) settings.setSandboxMemoryMb(((Number) body.get("sandboxMemoryMb")).intValue());
        if (body.containsKey("sandboxRateLimit")) settings.setSandboxRateLimit(((Number) body.get("sandboxRateLimit")).intValue());
        if (body.containsKey("primaryColor")) settings.setPrimaryColor((String) body.get("primaryColor"));

        appSettingsRepository.save(settings);

        Map<String, Object> result = new HashMap<>();
        result.put("appName", settings.getAppName());
        result.put("backendUrl", settings.getBackendUrl());
        result.put("defaultLanguage", settings.getDefaultLanguage());
        result.put("dailyReminderTime", settings.getDailyReminderTime());
        result.put("streakAlertHours", settings.getStreakAlertHours());
        result.put("dailyChallengeTime", settings.getDailyChallengeTime());
        result.put("sandboxTimeoutSec", settings.getSandboxTimeoutSec());
        result.put("sandboxMemoryMb", settings.getSandboxMemoryMb());
        result.put("sandboxRateLimit", settings.getSandboxRateLimit());
        result.put("primaryColor", settings.getPrimaryColor());
        return ResponseEntity.ok(result);
    }

    private void checkAdmin(Authentication auth) {
        Long userId = (Long) auth.getPrincipal();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        if (user.getRole() != User.Role.ADMIN) {
            throw new RuntimeException("Accès refusé : droits administrateur requis");
        }
    }
}
