package dev.start.init.service.mfa;

import dev.start.init.entity.user.MultiFactorAuth;
import dev.start.init.entity.user.User;

public interface WhatsAppMfaService extends MfaService {
    boolean verifyOtp(String phoneNumber, String otp);
    String sendOtp(String phoneNumber);
    MultiFactorAuth initializeMfaAuthEntity(User user);
}
