package com.codequest.model;
import com.google.gson.annotations.SerializedName;
public class Badge {
    @SerializedName("id")
    private long id;
    @SerializedName("name")
    private String name;
    @SerializedName("description")
    private String description;
    @SerializedName("icon")
    private String icon; 
    @SerializedName(value="earned", alternate={"isEarned"})
    private boolean isEarned;
    @SerializedName("obtainedAt")
    private String obtainedAt;
    public Badge() {
    }
    public Badge(long id, String name, String description, String icon, boolean isEarned) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.icon = icon;
        this.isEarned = isEarned;
    }
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getIcon() {
        return icon;
    }
    public void setIcon(String icon) {
        this.icon = icon;
    }
    public boolean isEarned() {
        return isEarned;
    }
    public void setEarned(boolean earned) {
        isEarned = earned;
    }
    public String getObtainedAt() {
        return obtainedAt;
    }
    public void setObtainedAt(String obtainedAt) {
        this.obtainedAt = obtainedAt;
    }
}
