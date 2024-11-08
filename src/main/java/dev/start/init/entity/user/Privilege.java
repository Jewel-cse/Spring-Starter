package dev.start.init.entity.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dev.start.init.constants.SequenceConstants;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Privilege  {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SequenceConstants.PRIVILEGE_SEQUENCE)
    @SequenceGenerator(name = SequenceConstants.PRIVILEGE_SEQUENCE, initialValue = SequenceConstants.ROLE_PERMISSION_SEQUENCE_INITIAL_VALUE, allocationSize = SequenceConstants.PRIVILEGE_SEQUENCE_ALLOCATION)
    private Long id;
    /*
    * Privilege names means Read, write,edit and delete operation
    *
    * */
    @Column(nullable = false,unique = true)
    private String name;

    @ManyToMany(mappedBy = "privileges")
    @JsonIgnore
    private Collection<Role> roles;

    public Privilege(final String name) {
        this.name = name;
    }
}
