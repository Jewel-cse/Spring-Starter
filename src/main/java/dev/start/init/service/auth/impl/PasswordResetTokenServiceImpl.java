package dev.start.init.service.auth.impl;


import dev.start.init.entity.auth.PasswordResetToken;
import dev.start.init.entity.auth.User;
import dev.start.init.repository.auth.PasswordResetTokenRepository;
import dev.start.init.service.auth.PasswordResetTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class PasswordResetTokenServiceImpl implements PasswordResetTokenService {

    private final PasswordResetTokenRepository tokenRepository;

    @Override
    public PasswordResetToken createPasswordResetToken(User user) {
        PasswordResetToken token = new PasswordResetToken();
        token.setToken(UUID.randomUUID().toString());
        token.setUser(user);
        token.setExpirationDate(LocalDateTime.now().plusHours(1)); // 1 hour expiry

        return tokenRepository.save(token);
    }

    @Override
    public PasswordResetToken findByToken(String token) {
        return tokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid password reset token."));
    }

    @Override
    public void deleteByUserId(Long userId) {
        tokenRepository.deleteByUserId(userId);
    }
}

