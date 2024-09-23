package dev.start.init.dto.mapper;

import dev.start.init.dto.EmployeeDto;
import dev.start.init.entity.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
/**
 * Mapper interface for converting between {@link Employee} and {@link EmployeeDto}.
 *
 * <p>Uses MapStruct to generate the mapping implementation.</p>
 *
 * @Author Md Jewel Rana
 * @version 1.0
 * @since 1.0
 */
@Mapper(componentModel = "spring")
public interface EmployeeMapper {
    EmployeeMapper INSTANCE = Mappers.getMapper(EmployeeMapper.class);

    /**
     * Converts an Employee entity to an EmployeeDto.
     *
     * @param employee entity to convert.
     * @return the converted EmployeeDto.
     */
    @Mapping(source = "publicId", target = "publicId")
    EmployeeDto employeeToEmployeeDto(Employee employee);

    /**
     * Converts an EmployeeDto to an Employee entity.
     *
     * @param employeeDto the EmployeeDto object to convert.
     * @return the converted Employee entity.
     */
    @Mapping(source = "publicId", target = "publicId")
    Employee employeeDtoToEmployee(EmployeeDto employeeDto);

    /**
     * Updates an existing Employee entity with values from an EmployeeDto.
     *
     * @param dto the EmployeeDto object containing updated values.
     * @param entity the Employee entity to update.
     */
    void updateEntityFromDto(EmployeeDto dto, @MappingTarget Employee entity);
}

