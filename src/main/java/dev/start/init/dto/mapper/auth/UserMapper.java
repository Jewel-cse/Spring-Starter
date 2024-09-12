package dev.start.init.dto.mapper.auth;


import dev.start.init.dto.auth.SignupRequestDto;
import dev.start.init.dto.auth.UserDto;
import dev.start.init.entity.auth.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {

    //UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    //@Mapping(source = "publicId", target = "publicId")
    UserDto toDto(User user);

    //@Mapping(source = "publicId", target = "publicId")
    User toEntity(UserDto userDto);

    //@Mapping(target = "enabled", constant = "false")
    User toEntity(SignupRequestDto signupRequestDto);
}

