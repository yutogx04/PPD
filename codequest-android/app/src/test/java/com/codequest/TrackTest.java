package com.codequest;
import com.codequest.model.Track;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
public class TrackTest {
    private Track track;
    @Before
    public void setUp() {
        track = new Track(1L, "Python", "Apprends Python", "BEGINNER",
                "PYTHON", 8, 32, 20, 40, false);
    }
    @Test
    public void constructor_setsAllFields() {
        assertEquals(1L, track.getId());
        assertEquals("Python", track.getTitle());
        assertEquals("Apprends Python", track.getDescription());
        assertEquals("BEGINNER", track.getDifficulty());
        assertEquals("PYTHON", track.getLanguage());
        assertEquals(8, track.getModuleCount());
        assertEquals(32, track.getLessonCount());
        assertEquals(20, track.getChallengeCount());
        assertEquals(40, track.getProgressPercent());
        assertFalse(track.isLocked());
    }
    @Test
    public void defaultConstructor_createsEmptyTrack() {
        Track empty = new Track();
        assertEquals(0, empty.getId());
        assertNull(empty.getTitle());
        assertNull(empty.getDescription());
        assertEquals(0, empty.getModuleCount());
        assertFalse(empty.isLocked());
    }
    @Test
    public void getLanguageShort_python_returnsPy() {
        track.setLanguage("PYTHON");
        assertEquals("Py", track.getLanguageShort());
    }
    @Test
    public void getLanguageShort_javascript_returnsJS() {
        track.setLanguage("JAVASCRIPT");
        assertEquals("JS", track.getLanguageShort());
    }
    @Test
    public void getLanguageShort_java_returnsJv() {
        track.setLanguage("JAVA");
        assertEquals("Jv", track.getLanguageShort());
    }
    @Test
    public void getLanguageShort_unknown_returnsRawValue() {
        track.setLanguage("RUST");
        assertEquals("RUST", track.getLanguageShort());
    }
    @Test
    public void setLocked_true_makesTrackLocked() {
        track.setLocked(true);
        assertTrue(track.isLocked());
    }
    @Test
    public void setRequiredLevel_getRequiredLevel_works() {
        track.setRequiredLevel(5);
        assertEquals(5, track.getRequiredLevel());
    }
    @Test
    public void setProgressPercent_getProgressPercent_works() {
        track.setProgressPercent(75);
        assertEquals(75, track.getProgressPercent());
    }
    @Test
    public void progressPercent_zeroByDefault() {
        Track newTrack = new Track();
        assertEquals(0, newTrack.getProgressPercent());
    }
    @Test
    public void setXpPerLesson_getXpPerLesson_works() {
        track.setXpPerLesson(20);
        assertEquals(20, track.getXpPerLesson());
    }
    @Test
    public void setTitle_getTitle_works() {
        track.setTitle("JavaScript");
        assertEquals("JavaScript", track.getTitle());
    }
    @Test
    public void setDescription_getDescription_works() {
        track.setDescription("New description");
        assertEquals("New description", track.getDescription());
    }
    @Test
    public void setDifficulty_getDifficulty_works() {
        track.setDifficulty("ADVANCED");
        assertEquals("ADVANCED", track.getDifficulty());
    }
    @Test
    public void setModuleCount_getModuleCount_works() {
        track.setModuleCount(12);
        assertEquals(12, track.getModuleCount());
    }
    @Test
    public void setLessonCount_getLessonCount_works() {
        track.setLessonCount(48);
        assertEquals(48, track.getLessonCount());
    }
    @Test
    public void setChallengeCount_getChallengeCount_works() {
        track.setChallengeCount(30);
        assertEquals(30, track.getChallengeCount());
    }
}
