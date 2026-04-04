package com.codequest.database.entity;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
@Entity(tableName = "tracks")
public class TrackEntity {
    @PrimaryKey
    private long id;
    private String title;
    private String description;
    private String difficulty;
    private String language;
    private int moduleCount;
    private int lessonCount;
    private int challengeCount;
    private int progressPercent;
    private boolean isLocked;
    private int requiredLevel;
    private long lastUpdated;
    public TrackEntity() {}
    public long getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getDifficulty() { return difficulty; }
    public String getLanguage() { return language; }
    public int getModuleCount() { return moduleCount; }
    public int getLessonCount() { return lessonCount; }
    public int getChallengeCount() { return challengeCount; }
    public int getProgressPercent() { return progressPercent; }
    public boolean isLocked() { return isLocked; }
    public int getRequiredLevel() { return requiredLevel; }
    public long getLastUpdated() { return lastUpdated; }
    public void setId(long id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }
    public void setLanguage(String language) { this.language = language; }
    public void setModuleCount(int moduleCount) { this.moduleCount = moduleCount; }
    public void setLessonCount(int lessonCount) { this.lessonCount = lessonCount; }
    public void setChallengeCount(int challengeCount) { this.challengeCount = challengeCount; }
    public void setProgressPercent(int progressPercent) { this.progressPercent = progressPercent; }
    public void setLocked(boolean locked) { isLocked = locked; }
    public void setRequiredLevel(int requiredLevel) { this.requiredLevel = requiredLevel; }
    public void setLastUpdated(long lastUpdated) { this.lastUpdated = lastUpdated; }
}
