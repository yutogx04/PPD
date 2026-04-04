package com.codequest.model;
import com.google.gson.annotations.SerializedName;
public class LessonSlide {
    @SerializedName("id")
    private long id;
    @SerializedName("orderIndex")
    private int orderIndex;
    @SerializedName("contentType")
    private String contentType; 
    @SerializedName("contentText")
    private String contentText;
    @SerializedName("codeSnippet")
    private String codeSnippet;
    @SerializedName("codeLanguage")
    private String codeLanguage; 
    @SerializedName("explanation")
    private String explanation; 
    @SerializedName("quizQuestion")
    private QuizQuestion quizQuestion; 
    public LessonSlide() {
    }
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public int getOrderIndex() {
        return orderIndex;
    }
    public void setOrderIndex(int orderIndex) {
        this.orderIndex = orderIndex;
    }
    public String getContentType() {
        return contentType;
    }
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
    public String getContentText() {
        return contentText;
    }
    public void setContentText(String contentText) {
        this.contentText = contentText;
    }
    public String getCodeSnippet() {
        return codeSnippet;
    }
    public void setCodeSnippet(String codeSnippet) {
        this.codeSnippet = codeSnippet;
    }
    public String getCodeLanguage() {
        return codeLanguage;
    }
    public void setCodeLanguage(String codeLanguage) {
        this.codeLanguage = codeLanguage;
    }
    public String getExplanation() {
        return explanation;
    }
    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }
    public QuizQuestion getQuizQuestion() {
        return quizQuestion;
    }
    public void setQuizQuestion(QuizQuestion quizQuestion) {
        this.quizQuestion = quizQuestion;
    }
    public boolean isTextSlide() {
        return "TEXT".equals(contentType);
    }
    public boolean isCodeSlide() {
        return "CODE".equals(contentType);
    }
    public boolean isQcmSlide() {
        return "QCM".equals(contentType);
    }
}
