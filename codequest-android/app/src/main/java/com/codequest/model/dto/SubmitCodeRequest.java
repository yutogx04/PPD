package com.codequest.model.dto;
public class SubmitCodeRequest {
    private String code;
    private String language;
    public SubmitCodeRequest(String code, String language) {
        this.code = code;
        this.language = language;
    }
    public String getCode() {
        return code;
    }
    public String getLanguage() {
        return language;
    }
}
