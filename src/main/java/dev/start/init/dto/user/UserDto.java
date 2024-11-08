package dev.start.init.dto.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import dev.start.init.constants.user.UserConstants;
import dev.start.init.dto.BaseDto;
import dev.start.init.entity.user.Role;
import dev.start.init.entity.user.UserHistory;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import jakarta.validation.constraints.Size;
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
public class UserDto extends BaseDto implements Serializable {
    @Serial private static final long serialVersionUID = -6342630857637389028L;

    private String publicId;

    @NotBlank(message = UserConstants.BLANK_USERNAME)
    @Size(min = 3, max = 50, message = UserConstants.USERNAME_SIZE)
    private String username;

    @Size(min = 4, message = UserConstants.PASSWORD_SIZE)
    @NotBlank(message = UserConstants.BLANK_PASSWORD)
    @ToString.Exclude
    @JsonIgnore
    private String password;

    private String firstName;
    private String middleName;
    private String lastName;

    @NotBlank(message = UserConstants.BLANK_EMAIL)
    @Email(message = UserConstants.INVALID_EMAIL)
    private String email;

    private String phone;
    private String profileImage;

    private int failedLoginAttempts;
    private LocalDateTime lastSuccessfulLogin;


    private boolean enabled ;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private boolean mfaEnable;


    private Collection<Role> roles;

    public String getName() {
        return StringUtils.joinWith(StringUtils.SPACE, getFirstName(), getMiddleName(), getLastName());
    }
}

