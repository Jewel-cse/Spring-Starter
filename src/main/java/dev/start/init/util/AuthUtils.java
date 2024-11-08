package dev.start.init.util;

import dev.start.init.constants.SecurityConstants;
import dev.start.init.enums.TokenType;
import dev.start.init.service.security.CookieService;
import dev.start.init.service.security.EncryptionService;
import dev.start.init.service.security.JwtService;
import dev.start.init.web.payload.response.JwtResponseBuilder;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Date;

@Component
public final class AuthUtils {

    private JwtService jwtService;
    private CookieService cookieService;
    private EncryptionService encryptionService;

    @Value("${access-token-expiration-in-minutes}")
    private int accessTokenExpirationInMinutes;

    public AuthUtils(JwtService jwtService, CookieService cookieService, EncryptionService encryptionService,@Value("${access-token-expiration-in-minutes}")int accessTokenExpirationInMinutes) {
        this.jwtService = jwtService;
        this.cookieService = cookieService;
        this.encryptionService = encryptionService;
        this.accessTokenExpirationInMinutes = accessTokenExpirationInMinutes;
    }
    /**
     * Refreshes the access token using the provided refresh token and username.
     *
     * The method performs the following steps:
     * 1. Decrypts the provided refresh token using the `encryptionService`.
     * 2. Validates the decrypted refresh token using `jwtService.isValidJwtToken()`.
     * 3. If the refresh token is valid, it generates a new access token or retains the current one.
     * 4. Updates the response headers with the new token details.
     * 5. Encrypts the new access token before sending it in the response.
     *
     * @param refreshToken the encrypted refresh token received from the client.
     * @param username     the username of the user who is requesting a new access token.
     * @return a {@link ResponseEntity} containing the encrypted new access token and updated headers.
     */

    public ResponseEntity<?> refreshAccessToken(String refreshToken, String username) {
        String decryptedRefreshToken = encryptionService.decrypt(refreshToken);
        boolean isRefreshTokenValid = jwtService.isValidJwtToken(decryptedRefreshToken);

        HttpHeaders responseHeaders = new HttpHeaders();

        String newAccessToken = updateCookies(username, isRefreshTokenValid, responseHeaders);

        String encryptedAccessToken = encryptionService.encrypt(newAccessToken);

        return ResponseEntity.ok()
                .headers(responseHeaders)
                .body(JwtResponseBuilder.buildJwtResponse(encryptedAccessToken));
    }


    /**
     * Creates a refresh token if expired and adds it to the cookies.
     *
     * @param username the username
     * @param isRefreshValid if the refresh token is valid
     * @param headers the http headers
     */
    public String updateCookies(String username, boolean isRefreshValid, HttpHeaders headers) {

        if (!isRefreshValid) {
            var token = jwtService.generateJwtToken(username);
            var refreshDuration = Duration.ofDays(SecurityConstants.DEFAULT_TOKEN_DURATION);

            var encryptedToken = encryptionService.encrypt(token);
            cookieService.addCookieToHeaders(headers, TokenType.REFRESH, encryptedToken, refreshDuration);
        }

        var accessTokenExpiration = DateUtils.addMinutes(new Date(), accessTokenExpirationInMinutes);
        return jwtService.generateJwtToken(username, accessTokenExpiration);
    }
}
