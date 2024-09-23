package dev.start.init.service.user.impl;

import dev.start.init.annotation.Loggable;
import dev.start.init.constants.CacheConstants;
import dev.start.init.constants.user.RoleConstants;
import dev.start.init.constants.user.UserConstants;
import dev.start.init.dto.user.RoleDto;
import dev.start.init.dto.user.UserDto;
import dev.start.init.entity.user.Role;
import dev.start.init.entity.user.User;
import dev.start.init.enums.RoleType;
import dev.start.init.enums.UserHistoryType;
import dev.start.init.exception.ResourceNotFoundException;
import dev.start.init.exception.user.UserAlreadyExistsException;
import dev.start.init.mapper.UserMapper;
import dev.start.init.repository.user.RoleRepository;
import dev.start.init.repository.user.UserRepository;
import dev.start.init.service.impl.UserDetailsBuilder;
import dev.start.init.service.mail.EmailService;
import dev.start.init.service.security.EncryptionService;
import dev.start.init.service.security.JwtService;
import dev.start.init.service.user.RoleService;
import dev.start.init.service.user.UserService;
import dev.start.init.util.UserUtils;
import dev.start.init.util.core.ValidationUtils;
import dev.start.init.web.payload.request.SignUpRequest;
import dev.start.init.web.payload.response.UserResponse;

import java.io.IOException;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.Validate;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.lang.NonNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * The UserServiceImpl class provides implementation for the UserService definitions.
 *
 * @author Md Jewel
 * @version 1.0
 * @since 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl  implements UserService {


    private final Clock clock;
    private final RoleService roleService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final EncryptionService encryptionService;
    private final EmailService emailService;

    /**
     * Saves or updates the user with the user instance given.
     *
     * @param user the user with updated information
     * @param isUpdate if the operation is an update
     * @return the updated user.
     * @throws NullPointerException in case the given entity is {@literal null}
     */

    @Override
    @Transactional
    @Loggable
    public UserDto saveOrUpdate(final User user, final boolean isUpdate) {
        Validate.notNull(user, UserConstants.USER_MUST_NOT_BE_NULL);

        User persistedUser = isUpdate ? userRepository.saveAndFlush(user) : userRepository.save(user);
        LOG.debug(UserConstants.USER_PERSISTED_SUCCESSFULLY, persistedUser);

        return UserMapper.MAPPER.toUserDto(persistedUser);
    }


    /**
     * Create the userDto with the userDto instance given.
     *
     * @param userDto the userDto with updated information
     * @return the updated userDto.
     * @throws NullPointerException in case the given entity is {@literal null}
     *
     */

    @Override
    @Transactional
    public @NonNull UserDto createUser(final UserDto userDto) throws TemplateException, MessagingException, IOException {

        return createUser(userDto, userDto.getUserRoles());
    }


    @Override
    @Transactional
    public @NonNull UserDto updateUser(final UserDto userDto) {
        Validate.notNull(userDto, UserConstants.USER_DTO_MUST_NOT_BE_NULL);
        return persistUser(userDto, userDto.getUserRoles(), UserHistoryType.PROFILE_UPDATE, true);
    }

    /**
     * Create the userDto with the userDto instance given.
     *
     * @param userDto the userDto with updated information
     * @param roles the roles.
     * @return the updated userDto.
     * @throws NullPointerException in case the given entity is {@literal null}
     */

    @Override
    @Transactional
    @Loggable
    public @NonNull UserDto createUser(final UserDto userDto, final Set<RoleDto> roles) throws TemplateException, MessagingException, IOException {
        Validate.notNull(userDto, UserConstants.USER_DTO_MUST_NOT_BE_NULL);

        User localUser = userRepository.findByUsername(userDto.getUsername());
        //if user already exists check it verified or not
        if (localUser != null) {
            // If the user exists but has not been verified, then treat this as a new sign-up.
            if (!localUser.isEnabled()) {
                LOG.debug(UserConstants.USER_EXIST_BUT_NOT_ENABLED, userDto.getEmail(), localUser);
                return UserMapper.MAPPER.toUserDto(localUser);
            }

            LOG.warn(UserConstants.USER_ALREADY_EXIST, userDto.getEmail());
            throw new UserAlreadyExistsException(UserConstants.USER_ALREADY_EXIST);
        }

        //user is fresh user so add public id, and encrypted password and sa
        // Assign a public id to the user. This is used to identify the user in the system and can be
        // shared publicly over the internet.
        userDto.setPublicId(UUID.randomUUID().toString());

        // Update the user password with an encrypted copy of the password
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));

        var verificationToken = jwtService.generateJwtToken(userDto.getUsername());
        userDto.setVerificationToken(verificationToken);

        //calling mail service to verify enabled user
        String encryptedToken = encryptionService.encrypt(verificationToken);
        LOG.debug("Encrypted JWT token: {}", encryptedToken);
        String encodedToken = encryptionService.encode(encryptedToken);

        emailService.sendVerificationEmail(userDto,encodedToken);


        return persistUser(userDto, roles, UserHistoryType.CREATED, false);
    }

    @Override
    public Page<UserDto> findAll(Pageable pageable) {
        Page<User> usersPage = userRepository.findAll(pageable);

        // Convert each User entity to UserDto
        List<UserDto> userDtoList = UserMapper.MAPPER.toUserDto(usersPage.getContent());

        /*List<UserDto> UserDtos = usersPage.getContent().stream()
                .map(UserMapper.MAPPER::toUserDto)
                .collect(Collectors.toList());*/
        // Create a new Page of UserDto with the same pagination info

        return new PageImpl<>(userDtoList, pageable, usersPage.getTotalElements());
    }

/*    *//**
     * Returns users according to the details in the dataTablesInput or null if no user exists.
     *
     * @param dataTablesInput the dataTablesInput
     * @return the dataTablesOutput
     *//*
    @Override
    public DataTablesOutput<UserDto> getUsers(final DataTablesInput dataTablesInput) {
        return userRepository.findAll(dataTablesInput);
    }*/

    /**
     * Returns a user for the given id or null if a user could not be found.
     *
     * @param id The id associated to the user to find
     * @return a user for the given email or null if a user could not be found.
     * @throws NullPointerException in case the given entity is {@literal null}
     */
    @Override
    public UserDto findById(final Long id) {
        Validate.notNull(id, UserConstants.USER_ID_MUST_NOT_BE_NULL);

        User storedUser = userRepository.findById(id).orElse(null);
        if (Objects.isNull(storedUser)) {
            return null;
        }
        return UserMapper.MAPPER.toUserDto(storedUser);
    }

    /**
     * Returns a user for the given publicId or null if a user could not be found.
     *
     * @param publicId the publicId
     * @return the userDto
     * @throws NullPointerException in case the given entity is {@literal null}
     */
    @Override
    @Cacheable(CacheConstants.USERS)
    public UserDto findByPublicId(final String publicId) {
        Validate.notNull(publicId, UserConstants.BLANK_PUBLIC_ID);

        User storedUser = userRepository.findByPublicId(publicId);
        if (Objects.isNull(storedUser)) {
            return null;
        }
        return UserMapper.MAPPER.toUserDto(storedUser);
    }

    /**
     * Returns a user for the given username or null if a user could not be found.
     *
     * @param username The username associated to the user to find
     * @return a user for the given username or null if a user could not be found
     * @throws NullPointerException in case the given entity is {@literal null}
     */
    @Override
    @Cacheable(CacheConstants.USERS)
    public UserDto findByUsername(final String username) {
        Validate.notNull(username, UserConstants.BLANK_USERNAME);

        var storedUser = userRepository.findByUsername(username);
        if (Objects.isNull(storedUser)) {
            return null;
        }
        return UserMapper.MAPPER.toUserDto(storedUser);
    }

    /**
     * Returns a user for the given email or null if a user could not be found.
     *
     * @param email The email associated to the user to find
     * @return a user for the given email or null if a user could not be found
     * @throws NullPointerException in case the given entity is {@literal null}
     */
    @Override
    @Cacheable(CacheConstants.USERS)
    public UserDto findByEmail(final String email) {
        Validate.notNull(email, UserConstants.BLANK_EMAIL);

        User storedUser = userRepository.findByEmail(email);
        if (Objects.isNull(storedUser)) {
            return null;
        }
        return UserMapper.MAPPER.toUserDto(storedUser);
    }

    /**
     * Find all users that failed to verify their email after a certain time.
     *
     * @return List of users that failed to verify their email.
     */
    @Override
    public List<UserDto> findAllNotEnabledAfterAllowedDays() {
        var date = LocalDateTime.now(clock).minusDays(UserConstants.DAYS_TO_ALLOW_ACCOUNT_ACTIVATION);
        List<User> expiredUsers = userRepository.findByEnabledFalseAndCreatedAtBefore(date);

        return UserMapper.MAPPER.toUserDto(expiredUsers);
    }

    /**
     * Returns a userDetails for the given username or null if a user could not be found.
     *
     * @param username The username associated to the user to find
     * @return a user for the given username or null if a user could not be found
     * @throws NullPointerException in case the given entity is {@literal null}
     */
    @Override
    public UserDetails getUserDetails(final String username) {
        Validate.notNull(username, UserConstants.BLANK_USERNAME);

        User storedUser = userRepository.findByUsername(username);
        return UserDetailsBuilder.buildUserDetails(storedUser);
    }

    /**
     * Checks if the username already exists.
     *
     * @param username the username
     * @return <code>true</code> if username exists
     * @throws NullPointerException in case the given entity is {@literal null}
     */
    @Override
    public boolean existsByUsername(final String username) {
        Validate.notNull(username, UserConstants.BLANK_USERNAME);
        return userRepository.existsByUsernameOrderById(username);
    }

    /**
     * Checks if the username or email already exists and enabled.
     *
     * @param username the username
     * @param email the email
     * @return <code>true</code> if username exists
     * @throws NullPointerException in case the given entity is {@literal null}
     */
    @Override
    public boolean existsByUsernameOrEmailAndEnabled(final String username, final String email) {
        Validate.notNull(username, UserConstants.BLANK_USERNAME);
        Validate.notNull(email, UserConstants.BLANK_EMAIL);

        return userRepository.existsByUsernameAndEnabledTrueOrEmailAndEnabledTrueOrderById(
                username, email);
    }

    /**
     * Validates the username exists and the token belongs to the user with the username.
     *
     * @param username the username
     * @param token the token
     * @return if token is valid
     */
    @Override
    public boolean isValidUsernameAndToken(final String username, final String token) {
        Validate.notNull(username, UserConstants.BLANK_USERNAME);

        return userRepository.existsByUsernameAndVerificationTokenOrderById(username, token);
    }

    /**
     * Update the user with the user instance given and the update type for record.
     *
     * @param userDto The user with updated information
     * @param userHistoryType the history type to be recorded
     * @return the updated user
     * @throws NullPointerException in case the given entity is {@literal null}
     */
    @Override
    @Caching(
            evict = {
                    @CacheEvict(value = CacheConstants.USERS, key = "#userDto.username"),
                    @CacheEvict(value = CacheConstants.USERS, key = "#userDto.publicId"),
                    @CacheEvict(value = CacheConstants.USERS, key = "#userDto.email"),
                    @CacheEvict(value = CacheConstants.USER_DETAILS, allEntries = true)
            })
    @Transactional
    public UserDto updateUser(UserDto userDto, UserHistoryType userHistoryType) {
        Validate.notNull(userDto, UserConstants.USER_DTO_MUST_NOT_BE_NULL);

        userDto.setVerificationToken(null);
        return persistUser(userDto, Collections.emptySet(), userHistoryType, true);
    }

    /**
     * Enables the user by setting the enabled state to true.
     *
     * @param publicId The user publicId
     * @return the updated user
     * @throws NullPointerException in case the given entity is {@literal null}
     */
    @Override
    @Caching(
            evict = {
                    @CacheEvict(value = CacheConstants.USERS),
                    @CacheEvict(value = CacheConstants.USER_DETAILS, allEntries = true)
            })
    @Transactional
    public UserDto enableUser(final String publicId) {
        Validate.notNull(publicId, UserConstants.BLANK_PUBLIC_ID);

        User storedUser = userRepository.findByPublicId(publicId);
        LOG.debug("Enabling user {}", storedUser);

        if (Objects.nonNull(storedUser)) {
            storedUser.setEnabled(true);
            UserDto userDto = UserMapper.MAPPER.toUserDto(storedUser);

            return persistUser(userDto, Collections.emptySet(), UserHistoryType.ACCOUNT_ENABLED, true);
        }
        return null;
    }

    /**
     * Disables the user by setting the enabled state to false.
     *
     * @param publicId The user publicId
     * @return the updated user
     * @throws NullPointerException in case the given entity is {@literal null}
     */
    @Override
    @Caching(
            evict = {
                    @CacheEvict(value = CacheConstants.USERS),
                    @CacheEvict(value = CacheConstants.USER_DETAILS, allEntries = true)
            })
    @Transactional
    public UserDto disableUser(final String publicId) {
        Validate.notNull(publicId, UserConstants.BLANK_PUBLIC_ID);

        User storedUser = userRepository.findByPublicId(publicId);
        if (Objects.nonNull(storedUser)) {
            storedUser.setEnabled(false);
            UserDto userDto = UserUtils.convertToUserDto(storedUser);

            return persistUser(userDto, Collections.emptySet(), UserHistoryType.ACCOUNT_DISABLED, true);
        }
        return null;
    }

    /**
     * Delete the user with the user id given.
     *
     * @param publicId The publicId associated to the user to delete
     * @throws NullPointerException in case the given entity is {@literal null}
     */
    @Override
    @Caching(
            evict = {
                    @CacheEvict(value = CacheConstants.USERS, key = "#publicId"),
                    @CacheEvict(value = CacheConstants.USER_DETAILS, allEntries = true)
            })
    @Transactional
    public void deleteUser(final String publicId) {
        ValidationUtils.validateInputsWithMessage(UserConstants.BLANK_PUBLIC_ID, publicId);

        // Number of rows deleted is expected to be 1 since publicId is unique
        int numberOfRowsDeleted = userRepository.deleteByPublicId(publicId);
        LOG.debug("Deleted {} user(s) with publicId {}", numberOfRowsDeleted, publicId);
    }

    /**
     * Transfers user details to a user object then persist to database.
     *
     * @param userDto the userDto
     * @param roles the roles
     * @param historyType the user history type
     * @param isUpdate if the operation is an update
     * @return the userDto
     */
    private UserDto persistUser(
            final UserDto userDto,
            final Set<RoleDto> roles,
            final UserHistoryType historyType,
            final boolean isUpdate) {
        //System.out.println("Requested user : "+userDto);

        // Initialize roles with default role if not updating
        Set<RoleDto> localRoles = new HashSet<>(roles);
        if (!isUpdate) {
            localRoles.add(new RoleDto(RoleType.ROLE_SYSTEM_USER));
        }

        // Retrieve or convert User entity based on update status
        User user = isUpdate ? userRepository.findByPublicId(userDto.getPublicId())
                : UserMapper.MAPPER.toUser(userDto);

        if (isUpdate) UserMapper.MAPPER.updateUserFromUserDto(userDto,user);

        // Clear existing roles
        //user.getRoles().clear();

        // Fetch and add roles
        Set<Role> roleSet = localRoles.stream()
                .map(roleDto -> {
                    // Fetch role by name
                    //System.out.println(roleDto.getName()+"User role");

                    Role storedRole = roleService.findByName(roleDto.getName());
                    // Optionally, check if the role is null or handle it
                    if (storedRole != null) {
                        roleService.save(storedRole);
                        return storedRole;
                    }
                    // If role is not found, you may choose to handle it differently, e.g., logging or throwing an exception
                    throw new ResourceNotFoundException("Role not found: " + roleDto.getName());
                })
                .collect(Collectors.toSet());

        // Add user history
        //user.addUserHistory(new UserHistory(UUID.randomUUID().toString(), user, historyType));

        user.setUserRoles(roleSet);

        LOG.debug("User to create {}",user);

        return saveOrUpdate(user, isUpdate);
    }
}


