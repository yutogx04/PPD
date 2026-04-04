package com.codequest.model;
import com.google.gson.annotations.SerializedName;
public class Module {
    @SerializedName("id")
    private long id;
    @SerializedName("trackId")
    private long trackId;
    @SerializedName("title")
    private String title;
    @SerializedName("description")
    private String description;
    @SerializedName("orderIndex")
    private int orderIndex;
    @SerializedName("xpReward")
    private int xpReward;
    @SerializedName("lessonCount")
    private int lessonCount;
    @SerializedName("challengeCount")
    private int challengeCount;
    @SerializedName("completedLessons")
    private int completedLessons;
    public Module() {
    }
    public Module(long id, long trackId, String title, String description, int orderIndex, int lessonCount) {
        this.id = id;
        this.trackId = trackId;
        this.title = title;
        this.description = description;
        this.orderIndex = orderIndex;
        this.lessonCount = lessonCount;
    }
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public long getTrackId() {
        return trackId;
    }
    public void setTrackId(long trackId) {
        this.trackId = trackId;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public int getOrderIndex() {
        return orderIndex;
    }
    public void setOrderIndex(int orderIndex) {
        this.orderIndex = orderIndex;
    }
    public int getXpReward() {
        return xpReward;
    }
    public void setXpReward(int xpReward) {
        this.xpReward = xpReward;
    }
    public int getLessonCount() {
        return lessonCount;
    }
    public void setLessonCount(int lessonCount) {
        this.lessonCount = lessonCount;
    }
    public int getChallengeCount() {
        return challengeCount;
    }
    public void setChallengeCount(int challengeCount) {
        this.challengeCount = challengeCount;
    }
    public int getCompletedLessons() {
        return completedLessons;
    }
    public void setCompletedLessons(int completedLessons) {
        this.completedLessons = completedLessons;
    }
    public int getProgressPercent() {
        if (lessonCount == 0)
            return 0;
        return (completedLessons * 100) / lessonCount;
    }
}
