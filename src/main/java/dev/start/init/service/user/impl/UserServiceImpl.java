package dev.start.init.service.user.impl;

import dev.start.init.constants.CacheConstants;
import dev.start.init.constants.user.UserConstants;
import dev.start.init.dto.user.UserDto;
import dev.start.init.entity.user.User;
import dev.start.init.entity.user.UserHistory;
import dev.start.init.enums.RoleType;
import dev.start.init.enums.UserHistoryType;
import dev.start.init.exception.ResourceNotFoundException;
import dev.start.init.exception.user.EmailExistsException;
import dev.start.init.exception.user.UserNotFoundException;
import dev.start.init.mapper.UserMapper;
import dev.start.init.repository.user.RoleRepository;
import dev.start.init.repository.user.UserHistoryRepository;
import dev.start.init.repository.user.UserRepository;
import dev.start.init.service.impl.UserDetailsBuilder;
import dev.start.init.service.mail.EmailService;
import dev.start.init.service.security.EncryptionService;
import dev.start.init.service.security.JwtService;
import dev.start.init.service.user.RoleService;
import dev.start.init.service.user.UserService;
import dev.start.init.util.core.ValidationUtils;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.Validate;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserHistoryRepository userHistoryRepository;
    /**
     * Create the userDto with the userDto instance given.
     *
     * @param accountDto the userDto with updated information
     * @return the updated userDto.
     * @throws NullPointerException in case the given entity is {@literal null}
     *
     */
    @Override
    @Transactional
    public UserDto registerNewUserAccount(UserDto accountDto) throws EmailExistsException {

        if (emailExist(accountDto.getEmail())) {
            throw new EmailExistsException
                    ("There is an account with that email adress: " + accountDto.getEmail());
        }
        User user = new User();
        user.setFirstName(accountDto.getFirstName());
        user.setLastName(accountDto.getLastName());
        user.setUsername(accountDto.getUsername());
        user.setPassword(passwordEncoder.encode(accountDto.getPassword()));
        user.setEmail(accountDto.getEmail());

        user.setRoles(Arrays.asList(roleRepository.findByName(RoleType.ROLE_USER.getName())));
        //return UserMapper.MAPPER.toUserDto(userRepository.save(user)) ;
        LOG.info("user:{}  -is to be create",user);
        User responseUser = userRepository.save(user);
        LOG.info("user Response :{}  -is created",responseUser);
        if(responseUser!=null) {
            UserHistory userHistory = new UserHistory();
            userHistory.setUserId(user.getId());
            userHistory.setUserHistoryType(UserHistoryType.CREATED);
            userHistory.setDescription("User doing registration");

            userHistoryRepository.save(userHistory);
            LOG.info("User History :{}is created ",userHistory);
        }
        return UserMapper.MAPPER.toUserDto(responseUser);
    }

    /**
     * Update the user with the userDto instance given.
     *
     * @param userDto the user with updated information
     * @return the updated user.
     * @throws NullPointerException in case the given entity is {@literal null}
     */

    @Override
    @Transactional
    public @NonNull UserDto updateUser(final UserDto userDto) {

        User user = userRepository.findByPublicId(userDto.getPublicId());
        if (user==null){
            throw new ResourceNotFoundException(UserConstants.USER_NOT_FOUND);
        }
        //message null means user is valid
        String message = isUserStatusInValid(user);
        if( message != null){
            throw new RuntimeException(message);
        }

        UserMapper.MAPPER.updateUserFromUserDto(userDto, user);
        User updatedUser = userRepository.save(user);
        LOG.info("User :{}  -is Updated",updatedUser);
        if(updatedUser!=null){
            UserHistory userHistory = new UserHistory();
            userHistory.setUserId(updatedUser.getId());
            userHistory.setUserHistoryType(UserHistoryType.PROFILE_UPDATE);
            userHistory.setDescription("User doing update");
            userHistoryRepository.save(userHistory);
            LOG.info("User History :{}is added ",userHistory);
        }
        return UserMapper.MAPPER.toUserDto(updatedUser);
    }


/*
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
*/

    @Override
    public Page<UserDto> findAll(Pageable pageable) {
        Page<User> usersPage = userRepository.findAll(pageable);
        // Convert each User entity to UserDto
        List<UserDto> userDtoList = UserMapper.MAPPER.toUserDto(usersPage.getContent());
        // Create a new Page of UserDto with the same pagination info
        return new PageImpl<>(userDtoList, pageable, usersPage.getTotalElements());
    }


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
    @Cacheable(CacheConstants.USERS)
    @Override
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

        if (Objects.isNull(storedUser)) {
            throw new UserNotFoundException(UserConstants.USER_NOT_FOUND);
        }
        LOG.debug("Enabling user {}", storedUser);
        storedUser.setEnabled(true);

        return UserMapper.MAPPER.toUserDto(userRepository.save(storedUser));
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

        if (Objects.isNull(storedUser)) {
            throw new UserNotFoundException(UserConstants.USER_NOT_FOUND);
        }
        LOG.debug("Disable user {}", storedUser);
        storedUser.setEnabled(false);

        return UserMapper.MAPPER.toUserDto(userRepository.save(storedUser));
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
    public UserDto deleteUser(final String publicId) {
        ValidationUtils.validateInputsWithMessage(UserConstants.BLANK_PUBLIC_ID, publicId);

        //this is just soft delete that means we just set isActive false
        //int numberOfRowsDeleted = userRepository.deleteByPublicId(publicId);

        User user = userRepository.findByPublicId(publicId);
        LOG.debug("Deleted {} user(s) with publicId {}", user, publicId);
        user.setActive(false);

        return UserMapper.MAPPER.toUserDto(userRepository.save(user));
    }

   /* private UserDto persistUser(
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

        user.setRoles(roleSet);

        LOG.debug("User to create {}",user);

        return saveOrUpdate(user, isUpdate);
    }*/

    public boolean emailExist(final String email) {
        return userRepository.findByEmail(email) != null;
    }

    /*
    * This method checks the validity of user
    * by providing a string which is error message
    * if it provides null that means user status valid
    * */
    public String isUserStatusInValid(final User user){
        if(!user.isActive()){
            return UserConstants.USER_DISABLED_MESSAGE;
        }
        else if(!user.isAccountNonExpired()){
            return UserConstants.USER_EXPIRED_MESSAGE;
        }
        else if(!user.isAccountNonLocked()){
            return UserConstants.USER_LOCKED_MESSAGE;
        }
        else if (!user.isCredentialsNonExpired()){
            return UserConstants.USER_CREDENTIALS_EXPIRED_MESSAGE;
        }
        return null;
    }
}


