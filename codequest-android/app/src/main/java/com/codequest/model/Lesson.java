package com.codequest.model;
import com.google.gson.annotations.SerializedName;
public class Lesson {
    @SerializedName("id")
    private long id;
    @SerializedName("moduleId")
    private long moduleId;
    @SerializedName("title")
    private String title;
    @SerializedName("lessonType")
    private String lessonType; 
    @SerializedName("orderIndex")
    private int orderIndex;
    @SerializedName("xpReward")
    private int xpReward;
    @SerializedName("estimatedMinutes")
    private int estimatedMinutes;
    @SerializedName("isCompleted")
    private boolean isCompleted;
    @SerializedName("isFavorite")
    private boolean isFavorite;
    @SerializedName("savedSlideIndex")
    private int savedSlideIndex; 
    @SerializedName("slideCount")
    private int slideCount;
    public Lesson() {
    }
    public Lesson(long id, long moduleId, String title, String lessonType, int xpReward, int estimatedMinutes) {
        this.id = id;
        this.moduleId = moduleId;
        this.title = title;
        this.lessonType = lessonType;
        this.xpReward = xpReward;
        this.estimatedMinutes = estimatedMinutes;
    }
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public long getModuleId() {
        return moduleId;
    }
    public void setModuleId(long moduleId) {
        this.moduleId = moduleId;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getLessonType() {
        return lessonType;
    }
    public void setLessonType(String lessonType) {
        this.lessonType = lessonType;
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
    public int getEstimatedMinutes() {
        return estimatedMinutes;
    }
    public void setEstimatedMinutes(int estimatedMinutes) {
        this.estimatedMinutes = estimatedMinutes;
    }
    public boolean isCompleted() {
        return isCompleted;
    }
    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }
    public boolean isFavorite() {
        return isFavorite;
    }
    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }
    public int getSavedSlideIndex() {
        return savedSlideIndex;
    }
    public void setSavedSlideIndex(int savedSlideIndex) {
        this.savedSlideIndex = savedSlideIndex;
    }
    public int getSlideCount() {
        return slideCount;
    }
    public void setSlideCount(int slideCount) {
        this.slideCount = slideCount;
    }
}
