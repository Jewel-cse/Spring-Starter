package dev.start.init.service.auth.impl;

import dev.start.init.entity.auth.User;
import dev.start.init.entity.auth.VerificationToken;
import dev.start.init.repository.auth.VerificationTokenRepository;
import dev.start.init.service.auth.VerificationTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class VerificationTokenServiceImpl implements VerificationTokenService {

    private final VerificationTokenRepository tokenRepository;

    @Override
    public VerificationToken createVerificationToken(User user) {
        VerificationToken token = new VerificationToken();
        token.setToken(UUID.randomUUID().toString());
        token.setUser(user);
        token.setExpiryDate(LocalDateTime.now().plusMinutes(5)); // 5 minutes expiry

        return tokenRepository.save(token);
    }

    @Override
    public VerificationToken findByToken(String token) {
        return tokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid verification token."));
    }

    @Override
    public void deleteByUserId(Long userId) {
        tokenRepository.deleteByUserId(userId);
    }
}

