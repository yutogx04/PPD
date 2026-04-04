package com.codequest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SubmitCodeRequest {

    @NotBlank
    @Size(max = 10000, message = "Le code ne doit pas dépasser 10 000 caractères")
    private String code;

    @NotBlank
    private String language;
}
