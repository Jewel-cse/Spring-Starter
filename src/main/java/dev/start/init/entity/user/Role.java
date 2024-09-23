package dev.start.init.entity.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dev.start.init.constants.SequenceConstants;
import dev.start.init.enums.RoleType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.SequenceGenerator;
import lombok.Data;
import jakarta.persistence.Cacheable;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.io.Serial;
import java.io.Serializable;
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

    @ManyToMany(mappedBy = "userRoles",fetch = FetchType.LAZY)
    @JsonIgnore
    @ToString.Exclude
    private Set<User> users;

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

    /**
     * Evaluate the equality of Role class.
     *
     * @param other is the object to use in equality test.
     * @return the equality of both objects.
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Role that)) {
            return false;
        }
        return Objects.equals(name, that.name);
    }

    /**
     * Hashcode of Role base on name.
     *
     * @return the hash value.
     */
    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
