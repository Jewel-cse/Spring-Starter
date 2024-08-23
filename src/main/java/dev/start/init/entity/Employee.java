package dev.start.init.entity;

import dev.start.init.constants.EmployeeConstants;
import dev.start.init.constants.SequenceConstants;
import dev.start.init.entity.base.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
@Entity
@Table(name = "EMPLOYEE")
public class Employee extends BaseEntity<Long>  {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SequenceConstants.EMPLOYEE_SEQUENCE)
    @SequenceGenerator(name = SequenceConstants.EMPLOYEE_SEQUENCE,
            sequenceName = "employee_sequence",
            initialValue = SequenceConstants.EMPLOYEE_SEQUENCE_INITIAL_VALUE,
            allocationSize = SequenceConstants.EMPLOYEE_SEQUENCE_ALLOCATION)
    private Long id;

    @Column(unique = true, nullable = false)
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
    @Column(name = "EMP_SALARY", precision = 9, scale = 3)
    private BigDecimal empSalary;

    @Column(name = "EMP_STATUS")
    private boolean empStatus ;

    @NotNull(message = EmployeeConstants.NULL_JOINING_DATE)
    @PastOrPresent(message = EmployeeConstants.INVALID_JOINING_DATE)
    private LocalDate joiningDate;

    public Employee(String empCode, String empName, String empDesignation, BigDecimal empSalary, boolean empStatus, LocalDate joiningDate) {
        this.empCode = empCode;
        this.empName=empName;
        this.empDesignation = empDesignation;
        this.empSalary=empSalary;
        this.empStatus=empStatus;
        this.joiningDate=joiningDate;
    }

}
