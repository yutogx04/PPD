package com.codequest;
import com.codequest.model.User;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
public class UserTest {
    private User user;
    @Before
    public void setUp() {
        user = new User(1L, "TestWizard", 840, 3, 12);
    }
    @Test
    public void constructor_setsAllFields() {
        assertEquals(1L, user.getId());
        assertEquals("TestWizard", user.getPseudo());
        assertEquals(840, user.getXp());
        assertEquals(3, user.getLevel());
        assertEquals(12, user.getStreak());
    }
    @Test
    public void defaultConstructor_createsEmptyUser() {
        User empty = new User();
        assertEquals(0, empty.getId());
        assertNull(empty.getPseudo());
        assertEquals(0, empty.getXp());
        assertEquals(0, empty.getLevel());
        assertEquals(0, empty.getStreak());
    }
    @Test
    public void setEmail_getEmail_works() {
        user.setEmail("test@codequest.com");
        assertEquals("test@codequest.com", user.getEmail());
    }
    @Test
    public void setAvatarUrl_getAvatarUrl_works() {
        user.setAvatarUrl("https://example.com/avatar.png");
        assertEquals("https://example.com/avatar.png", user.getAvatarUrl());
    }
    @Test
    public void setBio_getBio_works() {
        user.setBio("J'aime coder !");
        assertEquals("J'aime coder !", user.getBio());
    }
    @Test
    public void setXp_getXp_works() {
        user.setXp(1500);
        assertEquals(1500, user.getXp());
    }
    @Test
    public void setLevel_getLevel_works() {
        user.setLevel(5);
        assertEquals(5, user.getLevel());
    }
    @Test
    public void setStreak_getStreak_works() {
        user.setStreak(30);
        assertEquals(30, user.getStreak());
    }
    @Test
    public void setTotalLessonsCompleted_works() {
        user.setTotalLessonsCompleted(18);
        assertEquals(18, user.getTotalLessonsCompleted());
    }
    @Test
    public void setTotalChallengesSolved_works() {
        user.setTotalChallengesSolved(9);
        assertEquals(9, user.getTotalChallengesSolved());
    }
    @Test
    public void getLevelTitle_level1_returnsDebutant() {
        user.setLevel(1);
        assertEquals("Beginner", user.getLevelTitle());
    }
    @Test
    public void getLevelTitle_level2_returnsNovice() {
        user.setLevel(2);
        assertEquals("Novice", user.getLevelTitle());
    }
    @Test
    public void getLevelTitle_level3_returnsApprenti() {
        user.setLevel(3);
        assertEquals("Apprentice", user.getLevelTitle());
    }
    @Test
    public void getLevelTitle_level4_returnsDeveloppeur() {
        user.setLevel(4);
        assertEquals("Developer", user.getLevelTitle());
    }
    @Test
    public void getLevelTitle_level5_returnsExpert() {
        user.setLevel(5);
        assertEquals("Expert", user.getLevelTitle());
    }
    @Test
    public void getLevelTitle_level6_returnsMaitre() {
        user.setLevel(6);
        assertEquals("Master", user.getLevelTitle());
    }
    @Test
    public void getLevelTitle_invalidLevel_returnsDebutant() {
        user.setLevel(99);
        assertEquals("Beginner", user.getLevelTitle());
    }
    @Test
    public void getLevelTitle_zeroLevel_returnsDebutant() {
        user.setLevel(0);
        assertEquals("Beginner", user.getLevelTitle());
    }
}
