package dev.start.init.dto.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import dev.start.init.constants.user.UserConstants;
import dev.start.init.entity.user.UserHistory;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

/**
 * The UserDto transfers user details from outside into the application and vice versa.
 *
 * @author Md Jewel
 * @version 1.0
 * @since 1.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDto  implements Serializable {
    @Serial private static final long serialVersionUID = -6342630857637389028L;

    @EqualsAndHashCode.Include private String publicId;

    @EqualsAndHashCode.Include
    @NotBlank(message = UserConstants.BLANK_USERNAME)
    private String username;

    @ToString.Exclude
    //@NotBlank(message = UserConstants.BLANK_PASSWORD)
    private String password;

    private String firstName;
    private String middleName;
    private String lastName;

    @EqualsAndHashCode.Include
    @NotBlank(message = UserConstants.BLANK_EMAIL)
    @Email(message = UserConstants.INVALID_EMAIL)
    private String email;

    private String phone;
    private String profileImage;
    private String verificationToken;

    private int failedLoginAttempts;
    private LocalDateTime lastSuccessfulLogin;

    private boolean enabled;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;


    private Set<RoleDto> userRoles = new HashSet<>();

    private Set<UserHistory> userHistories = new HashSet<>();

    /**
     * Formulates the full name of the user.
     *
     * @return the full name of the user
     */

    public String getName() {
        return StringUtils.joinWith(StringUtils.SPACE, getFirstName(), getMiddleName(), getLastName());
    }
}

