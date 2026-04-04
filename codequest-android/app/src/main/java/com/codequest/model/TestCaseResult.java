package com.codequest.model;
import com.google.gson.annotations.SerializedName;
public class TestCaseResult {
    @SerializedName("input")
    private String input;
    @SerializedName("expectedOutput")
    private String expectedOutput;
    @SerializedName("actualOutput")
    private String actualOutput;
    @SerializedName("passed")
    private boolean passed;
    @SerializedName("timeMs")
    private long timeMs;
    public TestCaseResult() {
    }
    public TestCaseResult(String input, String expectedOutput, String actualOutput, boolean passed, long timeMs) {
        this.input = input;
        this.expectedOutput = expectedOutput;
        this.actualOutput = actualOutput;
        this.passed = passed;
        this.timeMs = timeMs;
    }
    public String getInput() {
        return input;
    }
    public void setInput(String input) {
        this.input = input;
    }
    public String getExpectedOutput() {
        return expectedOutput;
    }
    public void setExpectedOutput(String expectedOutput) {
        this.expectedOutput = expectedOutput;
    }
    public String getActualOutput() {
        return actualOutput;
    }
    public void setActualOutput(String actualOutput) {
        this.actualOutput = actualOutput;
    }
    public boolean isPassed() {
        return passed;
    }
    public void setPassed(boolean passed) {
        this.passed = passed;
    }
    public long getTimeMs() {
        return timeMs;
    }
    public void setTimeMs(long timeMs) {
        this.timeMs = timeMs;
    }
    public String getDisplayResult() {
        return "\"" + input + "\" → \"" + actualOutput + "\"";
    }
}
