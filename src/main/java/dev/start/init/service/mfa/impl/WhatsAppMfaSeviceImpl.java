package dev.start.init.service.mfa.impl;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import dev.start.init.entity.user.MultiFactorAuth;
import dev.start.init.entity.user.User;
import dev.start.init.enums.MultiFactorMethodType;
import dev.start.init.repository.user.MultiFactorAuthRepository;
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

    private final MultiFactorAuthRepository multiFactorAuthRepository;

    // Initialize Twilio client
    public WhatsAppMfaSeviceImpl( @Value("${twilio.account_sid}") String accountSid,
                                  @Value("${twilio.auth_token}") String authToken,
                                  @Value("${twilio.phone_number}") String twilioPhoneNumber,
                                  MultiFactorAuthRepository multiFactorAuthRepository
    ) {
        this.accountSid = accountSid;
        this.authToken = authToken;
        this.multiFactorAuthRepository=multiFactorAuthRepository;
        this.twilioPhoneNumber = twilioPhoneNumber;
        Twilio.init(accountSid, authToken);
    }

    // Send OTP via WhatsApp or SMS
    public String sendOtp(String phoneNumber) {
        String otp = generate6DigitOtp();
        String messageBody = "Your OTP code is: " + otp;

        if (!phoneNumber.startsWith("+")) {
            phoneNumber = "+" + phoneNumber;
        }

        Message message = Message.creator(
                new PhoneNumber("whatsapp:" + phoneNumber),
                new PhoneNumber("whatsapp:" + twilioPhoneNumber),  // Ensure "whatsapp:" prefix here too
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
    /**
     * @param user
     * @return
     */
    @Override
    public MultiFactorAuth initializeMfaAuthEntity(User user) {
        MultiFactorAuth multiFactorAuth = new MultiFactorAuth();
        multiFactorAuth.setUser(user);
        multiFactorAuth.setMethodType(MultiFactorMethodType.WHATSAPP_SMS);

        return multiFactorAuthRepository.save(multiFactorAuth);
    }

}
