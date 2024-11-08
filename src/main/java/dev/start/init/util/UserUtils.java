package dev.start.init.util;

import dev.start.init.constants.ErrorConstants;
import dev.start.init.constants.user.ProfileConstants;
import dev.start.init.constants.user.RoleConstants;
import dev.start.init.constants.user.UserConstants;
import dev.start.init.dto.user.RoleDto;
import dev.start.init.dto.user.UserDto;
import dev.start.init.dto.user.UserHistoryDto;
import dev.start.init.entity.user.Role;
import dev.start.init.entity.user.User;
import dev.start.init.entity.user.UserHistory;
import dev.start.init.enums.RoleType;
import dev.start.init.mapper.UserMapper;
import dev.start.init.service.impl.UserDetailsBuilder;
import dev.start.init.util.core.ValidationUtils;
import dev.start.init.web.payload.request.SignUpRequest;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;

import net.datafaker.Faker;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.validator.routines.EmailValidator;

/**
 * User utility class that holds user-related methods used across application.
 *
 * @author Md Jewel
 * @version 1.0
 * @since 1.0
 */
public final class UserUtils {

    public static final int PASSWORD_MAX_LENGTH = 15;
    private static final int PASSWORD_MIN_LENGTH = 6;
    private static final Faker FAKER = new Faker();

    private UserUtils() {
        throw new AssertionError(ErrorConstants.NOT_INSTANTIABLE);
    }

    /**
     * Generates a UserDto with random values using Faker.
     *
     * @param enabled whether the user account should be enabled
     * @return UserDto with randomly generated details
     */
    public static User getDummyUser(final boolean enabled,Role role) {
        UserDto user = new UserDto();
        user.setPublicId(FAKER.idNumber().valid());
        user.setUsername(FAKER.internet().username());
        user.setPassword(FAKER.internet().password(PASSWORD_MIN_LENGTH, PASSWORD_MAX_LENGTH));
        user.setFirstName(FAKER.name().firstName());
        user.setMiddleName(FAKER.name().firstName());
        user.setLastName(FAKER.name().lastName());
        user.setEmail(FAKER.internet().emailAddress());
        user.setPhone(FAKER.phoneNumber().cellPhone());
        user.setProfileImage(FAKER.avatar().image());
        user.setFailedLoginAttempts(0);
        user.setLastSuccessfulLogin(LocalDateTime.now().minusDays(FAKER.number().numberBetween(0, 30)));

        if(enabled){
            user.setEnabled(true);
            user.setAccountNonExpired(true);
            user.setAccountNonLocked(true);
            user.setCredentialsNonExpired(true);
        }
        user.setMfaEnable(FAKER.bool().bool());

        user.setRoles(Collections.singleton(role));

        return UserMapper.MAPPER.toUser(user);
    }

    /**
     * Create a user.
     *
     * @return a user
     */
    public static User createUser() {
        return createUser(FAKER.internet().username());
    }

    /**
     * Create a test user with flexibility.
     *
     * @param enabled if the user should be enabled or disabled
     * @return the user
     */
    public static User createUser(final boolean enabled) {
        return createUser(
                FAKER.internet().username(),
                FAKER.internet().password(PASSWORD_MIN_LENGTH, PASSWORD_MAX_LENGTH),
                FAKER.internet().emailAddress(),
                enabled);
    }

    /**
     * Create a user with some flexibility.
     *
     * @param username username used to create user.
     * @param roleType the role type
     * @return a user
     */
    public static User createUser(String username, RoleType roleType) {
        var user = createUser(username);
        user.getRoles().add(new Role(roleType));
        return user;
    }

    /**
     * Create a user with some flexibility.
     *
     * @param username username used to create user.
     * @return a user
     */
    public static User createUser(String username) {
        return createUser(
                username,
                FAKER.internet().password(PASSWORD_MIN_LENGTH, PASSWORD_MAX_LENGTH),
                FAKER.internet().emailAddress());
    }

    /**
     * Create a user with some flexibility.
     *
     * @param username username used to create user
     * @param password password used to create user.
     * @param email email used to create user.
     * @return a user
     */
    public static User createUser(String username, String password, String email) {
        return createUser(username, password, email, false);
    }

    /**
     * Create user with some flexibility.
     *
     * @param username username used to create user.
     * @param password password used to create user.
     * @param email email used to create user.
     * @param enabled boolean value used to evaluate if user enabled.
     * @return a user
     */
    public static User createUser(String username, String password, String email, boolean enabled) {
        var user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        user.setPhone(FAKER.phoneNumber().cellPhone());

        var name = FAKER.name().nameWithMiddle();
        var fullName = name.split(" ");
        user.setFirstName(fullName[0]);
        user.setMiddleName(fullName[1]);
        user.setLastName(fullName[2]);

        if (enabled) {
            user.setEnabled(true);
            user.setAccountNonExpired(true);
            user.setAccountNonLocked(true);
            user.setCredentialsNonExpired(true);
        }
        return user;
    }

    /**
     * Create a test user with flexibility.
     *
     * @param username the username
     * @return the userDto
     */
    public static UserDto createUserDto(final String username) {
        return UserUtils.convertToUserDto(createUser(username));
    }

    /**
     * Create a test user with flexibility.
     *
     * @param enabled if the user should be enabled or disabled
     * @return the userDto
     */
    public static UserDto createUserDto(final boolean enabled) {
        return createUserDto(FAKER.internet().username(), enabled);
    }

    /**
     * Create a test user with flexibility.
     *
     * @param username the username
     * @param enabled if the user should be enabled to authenticate
     * @return the userDto
     */
    public static UserDto createUserDto(final String username, boolean enabled) {
        var userDto = UserUtils.convertToUserDto(createUser(username));
        if (enabled) {
            enableUser(userDto);
        }
        return userDto;
    }

    /**
     * Create user with some flexibility.
     *
     * @param username username used to create user.
     * @param password password used to create user.
     * @param email email used to create user.
     * @param enabled boolean value used to evaluate if user enabled.
     * @return a userDto
     */
    public static UserDto createUserDto(
            String username, String password, String email, boolean enabled) {
        var user = createUser(username, password, email, enabled);

        return UserUtils.convertToUserDto(user);
    }

    /**
     * Transfers data from entity to transfer object.
     *
     * @param user stored user
     * @return user dto
     */
    public static UserDto convertToUserDto(final User user) {
        var userDto = UserMapper.MAPPER.toUserDto(user);
        Validate.notNull(userDto, UserConstants.USER_DTO_MUST_NOT_BE_NULL);
        return userDto;
    }

    /**
     * Transfers data from signUpRequest object to transfer object.
     *
     * @param signUpRequest the signup request
     * @return user dto
     */
    public static UserDto convertToUserDto(final SignUpRequest signUpRequest) {
        var userDto = UserMapper.MAPPER.toUserDto(signUpRequest);
        // for now enabled all users
        Validate.notNull(userDto, UserConstants.USER_DTO_MUST_NOT_BE_NULL);
        return userDto;
    }

    /**
     * Transfers data from entity to transfer object.
     *
     * @param users stored users
     * @return user dto
     */
    public static List<UserDto> convertToUserDto(final List<User> users) {
        var userDtoList = UserMapper.MAPPER.toUserDto(users);
        Validate.notNull(userDtoList, UserConstants.USER_DTO_MUST_NOT_BE_NULL);
        return userDtoList;
    }

    /**
     * Transfers data from userDetails to dto object.
     *
     * @param userDetailsBuilder stored user details
     * @return user dto
     */
    public static UserDto convertToUserDto(UserDetailsBuilder userDetailsBuilder) {
        var userDto = UserMapper.MAPPER.toUserDto(userDetailsBuilder);
        Validate.notNull(userDetailsBuilder, "userDetailsBuilder cannot be null");
        return userDto;
    }

    /**
     * Transfers data from a transfer object to an entity.
     *
     * @param userDto the userDto
     * @return user
     */
    public static User convertToUser(final UserDto userDto) {
        var user = UserMapper.MAPPER.toUser(userDto);
        Validate.notNull(userDto, UserConstants.USER_DTO_MUST_NOT_BE_NULL);
        return user;
    }



    /**
     * Enables and unlocks a user.
     *
     * @param userDto the userDto
     */
    public static void enableUser(final UserDto userDto) {
        Validate.notNull(userDto, UserConstants.USER_DTO_MUST_NOT_BE_NULL);
        userDto.setEnabled(true);
        userDto.setAccountNonExpired(true);
        userDto.setAccountNonLocked(true);
        userDto.setCredentialsNonExpired(true);
        userDto.setFailedLoginAttempts(0);
    }

    /**
     * Verifies input string is an email.
     *
     * @param email email.
     * @return true if a pattern matches valid3 email, otherwise false.
     */
    public static boolean isEmail(String email) {
        return EmailValidator.getInstance().isValid(email);
    }

    /**
     * Retrieves the roles from the userRoles.
     *
     * @param userRoles the userRoles
     * @return list of the roles as strings
     */
    public static List<String> getRoles(Set<Role> userRoles) {
        List<String> roles = new ArrayList<>();

        for (Role userRole : userRoles) {
            if (Objects.nonNull(userRole)) {
                roles.add(userRole.getName());
            }
        }
        return roles;
    }

    /**
     * Returns the role with the highest precedence if user has multiple roles.
     *
     * @param user the user
     * @return the role
     */
    /*public static String getTopmostRole(User user) {
        ValidationUtils.validateInputs(user);

        if (Objects.isNull(user.getRoles())) {
            return null;
        }

        List<String> roles = getRoles(user.getUserRoles());

        if (roles.contains(RoleType.ROLE_ADMIN.getName())) {
            return RoleType.ROLE_ADMIN.getName();
        }

        return RoleType.ROLE_USER.getName();
    }
*/
    /**
     * Returns the user profile or random image if not found.
     *
     * @param user the user
     * @return profile image
     */
    public static String getUserProfileImage(User user) {
        if (StringUtils.isBlank(user.getProfileImage())) {
            return ProfileConstants.PIC_SUM_PHOTOS_150_RANDOM;
        }

        return user.getProfileImage();
    }

    /**
     * Transfers data from entity to a returnable object.
     *
     * @return user dto
     */
    public static Function<User, UserDto> getUserResponse() {
        return UserMapper.MAPPER::toUserDto;
    }

    //used to controller level if necessary
    /*public static UserResponse toUserResponse(UserDto userDto) {
        if ( userDto == null ) {
            return null;
        }

        //System.out.println("Response dto tis : "+userDto);
        UserResponse userResponse = new UserResponse();

        userResponse.setPublicId( userDto.getPublicId() );
        userResponse.setUsername( userDto.getUsername() );
        userResponse.setFirstName( userDto.getFirstName() );
        userResponse.setLastName( userDto.getLastName() );
        userResponse.setEmail( userDto.getEmail() );
        userResponse.setPhone( userDto.getPhone() );
        userResponse.setEnabled( userDto.isEnabled() );

        //extract the role name from the RoleDto
        List<String>userRoles =  userDto.getUserRoles().stream()
                .map(role -> role.getName()).collect(Collectors.toList());

        userResponse.setUserRoles(userRoles);
        return userResponse;
    }
*/
    /*public static List<UserResponse> toUserResponse(List<UserDto> usertos) {
        if ( usertos == null ) {
            return null;
        }
        List<UserResponse> userResponses = new ArrayList<>();
        for (UserDto userDto : usertos) {
            userResponses.add(toUserResponse(userDto));
        }

        return userResponses;
    }
*/
    private static List<RoleDto> userRoleSetToRoleList(Set<Role> userRoles) {
        List<RoleDto> roles = new ArrayList<>();
        for (Role usrRole : userRoles){
            roles.add(new RoleDto(usrRole.getId(), usrRole.getName()));
        }
        return roles;
    }

    /*public static void updateUserFromDto(UserDto userDto, User user) {
        if (userDto == null || user == null) {
            return; // or throw an exception
        }

        if (userDto.getUsername() != null) {
            user.setUsername(userDto.getUsername());
        }

        if (userDto.getEmail() != null) {
            user.setEmail(userDto.getEmail());
        }

//    if (userDto.getPassword() != null) {
//      user.setPassword(userDto.getPassword());
//    }

        if (userDto.getFirstName() != null) {
            user.setFirstName(userDto.getFirstName());
        }

        if (userDto.getMiddleName() != null) {
            user.setMiddleName(userDto.getMiddleName());
        }

        if (userDto.getLastName() != null) {
            user.setLastName(userDto.getLastName());
        }

        if (userDto.getPhone() != null) {
            user.setPhone(userDto.getPhone());
        }

        if (userDto.getProfileImage() != null) {
            user.setProfileImage(userDto.getProfileImage());
        }

        if (userDto.getVerificationToken() != null) {
            user.setVerificationToken(userDto.getVerificationToken());
        }

        //user.setFailedLoginAttempts(userDto.getFailedLoginAttempts());
        //user.setLastSuccessfulLogin(userDto.getLastSuccessfulLogin());

        user.setEnabled(userDto.isEnabled());
        //user.setAccountNonExpired(userDto.isAccountNonExpired());
        //user.setAccountNonLocked(userDto.isAccountNonLocked());
        //user.setCredentialsNonExpired(userDto.isCredentialsNonExpired());

//    if (userDto.getUserRoles() != null) {
//      // Assuming you want to clear existing roles and add new ones from the DTO
//      user.getRoles().clear();
//      userDto.getUserRoles().forEach(roleDto -> {
//        Role role = new Role();
//        role.setId(roleDto.getId()); // Assuming RoleDto has an ID
//        role.setName(roleDto.getName()); // Assuming RoleDto has a name
//        user.getRoles().add(role);
//      });
//    }

        // UserHistories should typically not be updated like this, since they are usually records of actions.
        // If you do want to update them, you can add a similar block for userHistories.
    }
*/

    public static String addRolePrefix(String roleName) {
        if (!roleName.startsWith(RoleConstants.ROLE_PREFIX)) {
            return RoleConstants.ROLE_PREFIX + roleName;
        }
        return roleName;
    }

    public static String removeRolePrefix(String roleName) {
        if (roleName.startsWith(RoleConstants.ROLE_PREFIX)) {
            return roleName.substring(RoleConstants.ROLE_PREFIX.length());
        }
        return roleName;
    }

}
