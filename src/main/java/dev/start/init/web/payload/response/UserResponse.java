package dev.start.init.web.payload.response;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

import dev.start.init.dto.user.RoleDto;
import lombok.Data;

/**
 * This class models the format of the response produced in the controller endpoints.
 *
 * @author Md Jewel
 * @version 1.0
 * @since 1.0
 */
@Data
public class UserResponse implements Serializable {
    @Serial private static final long serialVersionUID = -8632756128923682589L;

    private String publicId;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private boolean enabled;
    private List<String> userRoles;
}

