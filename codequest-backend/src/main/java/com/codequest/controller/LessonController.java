package com.codequest.controller;

import com.codequest.dto.GamificationResult;
import com.codequest.dto.LessonDto;
import com.codequest.dto.LessonSlideDto;
import com.codequest.service.LessonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class LessonController {

    private final LessonService lessonService;

    @GetMapping("/lessons/{id}")
    public ResponseEntity<LessonDto> getLesson(@PathVariable Long id, Authentication auth,
            @RequestHeader(value = "Accept-Language", defaultValue = "fr") String lang) {
        Long userId = (Long) auth.getPrincipal();
        return ResponseEntity.ok(lessonService.getLesson(id, userId, lang));
    }

    @GetMapping("/lessons/{id}/slides")
    public ResponseEntity<List<LessonSlideDto>> getSlides(@PathVariable Long id,
            @RequestHeader(value = "Accept-Language", defaultValue = "fr") String lang) {
        return ResponseEntity.ok(lessonService.getSlides(id, lang));
    }

    @PutMapping("/lessons/{id}/progress")
    public ResponseEntity<Void> updateProgress(@PathVariable Long id,
                                                @RequestBody Map<String, Integer> body,
                                                Authentication auth) {
        Long userId = (Long) auth.getPrincipal();
        lessonService.updateProgress(id, userId, body.get("slideIndex"));
        return ResponseEntity.ok().build();
    }

    @PostMapping("/lessons/{id}/complete")
    public ResponseEntity<GamificationResult> completeLesson(@PathVariable Long id, Authentication auth) {
        Long userId = (Long) auth.getPrincipal();
        return ResponseEntity.ok(lessonService.completeLesson(id, userId));
    }

    @PostMapping("/lessons/{id}/favorite")
    public ResponseEntity<Void> toggleFavorite(@PathVariable Long id, Authentication auth) {
        Long userId = (Long) auth.getPrincipal();
        lessonService.toggleFavorite(id, userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/favorites")
    public ResponseEntity<List<LessonDto>> getFavorites(Authentication auth,
            @RequestHeader(value = "Accept-Language", defaultValue = "fr") String lang) {
        Long userId = (Long) auth.getPrincipal();
        return ResponseEntity.ok(lessonService.getFavorites(userId, lang));
    }

    @GetMapping("/users/me/continue")
    public ResponseEntity<LessonDto> getContinueLesson(Authentication auth,
            @RequestHeader(value = "Accept-Language", defaultValue = "fr") String lang) {
        Long userId = (Long) auth.getPrincipal();
        LessonDto lesson = lessonService.getContinueLesson(userId, lang);
        if (lesson == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(lesson);
    }
}
