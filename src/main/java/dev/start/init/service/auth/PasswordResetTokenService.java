package dev.start.init.service.auth;


import dev.start.init.entity.auth.PasswordResetToken;
import dev.start.init.entity.auth.User;

public interface PasswordResetTokenService {

    PasswordResetToken createPasswordResetToken(User user);

    PasswordResetToken findByToken(String token);

    void deleteByUserId(Long userId);
}

