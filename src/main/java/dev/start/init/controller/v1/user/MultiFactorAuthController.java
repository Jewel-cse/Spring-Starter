package dev.start.init.controller.v1.user;

import dev.samstevens.totp.exceptions.QrGenerationException;
import dev.start.init.entity.user.MultiFactorAuth;
import dev.start.init.entity.user.User;
import dev.start.init.enums.MultiFactorMethodType;
import dev.start.init.exception.user.UserAlreadyExistsException;
import dev.start.init.exception.user.UserNotFoundException;
import dev.start.init.repository.user.MultiFactorAuthRepository;
import dev.start.init.repository.user.UserRepository;
import dev.start.init.service.mfa.GoogleAuthenticatorMfaService;
import dev.start.init.service.user.UserService;
import dev.start.init.web.payload.request.OtpVerificationRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static dev.start.init.constants.apiEndPoint.API_V1.TWO_FACTOR_URL;

@RestController
@RequestMapping(TWO_FACTOR_URL)
public class MultiFactorAuthController {

    private final UserRepository userRepository;
    private GoogleAuthenticatorMfaService googleAuthenticatorMfaService;
    private final MultiFactorAuthRepository mfaAuthRepository;
    private final UserService userService;

    public MultiFactorAuthController(MultiFactorAuthRepository mfaAuthRepository,
                                     UserService userService,
                                     GoogleAuthenticatorMfaService googleAuthenticatorMfaService, UserRepository userRepository) {
        this.mfaAuthRepository = mfaAuthRepository;
        this.userService = userService;
        this.googleAuthenticatorMfaService = googleAuthenticatorMfaService;
        this.userRepository = userRepository;
    }

    //@RequestParam("secretKey") String secretKey,
    @GetMapping("/generate-qr-code")
    public ResponseEntity<byte[]> getQrCodePng(@RequestParam("username") String username) throws QrGenerationException {

        User user = userRepository.findByUsername(username);
        if(user == null) {
            throw new UserNotFoundException(username);
        }

        MultiFactorAuth multiFactorAuth = mfaAuthRepository.findByUserAndMethodType(user, MultiFactorMethodType.GOOGLE_AUTHENTICATOR);
        if(user.isMfaEnable() && multiFactorAuth != null && multiFactorAuth.isActive()){
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
    public String enable2FA(@RequestParam Long userId,@RequestBody List<String> methodType) {
        //for google auth:

        //step1: generate secret key for google auth
        //step2: store secrete key,user in mfa
        //step3(optional): user -> mfa -> pending

        //step4: generate qr-code and setup google auth
        //step5: verify otp



       /* try {
            // Fetch the user by ID
            User user = UserMapper.MAPPER.toUser(userService.findById(userId));

            // Generate a secret key for the user
            String secretKey = googleAuthenticatorMfaService.generateSecretKey();

            googleAuthenticatorMfaService.saveMfaAuthEntity(user, secretKey);

            // Generate the QR code URL (Google Authenticator URI)
            String totpUri = googleAuthenticatorMfaService.getQrCodeUrl(secretKey, user.getUsername());

            System.out.println("Otp: "+totpUri);

            model.addAttribute("totpUri", totpUri);

            // Return the QR code URL
            return "showQrCode";

        } catch (Exception e) {
            // Handle specific exceptions (e.g., user not found, service errors, etc.)
            model.addAttribute("error", e.getMessage());
            return "error";
        }*/
        return null;
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verify2FA(@RequestBody String otpCode) {
        return ResponseEntity.ok(googleAuthenticatorMfaService.verifyTotpCode(otpCode));
    }
    // Endpoint to verify the 2FA code
}

