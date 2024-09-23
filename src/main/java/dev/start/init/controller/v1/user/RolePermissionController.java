package dev.start.init.controller.v1.user;

import dev.start.init.constants.apiEndPoint.API_V1;
import dev.start.init.dto.user.PermissionDto;
import dev.start.init.entity.user.RolePermission;
import dev.start.init.service.user.RolePermissionService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping(API_V1.ROLE_PERMISSION_URL)
public class RolePermissionController {

    private final RolePermissionService rolePermissionService;

    @GetMapping("/{roleId}")
    public ResponseEntity<Map<String, Map<String, PermissionDto>>> getPermissionsByRoleId(@PathVariable Long roleId) {
        Map<String, Map<String, PermissionDto>> permissions = rolePermissionService.getActivePermissionsByRoleId(roleId);
        return ResponseEntity.ok(permissions);
    }

    @PutMapping("/{roleId}")
    public ResponseEntity<Void> updateRolePermissions(@PathVariable Long roleId, @RequestBody List<RolePermission> updatedPermissions) {
        rolePermissionService.updateRolePermissions(roleId, updatedPermissions);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/users/{publicId}")
    public ResponseEntity<Map<String, Map<String, PermissionDto>>> getMergedPermissionsByUserId(@PathVariable String publicId) {
        Map<String, Map<String, PermissionDto>> permissions = rolePermissionService.getPermissionsByUserId(publicId);
        return ResponseEntity.ok(permissions);
    }

}


