package com.codequest.model;
import com.google.gson.annotations.SerializedName;
import java.util.List;
public class Submission {
    @SerializedName("id")
    private long id;
    @SerializedName("challengeId")
    private long challengeId;
    @SerializedName("status")
    private String status; 
    @SerializedName("output")
    private String output;
    @SerializedName("errorMessage")
    private String errorMessage;
    @SerializedName("executionTimeMs")
    private long executionTimeMs;
    @SerializedName("memoryUsedMb")
    private double memoryUsedMb;
    @SerializedName("testCasesPassed")
    private int testCasesPassed;
    @SerializedName("testCasesTotal")
    private int testCasesTotal;
    @SerializedName("score")
    private int score; 
    @SerializedName("grade")
    private String grade; 
    @SerializedName("testResults")
    private List<TestCaseResult> testResults;
    public Submission() {
    }
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public long getChallengeId() {
        return challengeId;
    }
    public void setChallengeId(long challengeId) {
        this.challengeId = challengeId;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getOutput() {
        return output;
    }
    public void setOutput(String output) {
        this.output = output;
    }
    public String getErrorMessage() {
        return errorMessage;
    }
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
    public long getExecutionTimeMs() {
        return executionTimeMs;
    }
    public void setExecutionTimeMs(long executionTimeMs) {
        this.executionTimeMs = executionTimeMs;
    }
    public double getMemoryUsedMb() {
        return memoryUsedMb;
    }
    public void setMemoryUsedMb(double memoryUsedMb) {
        this.memoryUsedMb = memoryUsedMb;
    }
    public int getTestCasesPassed() {
        return testCasesPassed;
    }
    public void setTestCasesPassed(int testCasesPassed) {
        this.testCasesPassed = testCasesPassed;
    }
    public int getTestCasesTotal() {
        return testCasesTotal;
    }
    public void setTestCasesTotal(int testCasesTotal) {
        this.testCasesTotal = testCasesTotal;
    }
    public int getScore() {
        return score;
    }
    public void setScore(int score) {
        this.score = score;
    }
    public String getGrade() {
        return grade;
    }
    public void setGrade(String grade) {
        this.grade = grade;
    }
    public List<TestCaseResult> getTestResults() {
        return testResults;
    }
    public void setTestResults(List<TestCaseResult> testResults) {
        this.testResults = testResults;
    }
    public boolean isAccepted() {
        return "ACCEPTED".equals(status);
    }
    public boolean isError() {
        return "RUNTIME_ERROR".equals(status) || "COMPILE_ERROR".equals(status);
    }
}
