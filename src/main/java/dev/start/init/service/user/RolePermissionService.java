package dev.start.init.service.user;

import dev.start.init.dto.user.PermissionDto;
import dev.start.init.entity.user.RolePermission;

import java.util.List;
import java.util.Map;

public interface RolePermissionService {

    Map<String, Map<String, PermissionDto>> getActivePermissionsByRoleId(Long roleId);
    void updateRolePermissions(Long roleId, List<RolePermission> rolePermissionDtos);
    Map<String, Map<String, PermissionDto>> getPermissionsByUserId(String publicId);

}

