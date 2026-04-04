package com.codequest.model;

import com.google.gson.annotations.SerializedName;

public class UserStats {

    @SerializedName("xp")
    private int xp;
    
    @SerializedName("level")
    private int level;
    
    @SerializedName("levelName")
    private String levelName;
    
    @SerializedName("streak")
    private int streak;
    
    @SerializedName("totalLessonsCompleted")
    private int totalLessonsCompleted;
    
    @SerializedName("totalChallengesSolved")
    private int totalChallengesSolved;
    
    @SerializedName("totalBadges")
    private int totalBadges;
    
    @SerializedName("memberSince")
    private String memberSince;

    public UserStats() {
    }

    public int getXp() {
        return xp;
    }

    public int getLevel() {
        return level;
    }

    public String getLevelName() {
        return levelName;
    }

    public int getStreak() {
        return streak;
    }

    public int getTotalLessonsCompleted() {
        return totalLessonsCompleted;
    }

    public int getTotalChallengesSolved() {
        return totalChallengesSolved;
    }

    public int getTotalBadges() {
        return totalBadges;
    }

    public String getMemberSince() {
        return memberSince;
    }
}
