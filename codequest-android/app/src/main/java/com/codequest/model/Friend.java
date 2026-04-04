package com.codequest.model;
import com.google.gson.annotations.SerializedName;
public class Friend {
    @SerializedName("id")
    private long id;
    @SerializedName("pseudo")
    private String pseudo;
    @SerializedName("avatarUrl")
    private String avatarUrl;
    @SerializedName("level")
    private int level;
    @SerializedName("xp")
    private int xp;
    @SerializedName("streak")
    private int streak;
    @SerializedName("friendshipStatus")
    private String friendshipStatus; 
    @SerializedName("lastActivity")
    private String lastActivity; 
    public Friend() {
    }
    public Friend(long id, String pseudo, int level, int xp, int streak, String friendshipStatus) {
        this.id = id;
        this.pseudo = pseudo;
        this.level = level;
        this.xp = xp;
        this.streak = streak;
        this.friendshipStatus = friendshipStatus;
    }
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
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
    public int getStreak() {
        return streak;
    }
    public void setStreak(int streak) {
        this.streak = streak;
    }
    public String getFriendshipStatus() {
        return friendshipStatus;
    }
    public void setFriendshipStatus(String friendshipStatus) {
        this.friendshipStatus = friendshipStatus;
    }
    public String getLastActivity() {
        return lastActivity;
    }
    public void setLastActivity(String lastActivity) {
        this.lastActivity = lastActivity;
    }
    public boolean isPending() {
        return "PENDING".equals(friendshipStatus);
    }
    public boolean isAccepted() {
        return "ACCEPTED".equals(friendshipStatus);
    }
}
