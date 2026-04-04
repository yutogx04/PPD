package com.codequest;
import com.codequest.model.LessonSlide;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
public class LessonSlideTest {
    private LessonSlide slide;
    @Before
    public void setUp() {
        slide = new LessonSlide();
    }
    @Test
    public void isTextSlide_whenTEXT_returnsTrue() {
        slide.setContentType("TEXT");
        assertTrue(slide.isTextSlide());
        assertFalse(slide.isCodeSlide());
        assertFalse(slide.isQcmSlide());
    }
    @Test
    public void isCodeSlide_whenCODE_returnsTrue() {
        slide.setContentType("CODE");
        assertFalse(slide.isTextSlide());
        assertTrue(slide.isCodeSlide());
        assertFalse(slide.isQcmSlide());
    }
    @Test
    public void isQcmSlide_whenQCM_returnsTrue() {
        slide.setContentType("QCM");
        assertFalse(slide.isTextSlide());
        assertFalse(slide.isCodeSlide());
        assertTrue(slide.isQcmSlide());
    }
    @Test
    public void allTypesFalse_whenNull() {
        slide.setContentType(null);
        assertFalse(slide.isTextSlide());
        assertFalse(slide.isCodeSlide());
        assertFalse(slide.isQcmSlide());
    }
    @Test
    public void allTypesFalse_whenUnknown() {
        slide.setContentType("VIDEO");
        assertFalse(slide.isTextSlide());
        assertFalse(slide.isCodeSlide());
        assertFalse(slide.isQcmSlide());
    }
    @Test
    public void setId_getId_works() {
        slide.setId(42L);
        assertEquals(42L, slide.getId());
    }
    @Test
    public void setOrderIndex_getOrderIndex_works() {
        slide.setOrderIndex(3);
        assertEquals(3, slide.getOrderIndex());
    }
    @Test
    public void setContentText_getContentText_works() {
        slide.setContentText("Les variables en Python...");
        assertEquals("Les variables en Python...", slide.getContentText());
    }
    @Test
    public void setCodeSnippet_getCodeSnippet_works() {
        slide.setCodeSnippet("x = 42\nprint(x)");
        assertEquals("x = 42\nprint(x)", slide.getCodeSnippet());
    }
    @Test
    public void setCodeLanguage_getCodeLanguage_works() {
        slide.setCodeLanguage("Python 3");
        assertEquals("Python 3", slide.getCodeLanguage());
    }
    @Test
    public void setExplanation_getExplanation_works() {
        slide.setExplanation("Les variables stockent des valeurs");
        assertEquals("Les variables stockent des valeurs", slide.getExplanation());
    }
    @Test
    public void quizQuestion_defaultNull() {
        assertNull(slide.getQuizQuestion());
    }
}
