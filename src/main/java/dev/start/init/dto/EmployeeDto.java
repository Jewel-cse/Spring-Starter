package dev.start.init.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Data Transfer Object for Employee details.
 *
 * <p>Transfers employee data between different layers of the application.</p>
 *
 * @author Md Jewel Rana
 * @version 1.0
 * @since 1.0
 */
@Data
@EqualsAndHashCode()
public class EmployeeDto {

//    private Long id;
    private String publicId;

    private String empCode;

    private String empName;

    private String empDesignation;

    private BigDecimal empSalary;

    private boolean empStatus ;

    private LocalDate joiningDate;

}

