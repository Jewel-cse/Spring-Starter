package dev.start.init.dto.auth;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * DTO for login request.
 *
 * @author Md Jewel Rana
 * @version 1.0
 * @since 1.0
 *
 */
@Data
public class LoginRequestDto {

    @Email(message = "Email is not valid")
    @NotBlank(message = "Email is mandatory")
    private String email;

    @NotBlank(message = "Password is mandatory")
    @Size(min=8)
    private String password;
}

