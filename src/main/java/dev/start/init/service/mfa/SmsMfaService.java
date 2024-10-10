package dev.start.init.service.mfa;

public interface SmsMfaService  {

    /**
     * Sends an SMS verification code to the user's phone number.
     *
     * @param phoneNumber the phone number to send the code to
     * @return the generated SMS code
     */
    String sendSmsCode(String phoneNumber);

    /**
     * Verifies the SMS code against the stored code.
     *
     * @param inputCode the code entered by the user
     * @param storedCode the code stored in the system
     * @return true if the code matches, false otherwise
     */
    boolean verifySmsCode(String inputCode, String storedCode);
}

