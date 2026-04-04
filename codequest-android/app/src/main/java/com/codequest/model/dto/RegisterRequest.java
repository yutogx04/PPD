package com.codequest.model.dto;
public class RegisterRequest {
    private String pseudo;
    private String email;
    private String password;
    private String level;

    public RegisterRequest() {}

    public RegisterRequest(String pseudo, String email, String password, String level) {
        this.pseudo = pseudo;
        this.email = email;
        this.password = password;
        this.level = level;
    }

    public String getPseudo() { return pseudo; }
    public void setPseudo(String pseudo) { this.pseudo = pseudo; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getLevel() { return level; }
    public void setLevel(String level) { this.level = level; }
}
