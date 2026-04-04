package com.codequest.model;
import com.google.gson.annotations.SerializedName;
public class Notification {
    @SerializedName("id")
    private long id;
    @SerializedName("type")
    private String type; 
    @SerializedName("title")
    private String title;
    @SerializedName("message")
    private String message;
    @SerializedName("isRead")
    private boolean isRead;
    @SerializedName("createdAt")
    private String createdAt;
    public Notification() {
    }
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public boolean isRead() {
        return isRead;
    }
    public void setRead(boolean read) {
        isRead = read;
    }
    public String getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
