package com.codequest.model;
import com.google.gson.annotations.SerializedName;
public class User {
    @SerializedName("id")
    private long id;
    @SerializedName("email")
    private String email;
    @SerializedName("pseudo")
    private String pseudo;
    @SerializedName("avatarUrl")
    private String avatarUrl;
    @SerializedName("bio")
    private String bio;
    @SerializedName("xp")
    private int xp;
    @SerializedName("level")
    private int level;
    @SerializedName("streak")
    private int streak;
    @SerializedName("totalLessonsCompleted")
    private int totalLessonsCompleted;
    @SerializedName("totalChallengesSolved")
    private int totalChallengesSolved;
    @SerializedName("role")
    private String role;
    public User() {
    }
    public User(long id, String pseudo, int xp, int level, int streak) {
        this.id = id;
        this.pseudo = pseudo;
        this.xp = xp;
        this.level = level;
        this.streak = streak;
    }
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPseudo() {
        return pseudo;
    }
    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }
    public String getAvatarUrl() {
        return avatarUrl;
    }
    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
    public String getBio() {
        return bio;
    }
    public void setBio(String bio) {
        this.bio = bio;
    }
    public int getXp() {
        return xp;
    }
    public void setXp(int xp) {
        this.xp = xp;
    }
    public int getLevel() {
        return level;
    }
    public void setLevel(int level) {
        this.level = level;
    }
    public int getStreak() {
        return streak;
    }
    public void setStreak(int streak) {
        this.streak = streak;
    }
    public int getTotalLessonsCompleted() {
        return totalLessonsCompleted;
    }
    public void setTotalLessonsCompleted(int totalLessonsCompleted) {
        this.totalLessonsCompleted = totalLessonsCompleted;
    }
    public int getTotalChallengesSolved() {
        return totalChallengesSolved;
    }
    public void setTotalChallengesSolved(int totalChallengesSolved) {
        this.totalChallengesSolved = totalChallengesSolved;
    }
    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }
    public String getLevelTitle() {
        switch (level) {
            case 1:
                return "Beginner";
            case 2:
                return "Novice";
            case 3:
                return "Apprentice";
            case 4:
                return "Developer";
            case 5:
                return "Expert";
            case 6:
                return "Master";
            default:
                return "Beginner";
        }
    }
}
