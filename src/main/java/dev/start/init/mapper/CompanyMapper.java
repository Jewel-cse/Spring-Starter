package dev.start.init.mapper;

import dev.start.init.dto.CompanyDto;
import dev.start.init.entity.Company;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * Mapper interface for converting between {@link Company} and {@link CompanyDto}.
 *
 * <p>Uses MapStruct to generate the mapping implementation.</p>
 *
 * @Author Md Jewel Rana
 * @version 1.0
 * @since 1.0
 */
@Mapper(componentModel = "spring")
public interface CompanyMapper {
    /**
     * Converts a Company entity to an CompanyDto.
     *
     * @param Company entity to convert.
     * @return the converted CompanyDto.
     */
    @Mapping(source = "publicId", target = "publicId")
    CompanyDto toCompanyDto(Company Company);

    /**
     * Converts an CompanyDto to a Company entity.
     *
     * @param companyDto the CompanyDto object to convert.
     * @return the converted Company entity.
     */
    @Mapping(source = "publicId", target = "publicId")
    Company toCompany(CompanyDto companyDto);

    /**
     * Updates an existing Company entity with values from an CompanyDto.
     *
     * @param dto the CompanyDto object containing updated values.
     * @param entity the Company entity to update.
     */
    void updateEntityFromDto(CompanyDto dto, @MappingTarget Company entity);
}
