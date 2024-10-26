package dev.start.init.service.mfa.impl;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import dev.start.init.entity.user.MultiFactorAuth;
import dev.start.init.entity.user.User;
import dev.start.init.enums.MultiFactorMethodType;
import dev.start.init.exception.user.UserNotFoundException;
import dev.start.init.repository.user.MultiFactorAuthRepository;
import dev.start.init.repository.user.UserRepository;
import dev.start.init.service.mfa.SmsMfaService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;


@Service
public class SmsMfaServiceImpl extends OtpGenerator implements SmsMfaService {
    @Value("${twilio.account_sid}")
    private String accountSid;

    @Value("${twilio.auth_token}")
    private String authToken;

    @Value("${twilio.phone_number}")
    private String twilioPhoneNumber;

    @Value("${otp.expired.time}")
    private Long otpExpiredDuration;

    private final UserRepository userRepository;
    private final MultiFactorAuthRepository multiFactorAuthRepository;

    // Initialize Twilio client
    public SmsMfaServiceImpl(UserRepository userRepository,
                             @Value("${twilio.account_sid}") String accountSid,
                             @Value("${twilio.auth_token}") String authToken,
                             @Value("${twilio.phone_number}") String twilioPhoneNumber,
                             @Value("${otp.expired.time}") Long otpExpiredDuration,
                             MultiFactorAuthRepository multiFactorAuthRepository
    ) {
        this.userRepository = userRepository;
        this.accountSid = accountSid;
        this.authToken = authToken;
        this.twilioPhoneNumber = twilioPhoneNumber;
        this.otpExpiredDuration = otpExpiredDuration;
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
    @Transactional
    public String sendOtp(String phoneNumber) {

        Optional<MultiFactorAuth> multiFactorAuth= multiFactorAuthRepository.findByUserPhoneAndMethodType(phoneNumber,MultiFactorMethodType.SMS);
        String otp = generate6DigitOtp();
        if (!phoneNumber.startsWith("+")) {
            phoneNumber = "+" + phoneNumber;
        }
        MultiFactorAuth mfa = multiFactorAuth.isEmpty()?new MultiFactorAuth():multiFactorAuth.get();
        if(multiFactorAuth.isEmpty()){
            User user = userRepository.findByPhone(phoneNumber);
            if(user == null){
                throw  new UserNotFoundException("User not found with phone number: " + phoneNumber);
            }
            mfa = initializeMfaAuthEntity(user);
        }

        mfa.setVerificationCode(otp);
        mfa.setCodeExpiresAt(LocalDateTime.now().plusSeconds(otpExpiredDuration));
        multiFactorAuthRepository.save(mfa);

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
     * @return true if the code matches, false otherwise
     */
    @Override
    public boolean verifyOtp(String inputCode,String phone) {
        Optional<MultiFactorAuth> multiFactorAuth= multiFactorAuthRepository.findByUserPhoneAndMethodType(phone,MultiFactorMethodType.SMS);

        if(multiFactorAuth.isEmpty() || !inputCode.equals(multiFactorAuth.get().getVerificationCode())){
            return false;
        }

        //time gap is greater than -> valid
        Long timeGapInSeconds = Duration.between(LocalDateTime.now(), multiFactorAuth.get().getCodeExpiresAt()).getSeconds();
        if(timeGapInSeconds < 0){
            throw new RuntimeException("OTP has expired.");
        }
        MultiFactorAuth mfa = multiFactorAuth.get();

        //mfa.setVerificationCode(null);
        mfa.setVerified(true);
        mfa.setActive(true);
        multiFactorAuthRepository.save(mfa);
        return true;
    }

    /**
     * @param user
     * @return
     */
    @Override
    public MultiFactorAuth initializeMfaAuthEntity(User user) {
        MultiFactorAuth multiFactorAuth = new MultiFactorAuth();
        multiFactorAuth.setUser(user);
        multiFactorAuth.setMethodType(MultiFactorMethodType.SMS);

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
