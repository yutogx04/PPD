package com.codequest.controller;

import com.codequest.dto.ChallengeDto;
import com.codequest.dto.DailyChallengeDto;
import com.codequest.dto.TrackDto;
import com.codequest.dto.LeaderboardEntry;
import com.codequest.dto.SubmitCodeRequest;
import com.codequest.dto.SubmissionResponse;
import com.codequest.entity.Challenge;
import com.codequest.entity.DailyChallenge;
import com.codequest.entity.Submission;
import com.codequest.repository.*;
import com.codequest.service.SubmissionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/challenges")
@RequiredArgsConstructor
public class ChallengeController {

    private final ChallengeRepository challengeRepository;
    private final SubmissionRepository submissionRepository;
    private final DailyChallengeRepository dailyChallengeRepository;
    private final FriendshipRepository friendshipRepository;
    private final SubmissionService submissionService;

    @GetMapping
    public ResponseEntity<List<ChallengeDto>> getChallenges(Authentication auth,
            @RequestHeader(value = "Accept-Language", defaultValue = "fr") String lang) {
        Long userId = (Long) auth.getPrincipal();
        List<ChallengeDto> challenges = challengeRepository.findAll().stream()
                .map(c -> toChallengeDto(c, userId, lang))
                .collect(Collectors.toList());
        return ResponseEntity.ok(challenges);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChallengeDto> getChallenge(@PathVariable Long id, Authentication auth,
            @RequestHeader(value = "Accept-Language", defaultValue = "fr") String lang) {
        Long userId = (Long) auth.getPrincipal();
        Challenge challenge = challengeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Défi non trouvé"));
        return ResponseEntity.ok(toChallengeDto(challenge, userId, lang));
    }

    @GetMapping("/daily")
    public ResponseEntity<DailyChallengeDto> getDailyChallenge(Authentication auth,
            @RequestHeader(value = "Accept-Language", defaultValue = "en") String lang) {
        Long userId = (Long) auth.getPrincipal();
        
        DailyChallenge daily = dailyChallengeRepository.findByDate(LocalDate.now())
                .orElseGet(() -> {
                    List<Challenge> allChallenges = challengeRepository.findAll();
                    if (allChallenges.isEmpty()) {
                        throw new RuntimeException("No challenges available for daily challenge fallback");
                    }
                    
                    int dayOfYear = LocalDate.now().getDayOfYear();
                    int year = LocalDate.now().getYear();
                    
                    int pseudoRandomIndex = (dayOfYear + year) % allChallenges.size();
                    Challenge selected = allChallenges.get(pseudoRandomIndex);
                    
                    DailyChallenge newDaily = DailyChallenge.builder()
                            .challenge(selected)
                            .date(LocalDate.now())
                            .build();
                            
                    return dailyChallengeRepository.save(newDaily);
                });

        boolean completed = submissionRepository
                .findTopByUserIdAndChallengeIdAndStatusOrderByScoreDesc(
                        userId, daily.getChallenge().getId(), Submission.Status.ACCEPTED)
                .isPresent();
                
        return ResponseEntity.ok(DailyChallengeDto.fromEntity(daily, completed, lang));
    }

    @PostMapping("/{id}/run")
    public ResponseEntity<SubmissionResponse> runCode(@PathVariable Long id,
                                                       @Valid @RequestBody SubmitCodeRequest request,
                                                       Authentication auth) {
        Long userId = (Long) auth.getPrincipal();
        return ResponseEntity.ok(
                submissionService.runCode(id, request.getCode(), request.getLanguage(), userId)
        );
    }

    @PostMapping("/{id}/submit")
    public ResponseEntity<SubmissionResponse> submitCode(@PathVariable Long id,
                                                          @Valid @RequestBody SubmitCodeRequest request,
                                                          Authentication auth) {
        Long userId = (Long) auth.getPrincipal();
        return ResponseEntity.ok(
                submissionService.submitCode(id, request.getCode(), request.getLanguage(), userId)
        );
    }

    @GetMapping("/{id}/hint")
    public ResponseEntity<Map<String, String>> getHint(@PathVariable Long id, Authentication auth,
            @RequestHeader(value = "Accept-Language", defaultValue = "fr") String lang) {
        Long userId = (Long) auth.getPrincipal();
        long attempts = submissionRepository.countByUserIdAndChallengeId(userId, id);
        if (attempts < 3) {
            throw new RuntimeException("L'indice est disponible après 3 tentatives (vous en avez " + attempts + ")");
        }
        Challenge challenge = challengeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Défi non trouvé"));
        String hint = TrackDto.l(challenge.getHint(), challenge.getHintEn(), lang);
        return ResponseEntity.ok(Map.of("hint", hint != null ? hint : ""));
    }

    @GetMapping("/{id}/solution")
    public ResponseEntity<Map<String, String>> getSolution(@PathVariable Long id, Authentication auth) {
        Long userId = (Long) auth.getPrincipal();
        long attempts = submissionRepository.countByUserIdAndChallengeId(userId, id);
        if (attempts < 5) {
            throw new RuntimeException("La solution est disponible après 5 tentatives (vous en avez " + attempts + ")");
        }
        Challenge challenge = challengeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Défi non trouvé"));
        return ResponseEntity.ok(Map.of("solution", challenge.getSolution() != null ? challenge.getSolution() : ""));
    }

    @GetMapping("/{id}/friends-scores")
    public ResponseEntity<List<LeaderboardEntry>> getFriendScoresOnChallenge(
            @PathVariable Long id, Authentication auth) {
        Long userId = (Long) auth.getPrincipal();

        List<Long> friendIds = friendshipRepository.findAcceptedFriendships(userId).stream()
                .map(f -> f.getSender().getId().equals(userId) ? f.getReceiver().getId() : f.getSender().getId())
                .collect(Collectors.toList());

        List<LeaderboardEntry> scores = new java.util.ArrayList<>();
        int rank = 1;
        for (Long friendId : friendIds) {
            var bestSubmission = submissionRepository
                    .findTopByUserIdAndChallengeIdAndStatusOrderByScoreDesc(
                            friendId, id, Submission.Status.ACCEPTED);
            if (bestSubmission.isPresent()) {
                var sub = bestSubmission.get();
                var user = sub.getUser();
                scores.add(LeaderboardEntry.builder()
                        .rank(rank++)
                        .pseudo(user.getPseudo())
                        .avatarUrl(user.getAvatarUrl())
                        .level(user.getLevel())
                        .xp(user.getXp())
                        .isCurrentUser(false)
                        .build());
            }
        }
        scores.sort((a, b) -> Integer.compare(b.getXp(), a.getXp()));
        for (int i = 0; i < scores.size(); i++) {
            scores.get(i).setRank(i + 1);
        }

        return ResponseEntity.ok(scores);
    }

    private ChallengeDto toChallengeDto(Challenge challenge, Long userId, String lang) {
        int attemptCount = (int) submissionRepository.countByUserIdAndChallengeId(userId, challenge.getId());
        var bestSubmission = submissionRepository
                .findTopByUserIdAndChallengeIdAndStatusOrderByScoreDesc(
                        userId, challenge.getId(), Submission.Status.ACCEPTED);
        int bestScore = bestSubmission.map(Submission::getScore).orElse(0);
        String bestGrade = bestSubmission.map(Submission::getGrade).orElse("");
        boolean solved = bestSubmission.isPresent();

        return ChallengeDto.fromEntity(challenge, attemptCount, bestScore, bestGrade, solved, lang);
    }
}
