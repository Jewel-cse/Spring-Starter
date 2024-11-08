package dev.start.init.service.user.impl;


import dev.start.init.dto.user.RoleDto;
import dev.start.init.entity.user.Role;
import dev.start.init.repository.user.RoleRepository;
import dev.start.init.service.user.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;


/**
 * The RoleServiceImpl class is an implementation for the RoleService Interface.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public List<RoleDto> findAll() {
        List<Role> roles = roleRepository.findAll();

        return roles.stream()
                .map(role -> new RoleDto(role.getId(), role.getName()))
                .collect(Collectors.toList());
    }


    /**
     * Create the roleEntity with the roleEntity instance given.
     *
     * @param roleEntity the roleEntity
     * @return the persisted roleEntity with assigned id
     */
    @Override
    @Transactional
    public RoleDto save(Role roleEntity) {
        Validate.notNull(roleEntity, "The roleEntity cannot be null");
        Role role = roleRepository.findByName(roleEntity.getName());
        if (role != null) return new RoleDto(role.getId(), role.getName());
        //Validate.notNull(roleRepository.findByName(roleEntity.getName()), "The Role already exist");


        roleEntity.setName(roleEntity.getName());
        Role persistedRole = roleRepository.save(roleEntity);
        LOG.info("Role merged successfully {}", persistedRole);
        return new RoleDto(persistedRole.getId(), persistedRole.getName());
    }

    /**
     * Update the roleEntity with the roleEntity instance given.
     *
     * @param roleEntity the roleEntity to update
     * @return the updated roleEntity
     */
    @Override
    @Transactional
    public RoleDto update(Integer roleId,Role roleEntity) {
        Validate.notNull(roleEntity, "The roleEntity cannot be null");
        Validate.notNull(roleId, "The role ID cannot be null");

        // Ensure the role exists before updating
        Role existingRole = roleRepository.findById(roleId)
                .orElseThrow(() -> new IllegalArgumentException("Role not found with id: " + roleEntity.getId()));

        // Update the role name with the prefix
        //String roleNameWithPrefix = UserUtils.addRolePrefix(roleEntity.getName());
        existingRole.setName(roleEntity.getName());

        // Save the updated role
        Role updatedRole = roleRepository.save(existingRole);
        LOG.info("Role updated successfully {}", updatedRole);

        // Remove prefix for returned object
        updatedRole.setName(updatedRole.getName());
        return new RoleDto(updatedRole.getId(), updatedRole.getName());
    }


    @Override
    public void delete(Integer roleId) {
        roleRepository.deleteById(roleId);
    }

    /**
     * Retrieves the role with the specified name.
     *
     * @param name the name of the role to retrieve
     * @return the role tuple that matches the id given
     */
    @Override
    //@Cacheable(CacheConstants.ROLES)
    public Role findByName(String name) {
        Validate.notNull(name, "The name cannot be null");

        //String roleNameWithPrefix = UserUtils.addRolePrefix(name);
        Role role = roleRepository.findByName(name);
        if (role != null) {
            role.setName(role.getName());
        }
        return role;
    }


}


