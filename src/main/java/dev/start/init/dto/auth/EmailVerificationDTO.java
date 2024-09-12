package dev.start.init.dto.auth;


import lombok.Data;

import jakarta.validation.constraints.NotBlank;

@Data
public class EmailVerificationDTO {

    @NotBlank(message = "Token is mandatory")
    private String token;
}

