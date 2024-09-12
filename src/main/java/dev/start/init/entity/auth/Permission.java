package dev.start.init.entity.auth;

import dev.start.init.constants.SequenceConstants;
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
@Table(name = "PERMISSION")
@EqualsAndHashCode(callSuper = true)
public class Permission extends BaseEntity<Long> implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SequenceConstants.PERMISSION_SEQUENCE)
    @SequenceGenerator(name = SequenceConstants.PERMISSION_SEQUENCE,
            sequenceName = "PermissionSequence",
            initialValue = SequenceConstants.PERMISSION_SEQUENCE_INITIAL_VALUE,
            allocationSize = SequenceConstants.PERMISSION_SEQUENCE_ALLOCATION)
    private Long id;

    @Size(max = 100)
    @NotBlank(message = "Permission name is required")
    @Column(name = "NAME", unique = true, nullable = false)
    private String name;

    @Size(max = 255)
    @Column(name = "DESCRIPTION")
    private String description;

    @ManyToMany(mappedBy = "permissions", fetch = FetchType.LAZY)
    private Set<Role> roles;
}



