package dev.start.init.service.mfa;

import dev.samstevens.totp.exceptions.QrGenerationException;
import dev.start.init.entity.user.MultiFactorAuth;
import dev.start.init.entity.user.User;

public interface GoogleAuthenticatorMfaService extends MfaService {

    /**
     * Generates a new secret key for Google Authenticator.
     *
     * @return the generated secret key
     */
    String generateSecretKey();

    byte[] getQrCodeUrl(String secretKey, String username) throws QrGenerationException;

    /**
     * Verifies the TOTP code generated by the Google Authenticator app.
     *
     * @param inputCode the code entered by the user
     * @return true if the code is valid, false otherwise
     */
    MultiFactorAuth verifyTotpCode(int inputCode,String username);

    boolean isEnabled(User user);

    MultiFactorAuth initializeMfaAuthEntity(User user, String secret);


}