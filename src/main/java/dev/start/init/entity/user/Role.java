package dev.start.init.entity.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dev.start.init.constants.SequenceConstants;
import dev.start.init.enums.RoleType;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * The role entity.
 *
 * @author Md Jewel
 * @version 1.0
 * @since 1.0
 */
@Entity
@Data
@NoArgsConstructor
public class Role implements Serializable {
    @Serial private static final long serialVersionUID = 7008351760784988067L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,  generator = SequenceConstants.ROLE_SEQUENCE)
    @SequenceGenerator(name = SequenceConstants.ROLE_SEQUENCE,  initialValue = SequenceConstants.ROLE_SEQUENCE_INITIAL_VALUE, allocationSize = SequenceConstants.ROLE_SEQUENCE_ALLOCATION)
    private Integer id;

    @Column(name = "NAME", nullable = false, unique = true)
    private String name;

    @ManyToMany(mappedBy = "roles",fetch = FetchType.LAZY)
    @JsonIgnore
    private Collection<User> users;

    @ManyToMany
    @JoinTable(
            name = "roles_privileges",
            joinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "privilege_id", referencedColumnName = "id"))
    private Collection<Privilege> privileges;

    /**
     * The Role class creates a role for the user.
     *
     * @param roleType assigns the role properties.
     */

    public Role(RoleType roleType) {
        this.id = roleType.getId();
        this.name = roleType.getName();
    }

    public Role(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
    public Role(String name) {
        this.name = name;
    }
}
