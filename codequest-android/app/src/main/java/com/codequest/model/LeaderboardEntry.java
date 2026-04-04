package com.codequest.model;
import com.google.gson.annotations.SerializedName;
public class LeaderboardEntry {
    @SerializedName("rank")
    private int rank;
    @SerializedName("pseudo")
    private String pseudo;
    @SerializedName("avatarUrl")
    private String avatarUrl;
    @SerializedName("level")
    private int level;
    @SerializedName("xp")
    private int xp;
    @SerializedName("isCurrentUser")
    private boolean isCurrentUser;
    public LeaderboardEntry() {
    }
    public LeaderboardEntry(int rank, String pseudo, int level, int xp, boolean isCurrentUser) {
        this.rank = rank;
        this.pseudo = pseudo;
        this.level = level;
        this.xp = xp;
        this.isCurrentUser = isCurrentUser;
    }
    public int getRank() {
        return rank;
    }
    public void setRank(int rank) {
        this.rank = rank;
    }
    public String getPseudo() {
        return pseudo;
    }
    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }
    public String getAvatarUrl() {
        return avatarUrl;
    }
    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
    public int getLevel() {
        return level;
    }
    public void setLevel(int level) {
        this.level = level;
    }
    public int getXp() {
        return xp;
    }
    public void setXp(int xp) {
        this.xp = xp;
    }
    public boolean isCurrentUser() {
        return isCurrentUser;
    }
    public void setCurrentUser(boolean currentUser) {
        isCurrentUser = currentUser;
    }
    public String getXpFormatted() {
        if (xp >= 1000) {
            return String.format("%,d XP", xp);
        }
        return xp + " XP";
    }
    public boolean isPodium() {
        return rank >= 1 && rank <= 3;
    }
}
