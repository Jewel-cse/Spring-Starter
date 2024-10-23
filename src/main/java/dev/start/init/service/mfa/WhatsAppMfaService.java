package dev.start.init.service.mfa;

public interface WhatsAppMfaService extends MfaService {
    boolean verifyOtp(String phoneNumber, String otp);
    String sendOtp(String phoneNumber);
}
