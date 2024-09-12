package dev.start.init.service.auth;


import dev.start.init.dto.auth.PasswordResetRequestDto;
import dev.start.init.dto.auth.SignupRequestDto;
import dev.start.init.entity.auth.User;

public interface UserService {

    User registerUser(SignupRequestDto userRegistrationDTO);

    User findUserByEmail(String email);

    void enableUser(String email);
    /**
     * Resets the user's password using a reset token.
     *
     * @param token The reset token sent to the user.
     * @param resetPasswordRequestDto The new password to set.
     * @return The updated User entity.
     */
    User resetPassword(String token, PasswordResetRequestDto resetPasswordRequestDto);

    /**
     * Verifies the user's email using a verification token.
     *
     * @param token The verification token sent to the user.
     * @return The updated User entity.
     */
    void verifyEmail(String token);
}

