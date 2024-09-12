package dev.start.init.service.auth;

import dev.start.init.entity.auth.Role;

public interface RoleService {

    Role findByName(String name);
}

