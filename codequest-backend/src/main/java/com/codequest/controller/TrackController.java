package com.codequest.controller;

import com.codequest.dto.ModuleDto;
import com.codequest.dto.TrackDto;
import com.codequest.repository.ModuleRepository;
import com.codequest.service.TrackService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class TrackController {

    private final TrackService trackService;
    private final ModuleRepository moduleRepository;

    @GetMapping("/tracks")
    public ResponseEntity<List<TrackDto>> getTracks(Authentication auth,
            @RequestHeader(value = "Accept-Language", defaultValue = "fr") String lang) {
        Long userId = (Long) auth.getPrincipal();
        return ResponseEntity.ok(trackService.getAllTracks(userId, lang));
    }

    @GetMapping("/tracks/{id}")
    public ResponseEntity<TrackDto> getTrack(@PathVariable Long id, Authentication auth,
            @RequestHeader(value = "Accept-Language", defaultValue = "fr") String lang) {
        Long userId = (Long) auth.getPrincipal();
        return ResponseEntity.ok(trackService.getTrack(id, userId, lang));
    }

    @GetMapping("/tracks/{id}/modules")
    public ResponseEntity<List<ModuleDto>> getModules(@PathVariable Long id,
            @RequestHeader(value = "Accept-Language", defaultValue = "fr") String lang) {
        List<ModuleDto> modules = moduleRepository.findByTrackIdOrderByOrderIndexAsc(id).stream()
                .map(m -> ModuleDto.fromEntity(m, 0, lang))
                .collect(Collectors.toList());
        return ResponseEntity.ok(modules);
    }
}
