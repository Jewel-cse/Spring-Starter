package dev.start.init.service.mfa;

import dev.start.init.entity.user.MultiFactorAuth;
import dev.start.init.entity.user.User;
import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;

import java.io.IOException;

public interface EmailMfaService extends MfaService  {

    /**
     * Sends an email with a verification code to the user's email address.
     *
     * @param emailAddress the email address to send the code to
     * @return the generated email verification code
     */
    void sendEmailCode(String emailAddress) throws MessagingException, IOException, TemplateException;

    /**
     * Verifies the email code against the stored code.
     *
     * @param inputOtp the code entered by the user
     * @param email the code stored in the system
     * @return true if the code matches, false otherwise
     */
    boolean verifyEmailCode(String inputOtp,String email);

    MultiFactorAuth initializeMfaAuthEntity(User user);
}

