package dev.start.init.dto.auth;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for login response.
 *
 * @author Md Jewel Rana
 * @version 1.0
 * @since 1.0
 */
@Data
public class LoginResponseDto {
    private String token;
    private String refreshToken;
}

