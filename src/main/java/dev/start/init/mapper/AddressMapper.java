package dev.start.init.dto.mapper;

import dev.start.init.dto.AddressDto;
import dev.start.init.entity.Address;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * Mapper interface for converting between {@link Address} and {@link AddressDto}.
 * Uses MapStruct to generate the mapping implementation.
 *
 * @version 1.0
 * @since 1.0
 */
@Mapper(componentModel = "spring")
public interface AddressMapper {

    AddressMapper MAPPER = Mappers.getMapper(AddressMapper.class);

    /**
     * Converts AddressDto to Address entity.
     * @param dto The AddressDto to be converted.
     * @return The Address entity.
     */
    @Mapping(source = "filePath", target = "filePath")
    @Mapping(source = "fileType", target = "fileType")
    Address toEntity(AddressDto dto);

    /**
     * Converts Address entity to AddressDto.
     * @param entity The Address entity to be converted.
     * @return The AddressDto.
     */
    @Mapping(source = "filePath", target = "filePath")
    @Mapping(source = "fileType", target = "fileType")
    AddressDto toDto(Address entity);

    /**
     * Updates an existing Address entity from the AddressDto.
     * @param dto The AddressDto containing updated data.
     * @param entity The existing Address entity to be updated.
     */
    void updateEntityFromDto(AddressDto dto, @MappingTarget Address entity);

    /**
     * Converts a list of AddressDto to a list of Address entities.
     * @param dtoList The list of AddressDto to be converted.
     * @return The list of Address entities.
     */
    List<Address> toEntityList(List<AddressDto> dtoList);

    /**
     * Converts a list of Address entities to a list of AddressDto.
     * @param entityList The list of Address entities to be converted.
     * @return The list of AddressDto.
     */
    List<AddressDto> toDtoList(List<Address> entityList);
}

