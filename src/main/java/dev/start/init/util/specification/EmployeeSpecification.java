package dev.start.init.util.specification;

import dev.start.init.entity.Employee;
import org.springframework.data.jpa.domain.Specification;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.math.BigDecimal;

/**
 * Utility class for creating JPA Specifications for filtering {@link Employee} entities.
 *
 * <p>This class provides static methods to build {@link Specification} instances for various filtering criteria.</p>
 */
public class EmployeeSpecification {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_DATE;

    /**
     * Creates a Specification for filtering by employee code.
     *
     * @param empCode the employee code to filter by.
     * @return a Specification for filtering by employee code.
     */
    public static Specification<Employee> hasEmpCode(String empCode) {
        return (root, query, builder) ->
                empCode == null ? null : builder.equal(root.get("empCode"), empCode);
    }

    /**
     * Creates a Specification for filtering by employee name (case-insensitive) and partial matching.
     *
     * @param empName the employee name to filter by.
     * @return a Specification for filtering by employee name.
     */
    public static Specification<Employee> hasEmpName(String empName) {
        return (root, query, builder) ->
                empName == null ? null : builder.like(builder.lower(root.get("empName")), "%" + empName.toLowerCase() + "%");
    }

    /**
     * Creates a Specification for filtering by employee designation.
     *
     * @param empDesignation the employee designation to filter by.
     * @return a Specification for filtering by employee designation.
     */
    public static Specification<Employee> hasEmpDesignation(String empDesignation) {
        return (root, query, builder) ->
                empDesignation == null ? null : builder.equal(root.get("empDesignation"), empDesignation);
    }

    /**
     * Creates a Specification for filtering by employee salary range.
     *
     * @param minSalary the minimum salary to filter by.
     * @param maxSalary the maximum salary to filter by.
     * @return a Specification for filtering by salary range.
     */
    public static Specification<Employee> hasSalaryRange(BigDecimal minSalary, BigDecimal maxSalary) {
        return (root, query, builder) -> {
            if (minSalary == null && maxSalary == null) return null;
            if (minSalary != null && maxSalary != null) {
                return builder.between(root.get("empSalary"), minSalary, maxSalary);
            }
            if (minSalary != null) {
                return builder.greaterThanOrEqualTo(root.get("empSalary"), minSalary);
            }
            return builder.lessThanOrEqualTo(root.get("empSalary"), maxSalary);
        };
    }

    /**
     * Creates a Specification for filtering by employee status.
     *
     * @param empStatus the employee status to filter by.
     * @return a Specification for filtering by employee status.
     */
    public static Specification<Employee> hasEmpStatus(Boolean empStatus) {
        return (root, query, builder) ->
                empStatus == null ? null : builder.equal(root.get("empStatus"), empStatus);
    }

    /**
     * Creates a Specification for filtering by joining date.
     *
     * @param joiningDate the joining date to filter by in ISO format (e.g., "2024-08-09").
     * @return a Specification for filtering by joining date.
     */
    public static Specification<Employee> hasJoiningDate(LocalDate joiningDate) {
        return (root, query, builder) ->
            joiningDate ==null?null: builder.equal(root.get("joiningDate"), joiningDate);

    }

}

