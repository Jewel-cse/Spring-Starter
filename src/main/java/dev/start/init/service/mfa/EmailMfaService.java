package dev.start.init.service.mfa;

public interface EmailMfaService  {

    /**
     * Sends an email with a verification code to the user's email address.
     *
     * @param emailAddress the email address to send the code to
     * @return the generated email verification code
     */
    String sendEmailCode(String emailAddress);

    /**
     * Verifies the email code against the stored code.
     *
     * @param inputCode the code entered by the user
     * @param storedCode the code stored in the system
     * @return true if the code matches, false otherwise
     */
    boolean verifyEmailCode(String inputCode, String storedCode);
}

