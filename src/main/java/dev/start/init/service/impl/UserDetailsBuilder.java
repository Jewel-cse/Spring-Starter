package dev.start.init.service.impl;

import java.io.Serial;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import dev.start.init.constants.user.UserConstants;
import dev.start.init.entity.user.User;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.Validate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * UserDetailsBuilder builds the userDetails to be used by the application security context.
 *
 * @author Md Jewel
 * @version 1.0
 * @since 1.0
 */
@Data
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public final class UserDetailsBuilder implements UserDetails {
    @Serial private static final long serialVersionUID = -8755873164632782035L;

    private Long id;
    @EqualsAndHashCode.Include private String email;
    @EqualsAndHashCode.Include private String publicId;
    @EqualsAndHashCode.Include private String username;
    private String firstName;
    private String lastName;
    private String password;
    private String phone;
    private boolean enabled;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private int failedLoginAttempts;
    private LocalDateTime lastSuccessfulLogin;

    private Collection<? extends GrantedAuthority> authorities;

    /**
     * Builds userDetails object from the specified user.
     *
     * @param user the user
     * @throws NullPointerException if the user is null
     * @return the userDetails
     */
    public static UserDetailsBuilder buildUserDetails(final User user) {
        Validate.notNull(user, UserConstants.USER_MUST_NOT_BE_NULL);

        // Build the authorities from the user's roles
        Set<GrantedAuthority> authorities = new HashSet<>();
        user.getRoles()
                .forEach(
                        userRole -> {
                            if (Objects.nonNull(userRole)) {
                                authorities.add(new SimpleGrantedAuthority(userRole.getName()));
                            }
                        });

        return UserDetailsBuilder.builder()
                .id(user.getId())
                .email(user.getEmail())
                .publicId(user.getPublicId())
                .username(user.getUsername())
                .password(user.getPassword())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .publicId(user.getPublicId())
                .enabled(user.isEnabled())
                .failedLoginAttempts(user.getFailedLoginAttempts())
                .lastSuccessfulLogin(user.getLastSuccessfulLogin())
                .accountNonExpired(user.isAccountNonExpired())
                .accountNonLocked(user.isAccountNonLocked())
                .credentialsNonExpired(user.isCredentialsNonExpired())
                .authorities(authorities)
                .build();
    }
}

