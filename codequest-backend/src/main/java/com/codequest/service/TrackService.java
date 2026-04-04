package com.codequest.service;

import com.codequest.dto.TrackDto;
import com.codequest.entity.Track;
import com.codequest.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TrackService {

    private final TrackRepository trackRepository;
    private final ModuleRepository moduleRepository;
    private final LessonRepository lessonRepository;
    private final ChallengeRepository challengeRepository;
    private final UserProgressRepository userProgressRepository;
    private final UserRepository userRepository;

    public List<TrackDto> getAllTracks(Long userId) {
        return getAllTracks(userId, "fr");
    }

    public List<TrackDto> getAllTracks(Long userId, String lang) {
        int userLevel = userRepository.findById(userId)
                .map(u -> u.getLevel())
                .orElse(1);

        return trackRepository.findAll().stream()
                .map(track -> toDto(track, userId, userLevel, lang))
                .collect(Collectors.toList());
    }

    public TrackDto getTrack(Long trackId, Long userId) {
        return getTrack(trackId, userId, "fr");
    }

    public TrackDto getTrack(Long trackId, Long userId, String lang) {
        Track track = trackRepository.findById(trackId)
                .orElseThrow(() -> new RuntimeException("Parcours non trouvé"));
        int userLevel = userRepository.findById(userId)
                .map(u -> u.getLevel())
                .orElse(1);
        return toDto(track, userId, userLevel, lang);
    }

    private TrackDto toDto(Track track, Long userId, int userLevel, String lang) {
        int moduleCount = moduleRepository.findByTrackIdOrderByOrderIndexAsc(track.getId()).size();
        int lessonCount = (int) lessonRepository.countByModuleTrackId(track.getId());
        int challengeCount = (int) challengeRepository.countByModuleTrackId(track.getId());

        long completedLessons = userProgressRepository
                .countByUserIdAndCompletedTrueAndLessonModuleTrackId(userId, track.getId());
        int progressPercent = lessonCount > 0 ? (int) ((completedLessons * 100) / lessonCount) : 0;

        boolean isLocked = userLevel < track.getRequiredLevel();

        return TrackDto.fromEntity(track, moduleCount, lessonCount, challengeCount, progressPercent, isLocked, lang);
    }
}
