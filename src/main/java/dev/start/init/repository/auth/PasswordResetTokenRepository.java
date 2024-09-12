package dev.start.init.repository.auth;

import dev.start.init.entity.auth.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for the PasswordResetToken entity.
 */
@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

    /**
     * Find a password reset token by token value.
     *
     * @param token the token value
     * @return an Optional containing the PasswordResetToken, if found
     */
    Optional<PasswordResetToken> findByToken(String token);

    /**
     * Find a password reset token by user.
     *
     * @param userId the ID of the user
     * @return an Optional containing the PasswordResetToken, if found
     */
    Optional<PasswordResetToken> findByUserId(Long userId);

    /**
     * Delete a password reset token by token value.
     *
     * @param token the token value
     */
    void deleteByToken(String token);
    void deleteByUserId(Long userId);
}

