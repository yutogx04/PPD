package com.codequest.service;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GamificationServiceTest {

    @Test
    void calculateLevel_zeroXp_isLevel1() {
        assertEquals(1, GamificationService.calculateLevel(0));
    }

    @Test
    void calculateLevel_199xp_isLevel1() {
        assertEquals(1, GamificationService.calculateLevel(199));
    }

    @Test
    void calculateLevel_200xp_isLevel2() {
        assertEquals(2, GamificationService.calculateLevel(200));
    }

    @Test
    void calculateLevel_499xp_isLevel2() {
        assertEquals(2, GamificationService.calculateLevel(499));
    }

    @Test
    void calculateLevel_500xp_isLevel3() {
        assertEquals(3, GamificationService.calculateLevel(500));
    }

    @Test
    void calculateLevel_999xp_isLevel3() {
        assertEquals(3, GamificationService.calculateLevel(999));
    }

    @Test
    void calculateLevel_1000xp_isLevel4() {
        assertEquals(4, GamificationService.calculateLevel(1000));
    }

    @Test
    void calculateLevel_2000xp_isLevel5() {
        assertEquals(5, GamificationService.calculateLevel(2000));
    }

    @Test
    void calculateLevel_4000xp_isLevel6() {
        assertEquals(6, GamificationService.calculateLevel(4000));
    }

    @Test
    void calculateLevel_10000xp_isLevel6() {
        assertEquals(6, GamificationService.calculateLevel(10000));
    }

    @Test
    void getLevelName_level1_isDebutant() {
        assertEquals("Beginner", GamificationService.getLevelName(1));
    }

    @Test
    void getLevelName_level2_isNovice() {
        assertEquals("Novice", GamificationService.getLevelName(2));
    }

    @Test
    void getLevelName_level3_isApprenti() {
        assertEquals("Apprentice", GamificationService.getLevelName(3));
    }

    @Test
    void getLevelName_level4_isDeveloppeur() {
        assertEquals("Developer", GamificationService.getLevelName(4));
    }

    @Test
    void getLevelName_level5_isExpert() {
        assertEquals("Expert", GamificationService.getLevelName(5));
    }

    @Test
    void getLevelName_level6_isMaitre() {
        assertEquals("Master", GamificationService.getLevelName(6));
    }

    @Test
    void getLevelName_level0_returnsMaitre() {
        
        assertEquals("Master", GamificationService.getLevelName(0));
    }

    @Test
    void getLevelName_level99_returnsMaitre() {
        
        assertEquals("Master", GamificationService.getLevelName(99));
    }
}
