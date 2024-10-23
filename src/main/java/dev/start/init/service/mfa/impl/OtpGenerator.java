package dev.start.init.service.mfa.impl;

import java.util.Random;

public class OtpGenerator {

    /*
    * Generate 6 digit otp
    * */
    protected String generate6DigitOtp() {
        return String.format("%06d", new Random().nextInt(999999));
    }

    /*
    * Generate 4 digit otp
    *
    * */
    protected String generate4DigitOtp() {
        return String.format("%04d", new Random().nextInt(9999));
    }
}

