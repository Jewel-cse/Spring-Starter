package dev.start.init.service.user;

import dev.start.init.constants.CacheConstants;
import dev.start.init.dto.user.RoleDto;
import dev.start.init.dto.user.UserDto;
import dev.start.init.entity.user.User;
import dev.start.init.enums.UserHistoryType;
import dev.start.init.exception.user.EmailExistsException;
import dev.start.init.web.payload.response.UserResponse;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.lang.NonNull;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * This UserService interface is the contract for the user service operations.
 *
 * @author Md Jewel
 * @version 1.0
 * @since 1.0
 */
public interface UserService {

    /**
     * Create the userDto with the userDto instance given.
     *
     * @param userDto the userDto with updated information
     * @return the updated userDto.
     * @throws NullPointerException in case the given entity is {@literal null}
     */
    UserDto registerNewUserAccount(UserDto userDto) throws EmailExistsException;

    @NonNull
    UserDto updateUser(final UserDto userDto);


    /**
     * Returns users.
     *
     * @param pageable the pageable
     * @return the users
     */
    Page<UserDto> findAll(final Pageable pageable);

    /**
     * Returns a user for the given id or null if a user could not be found.
     *
     * @param id The id associated to the user to find
     * @return a user for the given email or null if a user could not be found.
     * @throws NullPointerException in case the given entity is {@literal null}
     */
    UserDto findById(Long id);

    /**
     * Returns a user for the given publicId or null if a user could not be found.
     *
     * @param publicId the publicId
     * @return the userDto
     * @throws NullPointerException in case the given entity is {@literal null}
     */
    UserDto findByPublicId(String publicId);

    /**
     * Returns a user for the given username or null if a user could not be found.
     *
     * @param username The username associated to the user to find
     * @return a user for the given username or null if a user could not be found
     * @throws NullPointerException in case the given entity is {@literal null}
     */
    UserDto findByUsername(String username);

    @Cacheable(CacheConstants.USERS)
    UserDto findByEmail(String email);

    List<UserDto> findAllNotEnabledAfterAllowedDays();

    UserDetails getUserDetails(String username);

    /**
     * Checks if the username or email already exists and enabled.
     *
     * @param username the username
     * @param email the email
     * @return <code>true</code> if username exists
     */
    boolean existsByUsernameOrEmailAndEnabled(String username, String email);


    /**
     * Enables the user by setting the enabled state to true.
     *
     * @param publicId The user publicId
     * @return the updated user
     * @throws NullPointerException in case the given entity is {@literal null}
     */
    UserDto enableUser(String publicId);

    /**
     * Disables the user by setting the enabled state to false.
     *
     * @param publicId The user publicId
     * @return the updated user
     * @throws NullPointerException in case the given entity is {@literal null}
     */
    UserDto disableUser(String publicId);

    /**
     * Delete the user with the user id given.
     *
     * @param publicId The publicId associated to the user to delete
     * @throws NullPointerException in case the given entity is {@literal null}
     */
    UserDto deleteUser(String publicId);
}
