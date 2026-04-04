package com.codequest.controller;

import com.codequest.dto.ModuleDto;
import com.codequest.dto.TrackDto;
import com.codequest.entity.Module;
import com.codequest.repository.ModuleRepository;
import com.codequest.service.TrackService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrackControllerTest {

    @Mock
    private TrackService trackService;

    @Mock
    private ModuleRepository moduleRepository;

    @InjectMocks
    private TrackController trackController;

    private UsernamePasswordAuthenticationToken auth() {
        return new UsernamePasswordAuthenticationToken(1L, null, List.of());
    }

    @Test
    void getTracks_returnsOk() {
        TrackDto dto = TrackDto.builder().id(1L).title("Python Basics").build();
        when(trackService.getAllTracks(1L, "fr")).thenReturn(List.of(dto));

        ResponseEntity<List<TrackDto>> response = trackController.getTracks(auth(), "fr");

        assertEquals(200, response.getStatusCode().value());
        assertEquals(1, response.getBody().size());
        assertEquals("Python Basics", response.getBody().get(0).getTitle());
    }

    @Test
    void getTrack_returnsOk() {
        TrackDto dto = TrackDto.builder().id(1L).title("Python Basics").build();
        when(trackService.getTrack(1L, 1L, "fr")).thenReturn(dto);

        ResponseEntity<TrackDto> response = trackController.getTrack(1L, auth(), "fr");

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Python Basics", response.getBody().getTitle());
    }

    @Test
    void getModules_returnsOk() {
        Module module = Module.builder().id(1L).title("Variables").orderIndex(0).build();
        when(moduleRepository.findByTrackIdOrderByOrderIndexAsc(1L)).thenReturn(List.of(module));

        ResponseEntity<List<ModuleDto>> response = trackController.getModules(1L, "fr");

        assertEquals(200, response.getStatusCode().value());
        assertEquals(1, response.getBody().size());
        assertEquals("Variables", response.getBody().get(0).getTitle());
    }
}
