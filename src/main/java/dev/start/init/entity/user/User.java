package dev.start.init.entity.auth;

import dev.start.init.constants.SequenceConstants;
import dev.start.init.entity.base.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@Entity
@Table(name = "USERS")
public class User extends BaseEntity<Long> implements Serializable, UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SequenceConstants.USER_SEQUENCE)
    @SequenceGenerator(name = SequenceConstants.USER_SEQUENCE,
            sequenceName = "UserSequence",
            initialValue = SequenceConstants.USER_SEQUENCE_INITIAL_VALUE,
            allocationSize = SequenceConstants.USER_SEQUENCE_ALLOCATION)
    private Long id;

    @Size(max = 100)
    @NotBlank(message = "Password is required")
    @Column(name = "PASSWORD", nullable = false)
    private String password;

    @Size(max = 100)
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Column(name = "EMAIL", unique = true, nullable = false)
    private String email;

    @Size(max = 100)
    @Column(name = "FIRST_NAME")
    private String firstName;

    @Size(max = 100)
    @Column(name = "LAST_NAME")
    private String lastName;

    @Column(name = "VERIFICATION_TOKEN")
    private String verificationToken;

    @Column(name = "PASSWORD_RESET_TOKEN")
    private String passwordResetToken;

    @Column(nullable = false)
    private boolean isEnabled = false;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "USER_ROLE",
            joinColumns = @JoinColumn(name = "USER_ID"),
            inverseJoinColumns = @JoinColumn(name = "ROLE_ID")
    )
    private Set<Role> roles=null;

    @Column(name = "LAST_LOGIN")
    private LocalDateTime lastLogin;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(roles.toString()));
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}


