package dev.start.init.repository.auth;


import dev.start.init.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for the User entity.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Find a user by username.
     *
     * @param username the username of the user
     * @return an Optional containing the User, if found
     */
    //Optional<User> findByUsername(String username);

    /**
     * Find a user by email.
     *
     * @param email the email of the user
     * @return an Optional containing the User, if found
     */
    Optional<User> findByEmail(String email);

    /**
     * Check if a user exists by username.
     *
     * @param username the username to check
     * @return true if the user exists, false otherwise
     */
    //boolean existsByUsername(String username);

    /**
     * Check if a user exists by email.
     *
     * @param email the email to check
     * @return true if the user exists, false otherwise
     */
    boolean existsByEmail(String email);

    Optional<User> findByVerificationToken(String verificationToken);
    Optional<User> findByPasswordResetToken(String token);
}

