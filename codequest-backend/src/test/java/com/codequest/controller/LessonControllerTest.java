package com.codequest.controller;

import com.codequest.dto.GamificationResult;
import com.codequest.dto.LessonDto;
import com.codequest.dto.LessonSlideDto;
import com.codequest.service.LessonService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LessonControllerTest {

    @Mock
    private LessonService lessonService;

    @InjectMocks
    private LessonController lessonController;

    private UsernamePasswordAuthenticationToken auth() {
        return new UsernamePasswordAuthenticationToken(1L, null, List.of());
    }

    @Test
    void getLesson_returnsOk() {
        LessonDto dto = LessonDto.builder().id(1L).title("Intro").build();
        when(lessonService.getLesson(1L, 1L, "fr")).thenReturn(dto);

        ResponseEntity<LessonDto> response = lessonController.getLesson(1L, auth(), "fr");

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Intro", response.getBody().getTitle());
    }

    @Test
    void getSlides_returnsOk() {
        LessonSlideDto slide = LessonSlideDto.builder().id(1L).contentType("TEXT").build();
        when(lessonService.getSlides(1L, "fr")).thenReturn(List.of(slide));

        ResponseEntity<List<LessonSlideDto>> response = lessonController.getSlides(1L, "fr");

        assertEquals(200, response.getStatusCode().value());
        assertEquals(1, response.getBody().size());
        assertEquals("TEXT", response.getBody().get(0).getContentType());
    }

    @Test
    void updateProgress_returnsOk() {
        doNothing().when(lessonService).updateProgress(1L, 1L, 3);

        ResponseEntity<Void> response = lessonController.updateProgress(
                1L, Map.of("slideIndex", 3), auth());

        assertEquals(200, response.getStatusCode().value());
        verify(lessonService).updateProgress(1L, 1L, 3);
    }

    @Test
    void completeLesson_returnsOk() {
        GamificationResult result = GamificationResult.builder()
                .xpGained(20).newXpTotal(420).build();
        when(lessonService.completeLesson(1L, 1L)).thenReturn(result);

        ResponseEntity<GamificationResult> response = lessonController.completeLesson(1L, auth());

        assertEquals(200, response.getStatusCode().value());
        assertEquals(20, response.getBody().getXpGained());
    }

    @Test
    void toggleFavorite_returnsOk() {
        doNothing().when(lessonService).toggleFavorite(1L, 1L);

        ResponseEntity<Void> response = lessonController.toggleFavorite(1L, auth());

        assertEquals(200, response.getStatusCode().value());
        verify(lessonService).toggleFavorite(1L, 1L);
    }

    @Test
    void getFavorites_returnsOk() {
        LessonDto dto = LessonDto.builder().id(1L).title("Fav").build();
        when(lessonService.getFavorites(1L, "fr")).thenReturn(List.of(dto));

        ResponseEntity<List<LessonDto>> response = lessonController.getFavorites(auth(), "fr");

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Fav", response.getBody().get(0).getTitle());
    }

    @Test
    void getContinueLesson_returnsOk() {
        LessonDto dto = LessonDto.builder().id(5L).title("Continue").build();
        when(lessonService.getContinueLesson(1L, "fr")).thenReturn(dto);

        ResponseEntity<LessonDto> response = lessonController.getContinueLesson(auth(), "fr");

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Continue", response.getBody().getTitle());
    }

    @Test
    void getContinueLesson_noContent() {
        when(lessonService.getContinueLesson(1L, "fr")).thenReturn(null);

        ResponseEntity<LessonDto> response = lessonController.getContinueLesson(auth(), "fr");

        assertEquals(204, response.getStatusCode().value());
    }
}
