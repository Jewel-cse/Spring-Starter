package dev.start.init.dto.user;

import lombok.Data;

import java.util.Set;

@Data
public class UserRoleDto {
    private UserDto user;
    private Set<RoleDto> roles;
}

