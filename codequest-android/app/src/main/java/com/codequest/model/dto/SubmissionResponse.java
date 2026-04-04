package com.codequest.model.dto;
import com.codequest.model.Badge;
import com.codequest.model.TestCaseResult;
import com.google.gson.annotations.SerializedName;
import java.util.List;
public class SubmissionResponse {
    @SerializedName("status")
    private String status;
    @SerializedName("output")
    private String output;
    @SerializedName("errorMessage")
    private String errorMessage;
    @SerializedName("testCasesPassed")
    private int testCasesPassed;
    @SerializedName("testCasesTotal")
    private int testCasesTotal;
    @SerializedName("score")
    private int score;
    @SerializedName("grade")
    private String grade;
    @SerializedName("xpGained")
    private int xpGained;
    @SerializedName("bonusXp")
    private int bonusXp;
    @SerializedName("badgesUnlocked")
    private List<Badge> badgesUnlocked;
    @SerializedName("testResults")
    private List<TestCaseResult> testResults;
    public SubmissionResponse() {
    }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getOutput() { return output; }
    public void setOutput(String output) { this.output = output; }
    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
    public int getTestCasesPassed() { return testCasesPassed; }
    public void setTestCasesPassed(int testCasesPassed) { this.testCasesPassed = testCasesPassed; }
    public int getTestCasesTotal() { return testCasesTotal; }
    public void setTestCasesTotal(int testCasesTotal) { this.testCasesTotal = testCasesTotal; }
    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }
    public String getGrade() { return grade; }
    public void setGrade(String grade) { this.grade = grade; }
    public int getXpGained() { return xpGained; }
    public void setXpGained(int xpGained) { this.xpGained = xpGained; }
    public int getBonusXp() { return bonusXp; }
    public void setBonusXp(int bonusXp) { this.bonusXp = bonusXp; }
    public List<TestCaseResult> getTestResults() { return testResults; }
    public void setTestResults(List<TestCaseResult> testResults) { this.testResults = testResults; }
    public int getTotalXp() {
        return xpGained + bonusXp;
    }
    public boolean hasBadges() {
        return badgesUnlocked != null && !badgesUnlocked.isEmpty();
    }
    public List<Badge> getBadgesUnlocked() {
        return badgesUnlocked;
    }
    public void setBadgesUnlocked(List<Badge> badgesUnlocked) { this.badgesUnlocked = badgesUnlocked; }
    public boolean isAccepted() {
        return "ACCEPTED".equalsIgnoreCase(status);
    }
}
