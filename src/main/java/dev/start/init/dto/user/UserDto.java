package dev.start.init.dto.auth;

import dev.start.init.entity.user.Role;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * DTO for user data transfer.
 */
@Data
public class UserDto implements Serializable {

    private Long id;
    private String publicId;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private LocalDateTime lastLogin;
    private Set<Role> roles;
}

