package dev.start.init.service.mfa.impl;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import dev.start.init.entity.user.MultiFactorAuth;
import dev.start.init.entity.user.User;
import dev.start.init.enums.MultiFactorMethodType;
import dev.start.init.repository.user.MultiFactorAuthRepository;
import dev.start.init.service.mfa.SmsMfaService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class SmsMfaServiceImpl extends OtpGenerator implements SmsMfaService {
    @Value("${twilio.account_sid}")
    private String accountSid;

    @Value("${twilio.auth_token}")
    private String authToken;

    @Value("${twilio.phone_number}")
    private String twilioPhoneNumber;

    private MultiFactorAuthRepository multiFactorAuthRepository;

    // Initialize Twilio client
    public SmsMfaServiceImpl( @Value("${twilio.account_sid}") String accountSid,
                              @Value("${twilio.auth_token}") String authToken,
                              @Value("${twilio.phone_number}") String twilioPhoneNumber,
                              MultiFactorAuthRepository multiFactorAuthRepository
    ) {
        this.accountSid = accountSid;
        this.authToken = authToken;
        this.twilioPhoneNumber = twilioPhoneNumber;
        this.multiFactorAuthRepository = multiFactorAuthRepository;
        Twilio.init(accountSid, authToken);
    }

    /**
     * Sends an SMS verification code to the user's phone number.
     *
     * @param phoneNumber the phone number to send the code to
     * @return the generated SMS code
     */
    @Override
    public String sendOtp(String phoneNumber) {
        //mfa table identify->by phone number -> save otp
        String otp = generate6DigitOtp();
        //otpData.put(phoneNumber, otp);

        String messageBody = "Your OTP code is: " + otp;

        Message message = Message.creator(
                new PhoneNumber(phoneNumber),
                new PhoneNumber(twilioPhoneNumber),
                messageBody
        ).create();
        return message.getSid();
    }

    /**
     * Verifies the SMS code against the stored code.
     *
     * @param inputCode  the code entered by the user
     * @param storedCode the code stored in the system
     * @return true if the code matches, false otherwise
     */
    @Override
    public boolean verifyOtp(String inputCode, String storedCode) {
        return false;
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
