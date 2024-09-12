package dev.start.init.service.auth.impl;

import dev.start.init.dto.auth.LoginRequestDto;
import dev.start.init.dto.auth.LoginResponseDto;
import dev.start.init.entity.auth.User;
import dev.start.init.security.JwtTokenProvider;
import dev.start.init.service.auth.AuthService;
import dev.start.init.service.auth.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import static java.time.LocalDateTime.now;

@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public LoginResponseDto login(LoginRequestDto loginDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDTO.getEmail(),
                        loginDTO.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        User user = userService.findUserByEmail(loginDTO.getEmail());
        //save the login time need update operation of userRepo.
        user.setLastLogin(now(Clock.systemDefaultZone()));

        String accessToken = generateAccessToken(user);
        String refreshToken = generateRefreshToken(user);

        return new LoginResponseDto(accessToken, refreshToken);
    }

    @Override
    public String generateAccessToken(User user) {
        return jwtTokenProvider.generateAccessToken(user.getEmail());
    }

    @Override
    public String generateRefreshToken(User user) {
        return jwtTokenProvider.generateRefreshToken(user.getEmail());
    }

    @Override
    public LoginResponseDto refreshAccessToken(String refreshToken) {
        if (jwtTokenProvider.validateRefreshToken(refreshToken)) {
            String email = jwtTokenProvider.getEmailFromRefreshToken(refreshToken);
            User user = userService.findUserByEmail(email);

            String newAccessToken = generateAccessToken(user);
            String newRefreshToken = generateRefreshToken(user);

            return new LoginResponseDto(newAccessToken, newRefreshToken);
        } else {
            throw new RuntimeException("Invalid refresh token.");
        }
    }
}

