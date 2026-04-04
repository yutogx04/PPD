package com.codequest.model;
import com.google.gson.annotations.SerializedName;
public class Track {
    @SerializedName("id")
    private long id;
    @SerializedName("title")
    private String title;
    @SerializedName("description")
    private String description;
    @SerializedName("difficulty")
    private String difficulty; 
    @SerializedName("language")
    private String language; 
    @SerializedName("moduleCount")
    private int moduleCount;
    @SerializedName("lessonCount")
    private int lessonCount;
    @SerializedName("challengeCount")
    private int challengeCount;
    @SerializedName("progressPercent")
    private int progressPercent;
    @SerializedName("isLocked")
    private boolean isLocked;
    @SerializedName("requiredLevel")
    private int requiredLevel;
    @SerializedName("xpPerLesson")
    private int xpPerLesson;
    public Track() {
    }
    public Track(long id, String title, String description, String difficulty, String language,
            int moduleCount, int lessonCount, int challengeCount, int progressPercent, boolean isLocked) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.difficulty = difficulty;
        this.language = language;
        this.moduleCount = moduleCount;
        this.lessonCount = lessonCount;
        this.challengeCount = challengeCount;
        this.progressPercent = progressPercent;
        this.isLocked = isLocked;
    }
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
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
    public String getDifficulty() {
        return difficulty;
    }
    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }
    public String getLanguage() {
        return language;
    }
    public void setLanguage(String language) {
        this.language = language;
    }
    public int getModuleCount() {
        return moduleCount;
    }
    public void setModuleCount(int moduleCount) {
        this.moduleCount = moduleCount;
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
    public int getProgressPercent() {
        return progressPercent;
    }
    public void setProgressPercent(int progressPercent) {
        this.progressPercent = progressPercent;
    }
    public boolean isLocked() {
        return isLocked;
    }
    public void setLocked(boolean locked) {
        isLocked = locked;
    }
    public int getRequiredLevel() {
        return requiredLevel;
    }
    public void setRequiredLevel(int requiredLevel) {
        this.requiredLevel = requiredLevel;
    }
    public int getXpPerLesson() {
        return xpPerLesson;
    }
    public void setXpPerLesson(int xpPerLesson) {
        this.xpPerLesson = xpPerLesson;
    }
    public String getLanguageShort() {
        switch (language) {
            case "PYTHON":
                return "Py";
            case "JAVASCRIPT":
                return "JS";
            case "JAVA":
                return "Jv";
            default:
                return language;
        }
    }
}
