package com.codequest.controller;

import com.codequest.dto.ChallengeDto;
import com.codequest.dto.LessonDto;
import com.codequest.dto.ModuleDto;
import com.codequest.repository.ChallengeRepository;
import com.codequest.repository.ModuleRepository;
import com.codequest.repository.SubmissionRepository;
import com.codequest.entity.Submission;
import com.codequest.service.LessonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/modules")
@RequiredArgsConstructor
public class ModuleController {

    private final ModuleRepository moduleRepository;
    private final LessonService lessonService;
    private final ChallengeRepository challengeRepository;
    private final SubmissionRepository submissionRepository;

    @GetMapping("/{id}")
    public ResponseEntity<ModuleDto> getModule(@PathVariable Long id,
            @RequestHeader(value = "Accept-Language", defaultValue = "fr") String lang) {
        return ResponseEntity.ok(
                ModuleDto.fromEntity(moduleRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Module non trouvé")), 0, lang)
        );
    }

    @GetMapping("/{id}/lessons")
    public ResponseEntity<List<LessonDto>> getLessons(@PathVariable Long id, Authentication auth,
            @RequestHeader(value = "Accept-Language", defaultValue = "fr") String lang) {
        Long userId = (Long) auth.getPrincipal();
        return ResponseEntity.ok(lessonService.getLessonsByModule(id, userId, lang));
    }

    @GetMapping("/{id}/challenges")
    public ResponseEntity<List<ChallengeDto>> getModuleChallenges(@PathVariable Long id, Authentication auth,
            @RequestHeader(value = "Accept-Language", defaultValue = "fr") String lang) {
        Long userId = (Long) auth.getPrincipal();
        List<ChallengeDto> challenges = challengeRepository.findByModuleId(id).stream()
                .map(c -> {
                    int attempts = (int) submissionRepository.countByUserIdAndChallengeId(userId, c.getId());
                    var best = submissionRepository.findTopByUserIdAndChallengeIdAndStatusOrderByScoreDesc(
                            userId, c.getId(), Submission.Status.ACCEPTED);
                    return ChallengeDto.fromEntity(c, attempts,
                            best.map(Submission::getScore).orElse(0),
                            best.map(Submission::getGrade).orElse(""),
                            best.isPresent(), lang);
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(challenges);
    }
}
