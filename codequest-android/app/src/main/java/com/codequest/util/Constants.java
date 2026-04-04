package com.codequest.util;
public class Constants {
    public static final String BASE_URL = "http://192.168.1.3:8080/"; 
    public static final int CONNECT_TIMEOUT = 15; 
    public static final int READ_TIMEOUT = 30; 
    public static final int LEVEL_1_XP = 0; 
    public static final int LEVEL_2_XP = 200; 
    public static final int LEVEL_3_XP = 500; 
    public static final int LEVEL_4_XP = 1000; 
    public static final int LEVEL_5_XP = 2000; 
    public static final int LEVEL_6_XP = 4000; 
    public static final int XP_LESSON_COMPLETE = 20;
    public static final int XP_MODULE_COMPLETE = 50;
    public static final int XP_CHALLENGE_EASY = 30;
    public static final int XP_CHALLENGE_MEDIUM = 50;
    public static final int XP_CHALLENGE_HARD = 80;
    public static final int XP_FIRST_ATTEMPT_BONUS = 25;
    public static final int XP_DAILY_CHALLENGE_BONUS = 50;
    public static final int XP_STREAK_7_DAYS_BONUS = 100;
    public static final int GRADE_S_MIN = 90;
    public static final int GRADE_A_MIN = 70;
    public static final int GRADE_B_MIN = 50;
    public static final int GRADE_C_MIN = 30;
    public static final int SUBMIT_COOLDOWN_MS = 10000; 
    public static final int MAX_CODE_LENGTH = 10000; 
    public static final int HINT_THRESHOLD = 3; 
    public static final int SOLUTION_THRESHOLD = 5; 
    public static final int JAVA_REQUIRED_LEVEL = 5;
    public static String getGrade(int score) {
        if (score >= GRADE_S_MIN)
            return "S";
        if (score >= GRADE_A_MIN)
            return "A";
        if (score >= GRADE_B_MIN)
            return "B";
        if (score >= GRADE_C_MIN)
            return "C";
        return "D";
    }
    public static int getLevelForXp(int xp) {
        if (xp >= LEVEL_6_XP)
            return 6;
        if (xp >= LEVEL_5_XP)
            return 5;
        if (xp >= LEVEL_4_XP)
            return 4;
        if (xp >= LEVEL_3_XP)
            return 3;
        if (xp >= LEVEL_2_XP)
            return 2;
        return 1;
    }
    public static int getXpForNextLevel(int currentLevel) {
        switch (currentLevel) {
            case 1:
                return LEVEL_2_XP;
            case 2:
                return LEVEL_3_XP;
            case 3:
                return LEVEL_4_XP;
            case 4:
                return LEVEL_5_XP;
            case 5:
                return LEVEL_6_XP;
            default:
                return LEVEL_6_XP;
        }
    }
    public static int getXpForLevel(int level) {
        return getXpForNextLevel(level - 1);
    }
}
