package dev.start.init.mapper;

import dev.start.init.dto.EmployeeDto;
import dev.start.init.dto.user.UserDto;
import dev.start.init.entity.Employee;
import dev.start.init.entity.user.User;
import dev.start.init.service.impl.UserDetailsBuilder;
import dev.start.init.util.UserUtils;
import dev.start.init.web.payload.request.SignUpRequest;
import dev.start.init.web.payload.response.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import java.util.List;

/**
 * The UserDtoMapper class outlines the supported conversions between User entity and other data
 * transfer objects.
 *
 * @author Md Jewel
 * @version 1.0
 * @since 1.0
 */
@Mapper(
        uses = {UserHistoryMapper.class},
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        imports = {UserUtils.class})
public interface UserMapper {

    /** The Dto mapper instance. */
    UserMapper MAPPER = Mappers.getMapper(UserMapper.class);

    /**
     * Convert and populate a user to userDto object.
     *
     * @param user the user
     * @return the userDto
     */
    //@Mapping(target = "userRoles", expression = "java(UserUtils.getTopmostRole(user))")
    //@Mapping(target = "profileImage", expression = "java(UserUtils.getUserProfileImage(user))")
    @Mapping(target = "userHistories", ignore = true)
    UserDto toUserDto(User user);


    /**
     * Convert and populate a signUpRequest to userDto object.
     *
     * @param signUpRequest the signup request
     * @return the userDto
     */
    UserDto toUserDto(SignUpRequest signUpRequest);

    /**
     * Convert and populate users to list of userDto objects.
     *
     * @param users the users
     * @return the list of userDto
     */
    List<UserDto> toUserDto(List<User> users);

    /**
     * Convert and populate a userDto to User object.
     *
     * @param userDetailsBuilder the userDetailsBuilder
     * @return the user
     */
    UserDto toUserDto(UserDetailsBuilder userDetailsBuilder);

    /**
     * Convert and populate a userDto to User object.
     *
     * @param userDto the userDto
     * @return the user
     */
    @Mapping(target = "profileImage", expression = "java(UserUtils.getUserProfileImage(user))")
    User toUser(UserDto userDto);


    /**
     * Updates an existing User entity with values from an UserDto.
     *
     * @param userDto the EmployeeDto object containing updated values.
     * @param user the Employee entity to update.
     */
    void updateUserFromUserDto(UserDto userDto, @MappingTarget User user);


    /**
     * Convert and populate a User to UserResponse object.
     *
     * @param user the user
     * @return the userResponse
     */
    //UserResponse toUserResponse(User user);
}

