package dev.start.init.controller.v1.user;

import dev.samstevens.totp.exceptions.QrGenerationException;
import dev.start.init.constants.user.UserConstants;
import dev.start.init.dto.user.UserDto;
import dev.start.init.entity.user.MultiFactorAuth;
import dev.start.init.entity.user.User;
import dev.start.init.enums.MultiFactorMethodType;
import dev.start.init.exception.ResourceNotFoundException;
import dev.start.init.exception.user.UserAlreadyExistsException;
import dev.start.init.exception.user.UserNotFoundException;
import dev.start.init.mapper.UserMapper;
import dev.start.init.repository.user.MultiFactorAuthRepository;
import dev.start.init.repository.user.UserRepository;
import dev.start.init.service.mfa.EmailMfaService;
import dev.start.init.service.mfa.GoogleAuthenticatorMfaService;
import dev.start.init.service.mfa.SmsMfaService;
import dev.start.init.service.mfa.WhatsAppMfaService;
import dev.start.init.service.user.UserService;
import dev.start.init.util.AuthUtils;
import dev.start.init.util.core.SecurityUtils;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static dev.start.init.constants.apiEndPoint.API_V1.TWO_FACTOR_URL;

@RestController
@RequiredArgsConstructor
@RequestMapping(TWO_FACTOR_URL)
public class MultiFactorAuthController {

    private final UserRepository userRepository;
    private final GoogleAuthenticatorMfaService googleAuthenticatorMfaService;
    private final WhatsAppMfaService whatsAppMfaService;
    private final SmsMfaService smsMfaService;
    private final EmailMfaService emailMfaService;
    private final MultiFactorAuthRepository mfaAuthRepository;
    private final UserService userService;
    private final AuthUtils authUtils;


    @GetMapping("/generate-qr-code")
    public ResponseEntity<byte[]> getQrCodePng(@RequestParam("username") String username) throws QrGenerationException {

        User user = userRepository.findByUsername(username);
        if(user == null) {
            throw new UserNotFoundException(username);
        }

        Optional<MultiFactorAuth> multiFactorAuth = mfaAuthRepository.findByUserIdAndMethodType(user.getId(), MultiFactorMethodType.GOOGLE_AUTHENTICATOR);
        if(multiFactorAuth.isPresent()){
            throw new UserAlreadyExistsException("Google authenticator is already active");
        }
        try {
            String secretKey = googleAuthenticatorMfaService.generateSecretKey();
            googleAuthenticatorMfaService.initializeMfaAuthEntity(user,secretKey);

            byte[] imageData = googleAuthenticatorMfaService.getQrCodeUrl(secretKey, username);

            // Set headers for PNG content type
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_PNG);

            // Return image data as response with headers
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(imageData);

        } catch (Exception e) {
            throw new QrGenerationException("Failed to generate QR code", e);
        }
    }

    //set user mfa enable
    @PostMapping("/enable")
    public ResponseEntity<?> enable2FA() {
        UserDto userDto = SecurityUtils.getAuthorizedUserDto();
        if (userDto == null){
            throw new UserNotFoundException("User not found !!");
        }
        return ResponseEntity.ok(userService.enableMfa(userDto.getPublicId())) ;
    }
    @PostMapping("/disable")
    public ResponseEntity<?> disable2FA() {
        UserDto userDto = SecurityUtils.getAuthorizedUserDto();
        if (userDto == null){
            throw new UserNotFoundException("User not found !!");
        }
        return ResponseEntity.ok(userService.disableMfa(userDto.getPublicId())) ;
    }

    //verify otp for google authenticator
    @PostMapping("/verify-otp")
    public ResponseEntity<?> verify2FA(@RequestParam("otpCode") int otpCode,
                                       @CookieValue(required = false) String refreshToken,
                                       @RequestParam("username") String username) {
        MultiFactorAuth multiFactorAuth = googleAuthenticatorMfaService.verifyTotpCode(otpCode,username);
        if(multiFactorAuth == null){
            throw new UserNotFoundException("This Authenticator not Valid!!");
        }
        return ResponseEntity.ok(authUtils.refreshAccessToken(refreshToken,username).getBody()) ;
    }

    /*
    * For whatsapp sms send and verify
    *
    * */

    // Endpoint to send OTP
    @PostMapping("/send-otp-whatsapp")
    public ResponseEntity<String> sendOtp(@RequestParam("phoneNumber") String phoneNumber) {
        try {
            smsMfaService.sendOtp(phoneNumber);
            return ResponseEntity.ok("OTP sent successfully to " + phoneNumber);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error sending OTP: " + e.getMessage());
        }
    }

    // Endpoint to verify OTP
    @PostMapping("/verify-otp-whatsapp")
    public ResponseEntity<String> verifyOtp(
            @RequestParam("phoneNumber") String phoneNumber,
            @RequestParam("otp") String otp) {
        boolean isValid = whatsAppMfaService.verifyOtp(phoneNumber, otp);
        if (isValid) {
            return ResponseEntity.ok("OTP verified successfully");
        } else {
            return ResponseEntity.status(400).body("Invalid OTP");
        }
    }

    /*
    * Using email send and verify email
    * */

    @PostMapping("/send-otp-email")
    public ResponseEntity<String> sendOtpEmail(@RequestParam("email") @Email  String email) {
        try {
            emailMfaService.sendEmailCode(email);
            return ResponseEntity.ok("OTP sent successfully to " + email);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error sending OTP: " + e.getMessage());
        }
    }

    @PostMapping("/verify-otp-email")
    public ResponseEntity<String> verifyOtpEmail(
            @RequestParam("email") String email,
            @RequestParam("otp") String otp) {

        boolean isValid = emailMfaService.verifyEmailCode(otp,email);
        if (isValid) {
            return ResponseEntity.ok("OTP verified successfully");
        } else {
            return ResponseEntity.status(400).body("Invalid OTP");
        }
    }
}

