package dev.start.init.service.user;

import dev.start.init.dto.user.RoleDto;
import dev.start.init.entity.user.Role;
import java.util.List;

/**
 * Role service to provide implementation for the definitions about a role.
 *
 * @author Md Jewel
 * @version 1.0
 * @since 1.0
 */
public interface RoleService {


    List<RoleDto> findAll();

    /**
     * Create the role with the role instance given.
     *
     * @param role the role
     * @return the persisted role with assigned id
     */
    RoleDto save(final Role role);
    public RoleDto update(Integer roleId, Role roleEntity);

    void delete(Integer roleId);

    /**
     * Retrieves the role with the specified name.
     *
     * @param name the name of the role to retrieve
     * @return the role tuple that matches the id given
     */
    Role findByName(final String name);
}


