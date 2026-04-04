package com.codequest.model;
import com.google.gson.annotations.SerializedName;
public class Challenge {
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
    @SerializedName("starterCode")
    private String starterCode;
    @SerializedName("hint")
    private String hint;
    @SerializedName("xpReward")
    private int xpReward;
    @SerializedName("exampleInput")
    private String exampleInput;
    @SerializedName("exampleOutput")
    private String exampleOutput;
    @SerializedName("exampleInput2")
    private String exampleInput2;
    @SerializedName("exampleOutput2")
    private String exampleOutput2;
    @SerializedName("isSolved")
    private boolean isSolved;
    @SerializedName("bestScore")
    private int bestScore;
    @SerializedName("bestGrade")
    private String bestGrade;
    @SerializedName("attemptCount")
    private int attemptCount;
    public Challenge() {
    }
    public Challenge(long id, String title, String description, String difficulty, String language,
            String starterCode, int xpReward) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.difficulty = difficulty;
        this.language = language;
        this.starterCode = starterCode;
        this.xpReward = xpReward;
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
    public String getStarterCode() {
        return starterCode;
    }
    public void setStarterCode(String starterCode) {
        this.starterCode = starterCode;
    }
    public String getHint() {
        return hint;
    }
    public void setHint(String hint) {
        this.hint = hint;
    }
    public int getXpReward() {
        return xpReward;
    }
    public void setXpReward(int xpReward) {
        this.xpReward = xpReward;
    }
    public String getExampleInput() {
        return exampleInput;
    }
    public void setExampleInput(String exampleInput) {
        this.exampleInput = exampleInput;
    }
    public String getExampleOutput() {
        return exampleOutput;
    }
    public void setExampleOutput(String exampleOutput) {
        this.exampleOutput = exampleOutput;
    }
    public String getExampleInput2() {
        return exampleInput2;
    }
    public void setExampleInput2(String exampleInput2) {
        this.exampleInput2 = exampleInput2;
    }
    public String getExampleOutput2() {
        return exampleOutput2;
    }
    public void setExampleOutput2(String exampleOutput2) {
        this.exampleOutput2 = exampleOutput2;
    }
    public boolean isSolved() {
        return isSolved;
    }
    public void setSolved(boolean solved) {
        isSolved = solved;
    }
    public int getBestScore() {
        return bestScore;
    }
    public void setBestScore(int bestScore) {
        this.bestScore = bestScore;
    }
    public String getBestGrade() {
        return bestGrade;
    }
    public void setBestGrade(String bestGrade) {
        this.bestGrade = bestGrade;
    }
    public int getAttemptCount() {
        return attemptCount;
    }
    public void setAttemptCount(int attemptCount) {
        this.attemptCount = attemptCount;
    }
    public boolean isHintAvailable() {
        return attemptCount >= 3;
    }
    public boolean isSolutionAvailable() {
        return attemptCount >= 5;
    }
}
