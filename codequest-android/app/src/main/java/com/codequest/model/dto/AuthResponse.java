package com.codequest.model.dto;
import com.codequest.model.User;
import com.google.gson.annotations.SerializedName;
public class AuthResponse {
    @SerializedName("accessToken")
    private String accessToken;
    @SerializedName("refreshToken")
    private String refreshToken;
    @SerializedName("user")
    private User user;
    @SerializedName("error")
    private String error;
    public AuthResponse() {
    }
    public String getAccessToken() {
        return accessToken;
    }
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
    public String getRefreshToken() {
        return refreshToken;
    }
    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
    public String getError() {
        return error;
    }
    public void setError(String error) {
        this.error = error;
    }
}
