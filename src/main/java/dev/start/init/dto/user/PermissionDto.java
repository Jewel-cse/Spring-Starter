package dev.start.init.dto.auth;

import lombok.Data;
import java.io.Serializable;

/**
 * DTO for Permission entity.
 */
@Data
public class PermissionDto implements Serializable {
    private Long id;
    private String name;
    private String description;
}

