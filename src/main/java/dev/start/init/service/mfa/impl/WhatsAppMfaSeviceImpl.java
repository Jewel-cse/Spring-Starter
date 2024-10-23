package dev.start.init.service.mfa.impl;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import dev.start.init.enums.MultiFactorMethodType;
import dev.start.init.service.mfa.WhatsAppMfaService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class WhatsAppMfaSeviceImpl extends OtpGenerator implements WhatsAppMfaService {

    // Store OTPs temporarily in memory (use a database or cache for production)
    private Map<String, String> otpData = new HashMap<>();

    @Value("${twilio.account_sid}")
    private String accountSid;

    @Value("${twilio.auth_token}")
    private String authToken;

    @Value("${twilio.phone_number}")
    private String twilioPhoneNumber;

    // Initialize Twilio client
    public WhatsAppMfaSeviceImpl( @Value("${twilio.account_sid}") String accountSid,
                                  @Value("${twilio.auth_token}") String authToken,
                                  @Value("${twilio.phone_number}") String twilioPhoneNumber
    ) {
        this.accountSid = accountSid;
        this.authToken = authToken;
        this.twilioPhoneNumber = twilioPhoneNumber;
        Twilio.init(accountSid, authToken);
    }

    // Send OTP via WhatsApp or SMS
    public String sendOtp(String phoneNumber) {
        String otp = generate6DigitOtp();
        otpData.put(phoneNumber, otp);

        String messageBody = "Your OTP code is: " + otp;

        Message message = Message.creator(
                new PhoneNumber("whatsapp:" + phoneNumber), // If using WhatsApp, add "whatsapp:" prefix
                new PhoneNumber(twilioPhoneNumber),
                messageBody
        ).create();

        return message.getSid();
    }

    // Verify the OTP
    public boolean verifyOtp(String phoneNumber, String otp) {
        if (otpData.containsKey(phoneNumber)) {
            String storedOtp = otpData.get(phoneNumber);
            return storedOtp.equals(otp);
        }
        return false;
    }

    /**
     * @param userId
     * @param methodType
     * @param secretKey
     */
    @Override
    public void enableMfaForUser(Long userId, MultiFactorMethodType methodType, String secretKey) {

    }

    /**
     * @param userId
     * @param methodType
     */
    @Override
    public void disableMfaForUser(Long userId, MultiFactorMethodType methodType) {

    }

}
