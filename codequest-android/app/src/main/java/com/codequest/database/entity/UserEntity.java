package com.codequest.database.entity;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
@Entity(tableName = "users")
public class UserEntity {
    @PrimaryKey
    private long id;
    private String email;
    private String pseudo;
    private String avatarUrl;
    private int xp;
    private int level;
    private int streak;
    private int totalLessonsCompleted;
    private int totalChallengesSolved;
    private long lastUpdated;
    public UserEntity() {}
    public long getId() { return id; }
    public String getEmail() { return email; }
    public String getPseudo() { return pseudo; }
    public String getAvatarUrl() { return avatarUrl; }
    public int getXp() { return xp; }
    public int getLevel() { return level; }
    public int getStreak() { return streak; }
    public int getTotalLessonsCompleted() { return totalLessonsCompleted; }
    public int getTotalChallengesSolved() { return totalChallengesSolved; }
    public long getLastUpdated() { return lastUpdated; }
    public void setId(long id) { this.id = id; }
    public void setEmail(String email) { this.email = email; }
    public void setPseudo(String pseudo) { this.pseudo = pseudo; }
    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }
    public void setXp(int xp) { this.xp = xp; }
    public void setLevel(int level) { this.level = level; }
    public void setStreak(int streak) { this.streak = streak; }
    public void setTotalLessonsCompleted(int c) { this.totalLessonsCompleted = c; }
    public void setTotalChallengesSolved(int c) { this.totalChallengesSolved = c; }
    public void setLastUpdated(long lastUpdated) { this.lastUpdated = lastUpdated; }
}
