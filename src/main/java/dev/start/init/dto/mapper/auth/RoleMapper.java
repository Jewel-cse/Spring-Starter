package dev.start.init.dto.mapper.auth;


import dev.start.init.dto.auth.RoleDto;
import dev.start.init.entity.auth.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {PermissionMapper.class})
public interface RoleMapper {

    RoleMapper INSTANCE = Mappers.getMapper(RoleMapper.class);

    @Mapping(source = "permissions", target = "permissions")
    RoleDto toDto(Role role);

    @Mapping(source = "permissions", target = "permissions")
    Role toEntity(RoleDto roleDto);
}

