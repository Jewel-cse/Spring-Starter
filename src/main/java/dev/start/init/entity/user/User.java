package dev.start.init.entity.user;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import dev.start.init.constants.SequenceConstants;
import dev.start.init.entity.base.BaseEntity;
import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;


/**
 * The user model for the application.
 *
 * @author Md Jewel
 * @version 1.0
 * @since 1.0
 */
@Entity
@Data
@Table(name = "users")
@NoArgsConstructor
public class User extends BaseEntity<Long> implements Serializable {
    @Serial private static final long serialVersionUID = 7538542321562810251L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,  generator = SequenceConstants.USER_SEQUENCE)
    @SequenceGenerator(name = SequenceConstants.USER_SEQUENCE,  initialValue = SequenceConstants.USER_SEQUENCE_INITIAL_VALUE, allocationSize = SequenceConstants.USER_SEQUENCE_ALLOCATION)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    private String password;

    private String firstName;
    private String middleName;
    private String lastName;
    private String phone;
    private String profileImage;

    private int failedLoginAttempts;
    private LocalDateTime lastSuccessfulLogin;
    private LocalDateTime DateRegistered;

    /*
     * For now as there is no verification technique is implemented , so
     * all the that fields are true
     *
     * */
    private boolean enabled = true;
    private boolean accountNonExpired = true;
    private boolean accountNonLocked = true;
    private boolean credentialsNonExpired = true;

    private boolean mfaEnable;

    @ManyToMany
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(
                    name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id"))

    private Collection<Role> roles;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<MultiFactorAuth> MfaFactorAuthMethods;

    /**
     * Formulates the full name of the user.
     *
     * @return the full name of the user
     */
    public String getName() {
        return StringUtils.joinWith(StringUtils.SPACE, getFirstName(), getMiddleName(), getLastName());
    }
}


