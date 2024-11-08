package dev.start.init.web.payload.request;

public class OtpVerificationRequest {

    private String secretKey;
    private String otpCode;

    // Getters and Setters

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getOtpCode() {
        return otpCode;
    }

    public void setOtpCode(String otpCode) {
        this.otpCode = otpCode;
    }
}

