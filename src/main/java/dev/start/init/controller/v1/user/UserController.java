package dev.start.init.controller.v1.user;

import dev.start.init.annotation.Loggable;
import dev.start.init.constants.AdminConstants;
import dev.start.init.constants.ErrorConstants;
import dev.start.init.constants.user.ProfileConstants;
import dev.start.init.constants.user.SignUpConstants;
import dev.start.init.constants.user.UserConstants;
import dev.start.init.dto.user.UserDto;
import dev.start.init.entity.user.User;
import dev.start.init.enums.OperationStatus;
import dev.start.init.enums.UserHistoryType;
import dev.start.init.exception.user.UserAlreadyExistsException;
import dev.start.init.mapper.UserMapper;
import dev.start.init.service.mail.EmailService;
import dev.start.init.service.security.EncryptionService;
import dev.start.init.service.security.JwtService;
import dev.start.init.service.user.UserService;
import dev.start.init.util.UserUtils;
import dev.start.init.web.payload.request.SignUpRequest;
import dev.start.init.web.payload.response.UserResponse;
import freemarker.template.TemplateException;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;

import java.io.IOException;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


/**
 *
 * This class handles all REST API requests related to user management.
 * It provides endpoints for creating, updating, enabling, disabling,
 * deleting users, and verifying user sign-up via token-based verification.
 *
 * <ul>
 *  <li><strong>GET /v1/users</strong> - Retrieves a paginated list of all users.</li>
 *  <li><strong>GET /v1/users/{publicId}</strong> - Retrieves a specific user by their public ID.</li>
 *  <li><strong>PUT /v1/users/{publicId}/enable</strong> - Enables a user account by their public ID.</li>
 *  <li><strong>PUT /v1/users/{publicId}/disable</strong> - Disables a user account by their public ID.</li>
 *  <li><strong>DELETE /v1/users/{publicId}</strong> - Deletes a user account by their public ID.</li>
 *  <li><strong>PUT /v1/users</strong> - Updates user details with data provided in the request body.</li>
 *  <li><strong>POST /v1/users</strong> - Creates a new user with details provided in the request body.</li>
 *  <li><strong>GET /v1/auth/verify-signup</strong> - Verifies the user's email address using a provided token.</li>
 * </ul>
 *
 * The controller uses several services to perform its actions:
 * <ul>
 *  <li><strong>UserService</strong> - Handles user creation, updates, and retrieval from the database.</li>
 *  <li><strong>JwtService</strong> - Responsible for generating and validating JWT tokens.</li>
 *  <li><strong>EmailService</strong> - Sends out emails, including verification and account-related notifications.</li>
 *  <li><strong>EncryptionService</strong> - Handles the encryption and decryption of sensitive data, such as verification tokens.</li>
 * </ul>
 *
 * <p><strong>Security:</strong></p>
 * <ul>
 *  <li>Authenticated users are required for most endpoints (using Spring Security PreAuthorize annotations).</li>
 *  <li>Some actions, like sign-up verification, do not require authentication.</li>
 * </ul>
 *
 * <p><strong>Logging:</strong></p>
 * <ul>
 *  <li>Annotated with {@code @Loggable} to automatically log method calls for better traceability.</li>
 * </ul>
 *
 * <p><strong>Exception Handling:</strong></p>
 * <ul>
 *  <li>Throws {@code UserAlreadyExistsException} if the user or email already exists during sign-up.</li>
 * </ul>
 *
 * <p><strong>Endpoints:</strong></p>
 * <ul>
 *  <li>{@code GET /v1/users}: Retrieves a paginated list of users.</li>
 *  <li>{@code GET /v1/users/{publicId}}: Retrieves user by public ID.</li>
 *  <li>{@code PUT /v1/users/{publicId}/enable}: Enables a user by public ID.</li>
 *  <li>{@code PUT /v1/users/{publicId}/disable}: Disables a user by public ID.</li>
 *  <li>{@code DELETE /v1/users/{publicId}}: Deletes a user by public ID.</li>
 *  <li>{@code PUT /v1/users}: Updates an existing user.</li>
 *  <li>{@code POST /v1/users}: Creates a new user.</li>
 *  <li>{@code GET /auth/verify-signup}: Verifies the user's email during sign-up using a token.</li>
 * </ul>
 *
 * <p>Example usage scenarios include:</p>
 * <ul>
 *  <li>User registration with email verification.</li>
 *  <li>Admin controls to enable, disable, and delete user accounts.</li>
 *  <li>Paginated listing of all users.</li>
 * </ul>
 *
 * @author Md Jewel
 * @version 1.0
 * @since 1.0
 */

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(AdminConstants.API_V1_USERS_ROOT_URL)
public class UserController {

    private final UserService userService;
    private final JwtService jwtService;
    private final EncryptionService encryptionService;


    private static final String AUTHORIZE =
            "isFullyAuthenticated()";
    //&& hasRole(T(org.dwasa.enums.RoleType).ROLE_ADMIN)

    /**
     * Performs a search for users based on the provided search criteria.
     *
     * @param page Allows for pagination of the search results.
     * @return The ResponseEntity containing the search results as a Page of users
     */
    @PreAuthorize(AUTHORIZE)
    @Loggable(ignoreResponseData = true)
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<UserDto>> getUsers(final Pageable page) {

        Page<UserDto> users = userService.findAll(page);
        return ResponseEntity.ok(users);
    }

    @PreAuthorize(AUTHORIZE)
    @Loggable(ignoreResponseData = true)
    @GetMapping(value = "/{publicId}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDto> getByPublicId(@PathVariable String publicId) {

        UserDto userDto = userService.findByPublicId(publicId);
        return ResponseEntity.ok(userDto);
    }

    /**
     * Enables the user associated with the publicId.
     *
     * @param publicId the publicId
     * @return if the operation is success
     */
    @PreAuthorize(AUTHORIZE)
    @PutMapping(value = "/{publicId}/enable")
    public ResponseEntity<OperationStatus> enableUser(@PathVariable String publicId) {
        var userDto = userService.enableUser(publicId);

        return ResponseEntity.ok(
                Objects.isNull(userDto) ? OperationStatus.FAILURE : OperationStatus.SUCCESS);
    }

    /**
     * Disables the user associated with the publicId.
     *
     * @param publicId the publicId
     * @return if the operation is success
     */
    @PreAuthorize(AUTHORIZE)
    @PutMapping(value = "/{publicId}/disable", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OperationStatus> disableUser(@PathVariable String publicId) {
        var userDto = userService.disableUser(publicId);

        return ResponseEntity.ok(
                Objects.isNull(userDto) ? OperationStatus.FAILURE : OperationStatus.SUCCESS);
    }

    /**
     * Deletes the user associated with the publicId.
     *
     * @param publicId the publicId
     * @return if the operation is success
     */
    @PreAuthorize(AUTHORIZE)
    @DeleteMapping(value = "/{publicId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OperationStatus> deleteUser(@PathVariable String publicId) {
        userService.deleteUser(publicId);

        return ResponseEntity.ok(OperationStatus.SUCCESS);
    }



    @Loggable
    @PutMapping
    @SecurityRequirements
    public ResponseEntity<UserDto> updateUser(@Valid @RequestBody UserDto userDto) {
        return ResponseEntity.ok(userService.updateUser(userDto));
    }

    @Loggable
    @PostMapping
    @SecurityRequirements
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody SignUpRequest signUpRequest) throws TemplateException, MessagingException, IOException {
        var userDto = UserUtils.convertToUserDto(signUpRequest);
        //enable using utils function
        //UserUtils.enableUser(userDto);

        if (userService.existsByUsernameOrEmailAndEnabled(userDto.getUsername(), userDto.getEmail())) {
            LOG.warn(UserConstants.USERNAME_OR_EMAIL_EXISTS);
            throw new UserAlreadyExistsException(UserConstants.USERNAME_OR_EMAIL_EXISTS);
        }

        UserDto userDto1 = userService.createUser(userDto);

        return ResponseEntity.ok(UserUtils.toUserResponse(userDto1));
    }
    /**
     * This mapping handles the continuation of sign up.
     * currently :simply check user exists or
     * @param token the token. this token is encrypted + encoded
     * @param redirectAttributes the redirectAttributes
     * @return the view mapping for login.
     */

    @Loggable
    @SecurityRequirements
    @GetMapping(SignUpConstants.SIGN_UP_VERIFY_MAPPING)
    public ResponseEntity<UserDto> completeSignUp(@RequestParam String token, RedirectAttributes redirectAttributes) throws TemplateException, MessagingException, IOException {
        var decodedToken = encryptionService.decode(token);
        var verificationToken = encryptionService.decrypt(decodedToken);

        var userDto = validateTokenAndUpdateUser(verificationToken, redirectAttributes);
        /*if (Objects.nonNull(userDto) && !redirectAttributes.containsAttribute( ErrorConstants.ERROR)) {

            // send an account confirmation to the user.
            emailService.sendVerificationEmail(userDto);

            return ProfileConstants.REDIRECT_TO_PROFILE;
        }*/

        //return SignUpConstants.SIGN_UP_VIEW_NAME;
        System.out.println("user dto after validating : " + userDto);
        if(userDto != null){
           return ResponseEntity.ok(userService.enableUser(userDto.getPublicId()));
        }

        return ResponseEntity.ok(null);
    }

    /**
     * Update the user at this point then send an email after an update if the token is valid.
     *
     * @param token the token
     * @return the user dto
     */

    @SuppressWarnings("unused")
    private UserDto validateTokenAndUpdateUser(final String token, final Model model) {
        if (!jwtService.isValidJwtToken(token)) {
            LOG.debug(ErrorConstants.INVALID_TOKEN);
            model.addAttribute(ErrorConstants.ERROR, ErrorConstants.INVALID_TOKEN);
            return null;
        }

        var username = jwtService.getUsernameFromToken(token);
        var userDto = userService.findByUsername(username);


        if (Objects.isNull(userDto) || !token.equals(userDto.getVerificationToken())) {
          LOG.debug(ErrorConstants.INVALID_TOKEN);
          model.addAttribute(ErrorConstants.ERROR, ErrorConstants.INVALID_TOKEN);
          return null;
        }

        if (userDto.getUsername().equals(username) && userDto.isEnabled()) {
            LOG.debug(SignUpConstants.ACCOUNT_EXISTS);
            model.addAttribute(ErrorConstants.ERROR, SignUpConstants.ACCOUNT_EXISTS);
            return null;
        }

        if (userDto.getUsername().equals(username)) {
            UserUtils.enableUser(userDto);
            return userService.updateUser(userDto, UserHistoryType.VERIFIED);
        }

        return null;
    }
}

