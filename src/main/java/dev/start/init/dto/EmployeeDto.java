package dev.start.init.dto;

import dev.start.init.constants.EmployeeConstants;
import jakarta.validation.constraints.*;
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
@EqualsAndHashCode(callSuper = false)
public class EmployeeDto extends BaseDto{

    private Long id;

    private String publicId;

    @NotBlank(message = EmployeeConstants.BLANK_EMP_CODE)
    @Size(max = 20)
    private String empCode;

    @NotBlank(message = EmployeeConstants.BLANK_EMP_NAME)
    @Size(max = 100)
    private String empName;

    @Size(max = 50, message = EmployeeConstants.BLANK_EMP_DESIG)
    private String empDesignation;

    @DecimalMin(value = "0.000", inclusive = true, message = EmployeeConstants.INVALID_SALARY)
    @DecimalMax(value = "999999.999", inclusive = true, message = EmployeeConstants.INVALID_SALARY)

    private BigDecimal empSalary;

    private boolean empStatus ;

    @NotNull(message = EmployeeConstants.NULL_JOINING_DATE)
    @PastOrPresent(message = EmployeeConstants.INVALID_JOINING_DATE)
    private LocalDate joiningDate;

    @NotNull(message = EmployeeConstants.NULL_COMPANY_ID)
    private CompanyDto company;

}

