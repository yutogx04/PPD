package com.codequest.model;
import com.google.gson.annotations.SerializedName;
import java.util.List;
public class QuizQuestion {
    @SerializedName("id")
    private long id;
    @SerializedName("question")
    private String question;
    @SerializedName("codeSnippet")
    private String codeSnippet; 
    @SerializedName("correctAnswer")
    private String correctAnswer;
    @SerializedName("wrongAnswer1")
    private String wrongAnswer1;
    @SerializedName("wrongAnswer2")
    private String wrongAnswer2;
    @SerializedName("wrongAnswer3")
    private String wrongAnswer3;
    @SerializedName("explanation")
    private String explanation;

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public String getQuestion() { return question; }
    public void setQuestion(String question) { this.question = question; }
    public String getCodeSnippet() { return codeSnippet; }
    public void setCodeSnippet(String codeSnippet) { this.codeSnippet = codeSnippet; }
    public String getCorrectAnswer() { return correctAnswer; }
    public void setCorrectAnswer(String correctAnswer) { this.correctAnswer = correctAnswer; }
    public String getWrongAnswer1() { return wrongAnswer1; }
    public void setWrongAnswer1(String wrongAnswer1) { this.wrongAnswer1 = wrongAnswer1; }
    public String getWrongAnswer2() { return wrongAnswer2; }
    public void setWrongAnswer2(String wrongAnswer2) { this.wrongAnswer2 = wrongAnswer2; }
    public String getWrongAnswer3() { return wrongAnswer3; }
    public void setWrongAnswer3(String wrongAnswer3) { this.wrongAnswer3 = wrongAnswer3; }
    public String getExplanation() { return explanation; }
    public void setExplanation(String explanation) { this.explanation = explanation; }
}
