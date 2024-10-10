package dev.start.init.controller.v1.user;

import dev.start.init.constants.apiEndPoint.API_V1;
import dev.start.init.entity.user.Privilege;
import dev.start.init.service.user.PrivilegeService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(API_V1.ROLE_PERMISSION_URL)
public class PrivilegeController {

    private final PrivilegeService privilegeService;

    @GetMapping("/{roleId}")
    public ResponseEntity<List<Privilege>> getPermissionsByRoleId(@PathVariable Integer roleId) {
        List<Privilege> permissions = privilegeService.getActivePrivilegeByRoleId(roleId);
        return ResponseEntity.ok(permissions);
    }

    @PutMapping("/{roleId}")
    public ResponseEntity<Void> updateRolePermissions(@PathVariable Integer roleId, @RequestBody List<Privilege> updatedPermissions) {
        //privilegeService.updateRolePermissions(roleId, updatedPermissions);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/users/{publicId}")
    public ResponseEntity<List<Privilege>> getMergedPermissionsByUserId(@PathVariable String publicId) {
        //Map<String, Map<String, Privilege>> permissions = privilegeService.getPrivilegeByUserId(publicId);
        return ResponseEntity.ok(null);
    }

}


