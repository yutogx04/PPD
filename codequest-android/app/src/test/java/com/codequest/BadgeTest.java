package com.codequest;
import com.codequest.model.Badge;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
public class BadgeTest {
    private Badge badge;
    @Before
    public void setUp() {
        badge = new Badge(1L, "Premier Pas", "Complète ta première leçon",
                "fa-shoe-prints", false);
    }
    @Test
    public void constructor_setsAllFields() {
        assertEquals(1L, badge.getId());
        assertEquals("Premier Pas", badge.getName());
        assertEquals("Complète ta première leçon", badge.getDescription());
        assertEquals("fa-shoe-prints", badge.getIcon());
        assertFalse(badge.isEarned());
    }
    @Test
    public void defaultConstructor_createsEmpty() {
        Badge empty = new Badge();
        assertEquals(0, empty.getId());
        assertNull(empty.getName());
        assertFalse(empty.isEarned());
    }
    @Test
    public void setEarned_true_makesEarned() {
        badge.setEarned(true);
        assertTrue(badge.isEarned());
    }
    @Test
    public void setEarned_false_makesLocked() {
        badge.setEarned(true);
        badge.setEarned(false);
        assertFalse(badge.isEarned());
    }
    @Test
    public void obtainedAt_defaultNull() {
        assertNull(badge.getObtainedAt());
    }
    @Test
    public void setObtainedAt_works() {
        badge.setObtainedAt("2026-03-13T00:00:00Z");
        assertEquals("2026-03-13T00:00:00Z", badge.getObtainedAt());
    }
    @Test
    public void setName_works() {
        badge.setName("Streak Master");
        assertEquals("Streak Master", badge.getName());
    }
    @Test
    public void setDescription_works() {
        badge.setDescription("7 jours de suite");
        assertEquals("7 jours de suite", badge.getDescription());
    }
    @Test
    public void setIcon_works() {
        badge.setIcon("fa-fire");
        assertEquals("fa-fire", badge.getIcon());
    }
}
