package dev.start.init.service.user.impl;

import dev.start.init.constants.user.RoleConstants;
import dev.start.init.entity.user.Privilege;
import dev.start.init.entity.user.Role;
import dev.start.init.repository.user.PrivilegeRepository;
import dev.start.init.repository.user.RoleRepository;
import dev.start.init.service.user.PrivilegeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
@Service
@RequiredArgsConstructor
public class PrivilegeServiceImpl implements PrivilegeService {
    private final PrivilegeRepository privilegeRepository;
    private final RoleRepository roleRepository;

    @Override
    public List<Privilege> getActivePrivilegeByRoleId(Integer roleId) {
        Role role = roleRepository.findById(roleId).get();
        if (role == null) {
            throw new RuntimeException("Role is not Found");
        }
        return privilegeRepository.findPrivilegesByRoles(role);
    }

    @Override
    public Privilege updatePrivilege(Integer roleId, List<Privilege> privilegeList) {
        Role role = roleRepository.findById(roleId).get();
        if (role == null) {
            throw new RuntimeException("Role is not Found");
        }
        //here will be added the logic of privilege update
        return null;
    }


}
