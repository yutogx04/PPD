package com.codequest.model.dto;
public class VerifyOTPRequest {
    private String email;
    private String otpCode;
    public VerifyOTPRequest(String email, String otpCode) {
        this.email = email;
        this.otpCode = otpCode;
    }
    public String getEmail() {
        return email;
    }
    public String getOtpCode() {
        return otpCode;
    }
}
