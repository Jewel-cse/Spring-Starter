package dev.start.init.dto.auth;

import lombok.Data;
import java.io.Serializable;
import java.util.Set;

/**
 * DTO for Role entity.
 */
@Data
public class RoleDto implements Serializable {
    private Long id;
    private String name;
    private String description;
    private Set<PermissionDto> permissions;
}

