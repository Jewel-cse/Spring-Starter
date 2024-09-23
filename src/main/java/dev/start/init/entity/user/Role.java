package dev.start.init.entity.auth;


import dev.start.init.constants.SequenceConstants;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;

import dev.start.init.entity.base.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Set;

@Data
@Entity
@Table(name = "ROLE")
@EqualsAndHashCode(callSuper = true)
public class Role extends BaseEntity<Long> implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SequenceConstants.ROLE_SEQUENCE)
    @SequenceGenerator(name = SequenceConstants.ROLE_SEQUENCE,
            sequenceName = "RoleSequence",
            initialValue = SequenceConstants.ROLE_SEQUENCE_INITIAL_VALUE,
            allocationSize = SequenceConstants.ROLE_SEQUENCE_ALLOCATION)
    private Long id;

    @Size(max = 100)
    @NotBlank(message = "Role name is required")
    @Column(name = "NAME", unique = true, nullable = false)
    private String name;

    @Size(max = 255)
    @Column(name = "DESCRIPTION")
    private String description;

    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    private Set<User> users;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "ROLE_PERMISSION",
            joinColumns = @JoinColumn(name = "ROLE_ID"),
            inverseJoinColumns = @JoinColumn(name = "PERMISSION_ID")
    )
    private Set<Permission> permissions;
}
