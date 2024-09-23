package dev.start.init.entity.user;

import dev.start.init.constants.SequenceConstants;
import dev.start.init.constants.user.UserConstants;
import dev.start.init.entity.base.BaseEntity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.SequenceGenerator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

/**
 * The user model for the application.
 *
 * @author Md Jewel
 * @version 1.0
 * @since 1.0
 */
@Entity
@Data
//@Audited
@Table(name = "users")
public class User extends BaseEntity<Long> implements Serializable {
    @Serial private static final long serialVersionUID = 7538542321562810251L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,  generator = SequenceConstants.USER_SEQUENCE)
    @SequenceGenerator(name = SequenceConstants.USER_SEQUENCE,  initialValue = SequenceConstants.USER_SEQUENCE_INITIAL_VALUE, allocationSize = SequenceConstants.USER_SEQUENCE_ALLOCATION)
    private Long id;

    @Column(unique = true, nullable = false)
    @NotBlank(message = UserConstants.BLANK_USERNAME)
    @Size(min = 3, max = 50, message = UserConstants.USERNAME_SIZE)
    private String username;

    @Column(unique = true, nullable = false)
    @NotBlank(message = UserConstants.BLANK_EMAIL)
    @Email(message = UserConstants.INVALID_EMAIL)
    private String email;

    @JsonIgnore
    @ToString.Exclude
    @NotBlank(message = UserConstants.BLANK_PASSWORD)
    private String password;

    private String firstName;
    private String middleName;
    private String lastName;
    private String phone;
    private String profileImage;
    private String verificationToken;

    private int failedLoginAttempts;
    private LocalDateTime lastSuccessfulLogin;

    private boolean enabled;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;

    @NotAudited
    @ManyToMany(fetch = FetchType.LAZY )
    @JoinTable(name = "role_user",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    @JsonIgnore
    @ToString.Exclude
    private Set<Role> userRoles = new HashSet<>();

//  @NotAudited
//  @ToString.Exclude
//  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
//  private Set<UserRole> userRoles = new HashSet<>();

//  @NotAudited
//  @ToString.Exclude
//  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
//  private Set<UserHistory> userHistories = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof User user) || !super.equals(o)) {
            return false;
        }
        return Objects.equals(getPublicId(), user.getPublicId())
                && Objects.equals(getUsername(), user.getUsername())
                && Objects.equals(getEmail(), user.getEmail());
    }


    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getPublicId(), getUsername(), getEmail());
    }

    /**
     * Add userRole to this User.
     *
     * @param role the role

    public void addUserRole(final Role role) {
    var userRole = new UserRole(this, role);
    userRoles.add(userRole);
    userRole.setUser(this);
    }
     */
    /**
     * Remove userRole from this User.
     *
     * @param role the role

    public void removeUserRole(final Role role) {
    var userRole = new UserRole(this, role);
    userRoles.remove(userRole);
    userRole.setUser(null);
    }

     */

    /**
     * Add a UserHistory to this user.
     *
     * @param userHistory userHistory to be added.
     */
//  public void addUserHistory(final UserHistory userHistory) {
//    userHistories.add(userHistory);
//    userHistory.setUser(this);
//  }

    /**
     * Formulates the full name of the user.
     *
     * @return the full name of the user
     */
    public String getName() {
        return StringUtils.joinWith(StringUtils.SPACE, getFirstName(), getMiddleName(), getLastName());
    }
}


