package com.codequest.model;
import com.google.gson.annotations.SerializedName;
public class DailyChallenge {
    @SerializedName("challengeId")
    private long challengeId;
    @SerializedName("title")
    private String title;
    @SerializedName("language")
    private String language;
    @SerializedName("difficulty")
    private String difficulty;
    @SerializedName("xpReward")
    private int xpReward;
    @SerializedName("isCompleted")
    private boolean isCompleted;
    public DailyChallenge() {
    }
    public DailyChallenge(long challengeId, String title, String language, String difficulty, int xpReward) {
        this.challengeId = challengeId;
        this.title = title;
        this.language = language;
        this.difficulty = difficulty;
        this.xpReward = xpReward;
    }
    public long getChallengeId() {
        return challengeId;
    }
    public void setChallengeId(long challengeId) {
        this.challengeId = challengeId;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getLanguage() {
        return language;
    }
    public void setLanguage(String language) {
        this.language = language;
    }
    public String getDifficulty() {
        return difficulty;
    }
    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }
    public int getXpReward() {
        return xpReward;
    }
    public void setXpReward(int xpReward) {
        this.xpReward = xpReward;
    }
    public boolean isCompleted() {
        return isCompleted;
    }
    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }
}
