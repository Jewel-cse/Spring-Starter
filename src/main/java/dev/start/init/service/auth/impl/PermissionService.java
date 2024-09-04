package dev.start.init.service.auth;

import dev.start.init.dto.auth.PermissionDto;
import dev.start.init.entity.auth.Permission;
import dev.start.init.dto.mapper.auth.PermissionMapper;
import dev.start.init.repository.auth.PermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PermissionService {

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private PermissionMapper permissionMapper;

    public List<PermissionDto> findAllPermissions() {
        return permissionRepository.findAll()
                .stream()
                .map(permissionMapper::toDto)
                .collect(Collectors.toList());
    }

    public PermissionDto createPermission(PermissionDto permissionDto) {
        Permission permission = permissionMapper.toEntity(permissionDto);
        Permission savedPermission = permissionRepository.save(permission);
        return permissionMapper.toDto(savedPermission);
    }
}

