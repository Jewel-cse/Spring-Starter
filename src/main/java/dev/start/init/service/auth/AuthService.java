package dev.start.init.service.auth;


import dev.start.init.dto.auth.LoginRequestDto;
import dev.start.init.dto.auth.LoginResponseDto;
import dev.start.init.entity.auth.User;

public interface AuthService {

    /**
     * Handles user login and generates both access and refresh tokens
     * @param loginDTO
     * @return
     */
    LoginResponseDto login(LoginRequestDto loginDTO);

    /**
     * Generates a short-lived JWT access token
     * @param user
     * @return
     */
    String generateAccessToken(User user);

    /**
     * Generates a long-lived JWT refresh token
     * @param user
     * @return
     */
    String generateRefreshToken(User user);

    /**
     * Validates the refresh token and issues a new access token
     * @param refreshToken
     * @return
     */
    LoginResponseDto refreshAccessToken(String refreshToken);
}
