package dev.start.init.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private String empCode;

    private String empName;

    private String empDesignation;

    private BigDecimal empSalary;

    private boolean empStatus ;

    private LocalDate joiningDate;

    @ManyToOne(targetEntity = Company.class,fetch = FetchType.LAZY)
    @JoinColumn(name = "companyId",referencedColumnName = "id")
    private Company company;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_address_id",referencedColumnName = "id",unique = true)
    private Address address;
}
