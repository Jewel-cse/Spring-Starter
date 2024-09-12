package dev.start.init.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * DTO for sign-up request.
 */
@Data
public class SignupRequestDto {

    /*@NotBlank(message = "Username is required")
    @Size(max = 100, message = "Username must be less than or equal to 100 characters")
    private String username;*/

    @Size(max = 100, message = "First name must be less than or equal to 100 characters")
    private String firstName;

    @Size(max = 100, message = "Last name must be less than or equal to 100 characters")
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Size(max = 100, message = "Email must be less than or equal to 100 characters")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(max = 100, message = "Password must be less than or equal to 100 characters")

    private String password;




}

