package dev.start.init.controller.v1.user;


import dev.start.init.annotation.Loggable;
import dev.start.init.constants.ErrorConstants;
import dev.start.init.constants.SecurityConstants;
import dev.start.init.constants.apiEndPoint.API_V1;
import dev.start.init.constants.user.SignUpConstants;
import dev.start.init.dto.user.UserDto;
import dev.start.init.entity.user.MultiFactorAuth;
import dev.start.init.entity.user.User;
import dev.start.init.enums.MultiFactorMethodType;
import dev.start.init.enums.OperationStatus;
import dev.start.init.enums.TokenType;
import dev.start.init.mapper.UserMapper;
import dev.start.init.repository.user.UserRepository;
import dev.start.init.service.mfa.GoogleAuthenticatorMfaService;
import dev.start.init.service.security.BruteforceProtectionService;
import dev.start.init.service.security.CookieService;
import dev.start.init.service.security.EncryptionService;
import dev.start.init.service.security.JwtService;
import dev.start.init.service.user.UserService;
import dev.start.init.util.AuthUtils;
import dev.start.init.util.core.SecurityUtils;
import dev.start.init.web.payload.request.LoginRequest;
import dev.start.init.web.payload.request.SignUpRequest;
import dev.start.init.web.payload.response.JwtResponseBuilder;
import dev.start.init.web.payload.response.LogoutResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

import java.time.Clock;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.AccountLockedException;

/**
 * This class attempt to authenticate with AuthenticationManager bean, add an authentication object
 * to SecurityContextHolder then Generate JWT token, then return JWT to a client.
 *
 * <p>The apis are given bellow : </p>
 * <ul>
 *     <li>login</li>
 *     <li>refresh-token</li>
 *     <li>logout</li>
 * </ul>
 * @author Md Jewel
 * @version 1.0
 * @since 1.0
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(SecurityConstants.API_V1_AUTH_ROOT_URL)
public class AuthController {

    private final UserRepository userRepository;
    @Value("${access-token-expiration-in-minutes}")
    private int accessTokenExpirationInMinutes;

    @Value("${account-locked-duration}")
    private int accountLockedDuration;

    private final JwtService jwtService;
    private final CookieService cookieService;
    private final EncryptionService encryptionService;
    private final UserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;
    private final BruteforceProtectionService bruteForceProtectionService;
    private final GoogleAuthenticatorMfaService googleAuthenticatorMfaService;

    private final AuthUtils authUtils;


    /**
     * Attempts to authenticate with the provided credentials.Check refreshToken which comes from cookies is      *  valid or not, if not valid then generate new refresh toke and set the cookie or headers of response
     * If credentials successfully , a JWT token is returned
     * with some user details with cookies
     *
     * <p>A refresh token is generated and returned as a cookie.
     *
     * @param refreshToken The refresh token from cookie
     * @param loginRequest the login request
     * @return the jwt token with other details
     */
    //@SecurityRequirements
    @Operation(summary = "Authenticate User", description = "Logs in a user and returns a JWT token.")
    @Loggable(level = "debug")
    @PostMapping(value = SecurityConstants.LOGIN)
    public ResponseEntity<?> authenticateUser(
            @CookieValue(required = false) String refreshToken,
            @Valid @RequestBody LoginRequest loginRequest) throws AccountLockedException {

        User user = userRepository.findByUsername(loginRequest.getUsername());
        if(user !=null){
            // Getting the current time
            LocalDateTime now = LocalDateTime.now(Clock.systemDefaultZone());
            Duration lockedDuration = Duration.between(user.getUpdatedAt(), now);

            //if account is already locked and locked time up -> unlocked the user
            if (!user.isAccountNonLocked() && lockedDuration.getSeconds() > accountLockedDuration ) {
                bruteForceProtectionService.resetBruteForceCounter(user.getUsername());
            }
        }

        var username = loginRequest.getUsername();
        // Authentication will fail if the credentials are invalid and throw exception.
        SecurityUtils.authenticateUser(authenticationManager, username, loginRequest.getPassword());

        if(user.isMfaEnable()){
            return ResponseEntity.ok(new String("OTP_REQUIRED"));
        }

        /*//if authentication is done, then send the qr code url as png image
        MultiFactorAuth multiFactorAuth = new MultiFactorAuth();
        if(user.isMfaEnable()){
            switch (methodType){
                case "GOOGLE_AUTHENTICATOR":
                    multiFactorAuth = googleAuthenticatorMfaService.verifyTotpCode(inputCode);
            }
        }
        if(multiFactorAuth == null){
            throw new RuntimeException("Two factor authentication failed");
        }*/

        /*var decryptedRefreshToken = encryptionService.decrypt(refreshToken);
        var isRefreshTokenValid = jwtService.isValidJwtToken(decryptedRefreshToken);

        var responseHeaders = new HttpHeaders();
        // If the refresh token is valid, then we will not generate a new refresh token.
        String newAccessToken = updateCookies(username, isRefreshTokenValid, responseHeaders);
        String encryptedAccessToken = encryptionService.encrypt(newAccessToken);

        return ResponseEntity.ok()
                .headers(responseHeaders)
                .body(JwtResponseBuilder.buildJwtResponse(encryptedAccessToken));*/

        return authUtils.refreshAccessToken(refreshToken,username);
    }

    /**
     * Refreshes the current access token and refresh token accordingly.
     *
     * @param refreshToken The refresh token
     * @param request The request
     * @return the jwt token details
     */
    //@SecurityRequirements
    @Operation(summary = "Refresh JWT Token", description = "Refreshes the JWT access token using a valid refresh token.")
    @Loggable(level = "error")
    @GetMapping(value = SecurityConstants.REFRESH_TOKEN)
    public ResponseEntity<JwtResponseBuilder> refreshToken(
            @CookieValue String refreshToken, HttpServletRequest request) {

        var decryptedRefreshToken = encryptionService.decrypt(refreshToken);
        boolean refreshTokenValid = jwtService.isValidJwtToken(decryptedRefreshToken);

        if (!refreshTokenValid) {
            throw new IllegalArgumentException( ErrorConstants.INVALID_TOKEN);
        }
        var username = jwtService.getUsernameFromToken(decryptedRefreshToken);
        var userDetails = userDetailsService.loadUserByUsername(username);

        SecurityUtils.validateUserDetailsStatus(userDetails);
        SecurityUtils.authenticateUser(request, userDetails);

        var expiration = DateUtils.addMinutes(new Date(), accessTokenExpirationInMinutes);
        var newAccessToken = jwtService.generateJwtToken(username, expiration);
        var encryptedAccessToken = encryptionService.encrypt(newAccessToken);

        return ResponseEntity.ok(JwtResponseBuilder.buildJwtResponse(encryptedAccessToken));
    }

    /**
     * Logout the user from the system and clear all cookies from request and response.
     *
     * @param request the request
     * @param response the response
     * @return response entity
     */
    //@SecurityRequirements
    @Operation(summary = "Logout User", description = "Logs out the user and clears all associated cookies.")
    @Loggable(level = "warn")
    @DeleteMapping(value = SecurityConstants.LOGOUT)
    public ResponseEntity<LogoutResponse> logout(
            HttpServletRequest request, HttpServletResponse response) {
        SecurityUtils.logout(request, response);

        var responseHeaders = cookieService.addDeletedCookieToHeaders( TokenType.REFRESH);
        var logoutResponse = new LogoutResponse( OperationStatus.SUCCESS);
        SecurityUtils.clearAuthentication();

        return ResponseEntity.ok().headers(responseHeaders).body(logoutResponse);
    }

}

