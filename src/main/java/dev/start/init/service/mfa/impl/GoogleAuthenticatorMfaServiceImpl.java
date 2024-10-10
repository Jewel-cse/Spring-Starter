package dev.start.init.service.mfa.impl;

import dev.samstevens.totp.code.*;
import dev.samstevens.totp.exceptions.QrGenerationException;
import dev.samstevens.totp.qr.QrData;
import dev.samstevens.totp.qr.QrGenerator;
import dev.samstevens.totp.qr.ZxingPngQrGenerator;
import dev.samstevens.totp.secret.DefaultSecretGenerator;
import dev.samstevens.totp.secret.SecretGenerator;
import dev.samstevens.totp.time.SystemTimeProvider;
import dev.samstevens.totp.time.TimeProvider;
import dev.start.init.entity.user.MultiFactorAuth;
import dev.start.init.entity.user.User;
import dev.start.init.enums.MultiFactorMethodType;
import dev.start.init.exception.ResourceNotFoundException;
import dev.start.init.repository.user.MultiFactorAuthRepository;
import dev.start.init.repository.user.UserRepository;
import dev.start.init.service.mfa.GoogleAuthenticatorMfaService;
import dev.start.init.util.core.SecurityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
public class GoogleAuthenticatorMfaServiceImpl implements GoogleAuthenticatorMfaService {

    @Value("${spring.application.name}")
    String issuer;

    private MultiFactorAuthRepository mfaRepository;
    private UserRepository userRepository;
    public GoogleAuthenticatorMfaServiceImpl(@Value("${spring.application.name}") String issuer,
                                             MultiFactorAuthRepository mfaRepository ,
                                             UserRepository userRepository){
        this.mfaRepository = mfaRepository;
        this.userRepository = userRepository;
        this.issuer = issuer;
    }

    private final int digit = 6;
    private final int period=30;


    @Override
    public String generateSecretKey() {
        SecretGenerator secretGenerator = new DefaultSecretGenerator(64);
        return secretGenerator.generate();
    }

    @Override
    public byte[] getQrCodeUrl(String secretKey, String username) throws QrGenerationException {

        //return String.format("otpauth://totp/%s:%s?secret=%s&issuer=%s", issuer, username, secretKey, issuer);

        try {
            QrData data = new QrData.Builder()
                    .label(username)
                    .secret(secretKey)
                    .issuer(issuer)
                    .algorithm(HashingAlgorithm.SHA1)
                    .digits(digit)
                    .period(period)
                    .build();
            //generate png image
            QrGenerator generator = new ZxingPngQrGenerator();
            byte[] imageData = generator.generate(data);

            return imageData;

            //String base64Image = Base64.getEncoder().encodeToString(imageData);

            //return "data:image/png;base64," + base64Image;
            //String mimeType = generator.getImageMimeType();
            //return mimeType;
        } catch (Exception e) {
            throw new QrGenerationException("Failed to generate QR code", e);
        }
    }

    @Override
    public MultiFactorAuth verifyTotpCode(String inputCode) {

        User user = (User) SecurityUtils.getAuthentication().getPrincipal();
        MultiFactorAuth multiFactorAuth =mfaRepository.findByUserAndMethodType(user,MultiFactorMethodType.GOOGLE_AUTHENTICATOR);

        TimeProvider timeProvider = new SystemTimeProvider();
        CodeGenerator codeGenerator = new DefaultCodeGenerator(HashingAlgorithm.SHA1);
        CodeVerifier verifier = new DefaultCodeVerifier(codeGenerator, timeProvider);
        boolean isValid= verifier.isValidCode(multiFactorAuth.getSecretCode(), inputCode);
        if(isValid){
            multiFactorAuth.setVerified(true);
            multiFactorAuth.setActive(true);
            return mfaRepository.save(multiFactorAuth);
        }
        return null;
    }

    @Override
    public boolean isEnabled(User user) {

        return false;
    }

    @Override
    public MultiFactorAuth initializeMfaAuthEntity(User user, String secret) {
        MultiFactorAuth multiFactorAuth = new MultiFactorAuth();
        multiFactorAuth.setUser(user);
        multiFactorAuth.setSecretCode(secret);
        multiFactorAuth.setMethodType(MultiFactorMethodType.GOOGLE_AUTHENTICATOR);

        mfaRepository.save(multiFactorAuth);
        return null;
    }

    @Override
    public void enableMfaForUser(Long userId, MultiFactorMethodType methodType, String secretKey) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        MultiFactorAuth mfa = new MultiFactorAuth();
        mfa.setMethodType(methodType);
        mfa.setSecretCode(secretKey);
        mfa.setActive(true);
        mfa.setUser(user);
        mfaRepository.save(mfa);

        user.setMfaEnable(true);
        userRepository.save(user);
    }

    @Override
    public void disableMfaForUser(Long userId, MultiFactorMethodType methodType) {

    }

    /*@Override
    public MultiFactorAuth saveMfaAuthEntity(User user, String secret){
        MultiFactorAuth twoFactorAuth = new MultiFactorAuth();
        twoFactorAuth.setUser(user);
        twoFactorAuth.setMethod(MultiFactorMethodType.GOOGLE_AUTHENTICATOR.getMethodName());
        twoFactorAuth.setSecretCode(secret);
        twoFactorAuth.setVerified(false);
        return mfaAuthRepository.save(twoFactorAuth);
    }*/
}
