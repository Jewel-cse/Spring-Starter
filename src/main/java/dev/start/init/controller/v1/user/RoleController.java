package dev.start.init.controller.v1.user;


import dev.start.init.constants.SecurityConstants;
import dev.start.init.dto.user.RoleDto;
import dev.start.init.entity.user.Role;
import dev.start.init.service.user.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(SecurityConstants.API_ROLE)
public class RoleController {

    private final RoleService roleService;

    @PostMapping
    public ResponseEntity<RoleDto> createRole(@RequestBody Role role) {
        RoleDto savedRole = roleService.save(role);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedRole);

    }

    @GetMapping
    public ResponseEntity<List<RoleDto>> findAllRole() {
        return ResponseEntity.ok(roleService.findAll());
    }


    @PutMapping("/{roleId}")
    public ResponseEntity<RoleDto> updateRole(@PathVariable Integer roleId,@RequestBody Role role) {
        return ResponseEntity.ok(roleService.update(roleId, role));
    }

    @DeleteMapping("/{roleId}")
    public ResponseEntity<Void> deleteRole(@PathVariable Integer roleId) {
        roleService.delete(roleId);
        return ResponseEntity.noContent().build();
    }

}

