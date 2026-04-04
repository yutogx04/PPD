package com.codequest.model.dto;
import com.codequest.model.Badge;
import com.google.gson.annotations.SerializedName;
import java.util.List;
public class GamificationResult {
    @SerializedName("xpGained")
    private int xpGained;
    @SerializedName("newXpTotal")
    private int newXpTotal;
    @SerializedName("newLevel")
    private int newLevel;
    @SerializedName("leveledUp")
    private boolean leveledUp;
    @SerializedName("badgesUnlocked")
    private List<Badge> badgesUnlocked;
    public GamificationResult() {
    }
    public int getXpGained() {
        return xpGained;
    }
    public int getNewXpTotal() {
        return newXpTotal;
    }
    public int getNewLevel() {
        return newLevel;
    }
    public boolean isLeveledUp() {
        return leveledUp;
    }
    public List<Badge> getBadgesUnlocked() {
        return badgesUnlocked;
    }
    public boolean hasBadges() {
        return badgesUnlocked != null && !badgesUnlocked.isEmpty();
    }
}
