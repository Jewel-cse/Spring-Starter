package dev.start.init.service.mfa.impl;

import dev.start.init.entity.user.MultiFactorAuth;
import dev.start.init.entity.user.User;
import dev.start.init.enums.MultiFactorMethodType;
import dev.start.init.exception.user.UserNotFoundException;
import dev.start.init.repository.user.MultiFactorAuthRepository;
import dev.start.init.repository.user.UserRepository;
import dev.start.init.service.mail.EmailService;
import dev.start.init.service.mfa.EmailMfaService;
import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

import static dev.start.init.dto.response.error.CustomErrorAdvice.logger;

@Service
@RequiredArgsConstructor
public class EmailMfaServiceImpl extends OtpGenerator implements EmailMfaService {

    private final MultiFactorAuthRepository multiFactorAuthRepository;
    private final EmailService emailService;
    private final UserRepository userRepository;


    /**
     * Sends an email with a verification code to the user's email address.
     *
     * @param emailAddress the email address to send the code to
     * @return the generated email verification code
     */
    @Override
    public void sendEmailCode(String emailAddress) throws MessagingException, IOException, TemplateException{
        String otp = generate6DigitOtp();
        Optional<MultiFactorAuth> multiFactorAuth= multiFactorAuthRepository.findByEmail(emailAddress);
        if(multiFactorAuth.isEmpty()){
            User user = userRepository.findByEmail(emailAddress);
            if(user != null){
                initializeMfaAuthEntity(user);
            }
            else {
                throw new UserNotFoundException("User not found");
            }
        }
        multiFactorAuth.get().setVerificationCode(otp);
        multiFactorAuthRepository.save(multiFactorAuth.get());
        emailService.sendOtpEmail(emailAddress,otp);
    }

    /**
     * Verifies the email code against the stored code.
     *
     * @param inputOtp  the code entered by the user
     * @return true if the code matches, false otherwise
     */
    @Override
    public boolean verifyEmailCode(String inputOtp,String email) {
       Optional<MultiFactorAuth> multiFactorAuth= multiFactorAuthRepository.findByEmail(email);
       if (multiFactorAuth.isEmpty()) {
           throw new UserNotFoundException("User not found");
       }
       if(inputOtp.equals(multiFactorAuth.get().getVerificationCode())){
           multiFactorAuth.get().setVerified(true);
           multiFactorAuth.get().setActive(true);
           multiFactorAuth.get().setVerificationCode(null);
           MultiFactorAuth updatedMfa =  multiFactorAuthRepository.save(multiFactorAuth.get());
           logger.info("updated mfa entity : {}", updatedMfa);
           return true;
       }
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
        multiFactorAuth.setMethodType(MultiFactorMethodType.EMAIL);

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
