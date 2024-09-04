package dev.start.init.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for password reset response.
 *
 * @author  Md Jewel Rana
 * @version 1.0
 * @since 1.0
 */
@Data
public class PasswordResetDto {

    @Email(message = "Email is not valid")
    @NotBlank(message = "Email is mandatory")
    private String email;
}

