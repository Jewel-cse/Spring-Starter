package dev.start.init.service.auth;

import dev.start.init.entity.user.Role;

public interface RoleService {

    Role findByName(String name);
}

