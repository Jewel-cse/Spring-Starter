package dev.start.init.dto.mapper.auth;

import dev.start.init.dto.auth.PermissionDto;
import dev.start.init.entity.auth.Permission;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface PermissionMapper {

    //PermissionMapper INSTANCE = Mappers.getMapper(PermissionMapper.class);

    PermissionDto toDto(Permission permission);

    Permission toEntity(PermissionDto permissionDto);
}

