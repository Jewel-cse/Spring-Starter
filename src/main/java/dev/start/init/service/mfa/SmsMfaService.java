package dev.start.init.service.mfa;

import dev.start.init.entity.user.MultiFactorAuth;
import dev.start.init.entity.user.User;

public interface SmsMfaService extends MfaService {

    /**
     * Sends an SMS verification code to the user's phone number.
     *
     * @param phoneNumber the phone number to send the code to
     * @return the generated SMS code
     */
    String sendOtp(String phoneNumber);

    /**
     * Verifies the SMS code against the stored code.
     *
     * @param phoneNumber the code entered by the user
     * @param otp the code stored in the system
     * @return true if the code matches, false otherwise
     */
    boolean verifyOtp(String otp, String phoneNumber);

    MultiFactorAuth initializeMfaAuthEntity(User user);


}

