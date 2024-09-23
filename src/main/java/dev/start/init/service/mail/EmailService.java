package dev.start.init.service.mail;

import dev.start.init.dto.user.UserDto;
import dev.start.init.entity.user.User;
import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;

import java.io.IOException;

/**
 * Service interface for email operations.
 * <p>
 * This service provides methods for sending verification emails and password reset links.
 * </p>
 */
public interface EmailService {

    /**
     * Sends a registration verification email to the user.
     *
     * @param user the user to whom the verification email will be sent
     */
    void sendVerificationEmail(UserDto user,String token) throws IOException, TemplateException, MessagingException;

    /**
     * Sends a password reset email to the user with a link to reset their password.
     *
     * @param user the user to whom the password reset email will be sent
     */
    void sendPasswordResetEmail(UserDto user) throws Exception;
}

