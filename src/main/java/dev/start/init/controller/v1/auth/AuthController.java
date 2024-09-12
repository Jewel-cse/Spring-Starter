package dev.start.init.controller.v1.auth;

import dev.start.init.constants.apiEndPoint.API_V1;
import dev.start.init.dto.auth.*;
import dev.start.init.dto.mapper.auth.UserMapper;
import dev.start.init.service.auth.AuthService;
import dev.start.init.service.auth.UserService;
import dev.start.init.entity.auth.User;
import dev.start.init.service.mail.EmailService; // Hypothetical service for email operations
import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.io.IOException;

import static dev.start.init.constants.apiEndPoint.API_V1.AUTH_URL;

/**
 * Controller for handling authentication-related requests.
 * <p>
 * This controller provides endpoints for the following authentication features:
 * <ul>
 *     <li><b>Register a New User:</b> Allows users to register a new account and sends a verification email.</li>
 *     <li><b>Verify User's Email Address:</b> Verifies the user's email address using a verification token.</li>
 *     <li><b>Resend Verification Email:</b> Resends the email verification message to the user if they did not receive it initially.</li>
 *     <li><b>Log In and Get Access and Refresh Tokens:</b> Authenticates the user and returns access and refresh tokens.</li>
 *     <li><b>Request a Password Reset Link:</b> Sends a password reset link to the user's email address.</li>
 *     <li><b>Reset Password:</b> Resets the user's password using a provided token and new password.</li>
 *     <li><b>Refresh Access Token:</b> Refreshes the access token using a valid refresh token.</li>
 * </ul>
 * </p>
 */

@RequiredArgsConstructor
@RestController
@RequestMapping(AUTH_URL)
public class AuthController {

    private final AuthService authService;
    private final UserService userService;
    private final EmailService emailService;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    /**
     * Register a new user.
     *
     * @param registrationRequest the registration request containing user details
     * @return the response entity with registration status
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequestDto registrationRequest) throws TemplateException, MessagingException, IOException {
        User user = userService.registerUser(registrationRequest);
        emailService.sendVerificationEmail(user);
        //return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully. Please check your email to verify.");
        return ResponseEntity.ok(userMapper.toDto(user));
    }

    /**
     * Verify user's email address.
     *
     * @param token the verification token sent to the user's email
     * @return the response entity with verification status
     */
    @GetMapping("/verify-email")
    public ResponseEntity<?> verifyEmail(@RequestParam String token) {
        userService.verifyEmail(token);
        return ResponseEntity.ok(new ResponseEntity<>(HttpStatus.OK));
        //return ResponseEntity.ok().build();
    }

    /**
     * Resend verification email.
     *
     * @param email the email address of the user
     * @return the response entity with resend status
     */
    @PostMapping("/resend-verification")
    public ResponseEntity<?> resendVerificationEmail(@RequestParam String email) throws TemplateException, MessagingException, IOException {
        User user = userService.findUserByEmail(email);
        if (user != null) {
            emailService.sendVerificationEmail(user);
            //return ResponseEntity.ok("Verification email sent.");
            return ResponseEntity.ok(new ResponseEntity<>(HttpStatus.OK));
        } else {
            //return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
            return ResponseEntity.ok(new ResponseEntity<>(HttpStatus.NOT_FOUND));
        }
    }

    /**
     * Log in and get access and refresh tokens.
     *
     * @param loginRequest the login request containing email and password
     * @return the response entity with authentication tokens
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto loginRequest) {
        LoginResponseDto response = authService.login(loginRequest);
        return ResponseEntity.ok(response);
    }

    /**
     * Request a password reset link.
     *
     * @param email the email address of the user
     * @return the response entity with request status
     */
    @PostMapping("/forgot-password")
    public ResponseEntity<?> requestPasswordReset(@RequestParam String email) throws Exception {
        User user = userService.findUserByEmail(email);
        if (user != null) {
            emailService.sendPasswordResetEmail(user);
            //return ResponseEntity.ok("Password reset link sent.");
            return ResponseEntity.ok(new ResponseEntity<>(HttpStatus.OK));
        } else {
            //return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
            return ResponseEntity.ok(new ResponseEntity<>(HttpStatus.NOT_FOUND));
        }
    }

    /**
     * Reset the password using the provided token.
     *
     * @param resetPasswordRequest the request containing new password and token
     * @return the response entity with reset status
     */
    //currently it works for get method
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody PasswordResetRequestDto resetPasswordRequest,@RequestParam String token) {
        return ResponseEntity.ok(userService.resetPassword(token,resetPasswordRequest)) ;
        //return ResponseEntity.ok("Password reset successfully.");
    }

    /**
     * Refresh the access token using the refresh token.
     *
     * @param refreshTokenRequest the request containing refresh token
     * @return the response entity with new access and refresh tokens
     */
    /*@PostMapping("/refresh-token")
    public ResponseEntity<LoginResponseDto> refreshAccessToken(@Valid @RequestBody RefreshTokenRequestDto refreshTokenRequest) {
        LoginResponseDto response = authService.refreshAccessToken(refreshTokenRequest.getRefreshToken());
        return ResponseEntity.ok(response);
    }*/
}


