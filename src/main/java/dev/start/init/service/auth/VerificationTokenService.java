package dev.start.init.service.auth;


import dev.start.init.entity.auth.User;
import dev.start.init.entity.auth.VerificationToken;

public interface VerificationTokenService {

    VerificationToken createVerificationToken(User user);

    VerificationToken findByToken(String token);

    void deleteByUserId(Long userId);
}

