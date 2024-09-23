package dev.start.init.service.user.impl;

import dev.start.init.dto.user.PermissionDto;
import dev.start.init.entity.user.RolePermission;
import dev.start.init.repository.user.RolePermissionRepository;
import dev.start.init.service.user.RolePermissionService;
import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Service
public class RolePermissionServiceImpl implements
        RolePermissionService {

    private final RolePermissionRepository rolePermissionRepository;

    public Map<String, Map<String, PermissionDto>> getActivePermissionsByRoleId(Long roleId) {
        List<RolePermission> rolePermissions = rolePermissionRepository.findByRoleIdAndIsActive(roleId, 1);
        Map<String, Map<String, PermissionDto>> permissions = new HashMap<>();

        for (RolePermission rp : rolePermissions) {
            permissions
                    .computeIfAbsent(rp.getModuleId(), k -> new HashMap<>())
                    .put(rp.getDocumentId(), new PermissionDto(rp.getCanCreate(), rp.getCanRead(), rp.getCanUpdate(), rp.getCanDelete()));
        }

        return permissions;
    }

    public void updateRolePermissions(Long roleId, List<RolePermission> updatedPermissions) {
        List<RolePermission> existingPermissions = rolePermissionRepository.findByRoleId(roleId);

        // Deactivate all existing permissions for the role
        for (RolePermission rp : existingPermissions) {
            rp.setActive(false);
        }
        rolePermissionRepository.saveAll(existingPermissions);

        // Update or create new permissions
        for (RolePermission updatedPermission : updatedPermissions) {
            RolePermission existingPermission = rolePermissionRepository.findByRoleIdAndModuleIdAndDocumentId(roleId, updatedPermission.getModuleId(), updatedPermission.getDocumentId());
            if (existingPermission != null) {
                existingPermission.setCanCreate(updatedPermission.getCanCreate());
                existingPermission.setCanRead(updatedPermission.getCanRead());
                existingPermission.setCanUpdate(updatedPermission.getCanUpdate());
                existingPermission.setCanDelete(updatedPermission.getCanDelete());
                existingPermission.setActive(true);
                rolePermissionRepository.save(existingPermission);
            } else {
                updatedPermission.setRoleId(roleId);
                updatedPermission.setActive(true);
                rolePermissionRepository.save(updatedPermission);
            }
        }
    }

    @Override
    public Map<String, Map<String, PermissionDto>> getPermissionsByUserId(String publicId) {
        List<RolePermission> rolePermissions = rolePermissionRepository.findByUserIdAndIsActive(publicId, 1);
        Map<String, Map<String, PermissionDto>> permissions = new HashMap<>();

        for (RolePermission rp : rolePermissions) {
            permissions
                    .computeIfAbsent(rp.getModuleId(), k -> new HashMap<>())
                    .merge(rp.getDocumentId(),
                            new PermissionDto(rp.getCanCreate() == 1, rp.getCanRead() == 1, rp.getCanUpdate() == 1, rp.getCanDelete() == 1),
                            (existing, newPerm) -> new PermissionDto(
                                    existing.isCanCreate() || newPerm.isCanCreate(),
                                    existing.isCanRead() || newPerm.isCanRead(),
                                    existing.isCanUpdate() || newPerm.isCanUpdate(),
                                    existing.isCanDelete() || newPerm.isCanDelete()
                            )
                    );
        }

        return permissions;
    }
}

