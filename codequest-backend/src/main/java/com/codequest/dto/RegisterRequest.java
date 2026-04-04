package com.codequest.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {

    @NotBlank @Email
    private String email;

    @NotBlank @Size(min = 6, max = 100)
    private String password;

    @NotBlank @Size(min = 3, max = 30)
    private String pseudo;

    private String level;
}
