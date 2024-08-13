package dev.start.init.controller.v1;

import dev.start.init.constants.apiEndPoint.API_V1;
import dev.start.init.dto.EmployeeDto;
import dev.start.init.entity.Employee;
import dev.start.init.service.EmployeeService;
import dev.start.init.util.specification.EmployeeSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;


/**
 * REST controller for managing {@link Employee} entities.
 *
 * Service for managing Employee entities.
 *
 *
 * @Author Md Jewel Rana
 * @version 1.0
 * @since 1.0
 */
@RestController
@RequestMapping(API_V1.EMPLOYEE_URL)
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    /**
     * Creates a new {@link Employee} entity.
     *
     * @param employeeDto the DTO containing data for the new entity.
     * @return the created {@link Employee}.
     */
    @PostMapping
    public ResponseEntity<EmployeeDto> createEmployee(@RequestBody @Valid EmployeeDto employeeDto) {
        return ResponseEntity.ok(employeeService.createEmployee(employeeDto));
    }

    @PostMapping("/batch")
    public ResponseEntity<List<EmployeeDto>> createEmployees(@RequestBody List<EmployeeDto> employeeDtos) {
        try {
            List<EmployeeDto> createdEmployees = employeeService.createEmployeesBatch(employeeDtos);
            return ResponseEntity.ok(createdEmployees);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    /**
     * Updates an existing {@link Employee} entity.
     *
     * @param id the ID of the entity to update.
     * @param employeeDto the DTO containing updated data.
     * @return the updated {@link Employee}.
     */
    @PutMapping("/{id}")
    public ResponseEntity<EmployeeDto> updateEmployee(@PathVariable Long id, @RequestBody @Valid EmployeeDto employeeDto) {
        return ResponseEntity.ok(employeeService.updateEmployee(id, employeeDto));
    }

    /**
     * Retrieves a paginated list of employees with optional filters.
     *
     * @param page          page number (default: 0)
     * @param size          number of items per page (default: 10)
     * @param sortBy        field to sort by (default: 'id')
     * @param orderBy       sort orderBy (default: 'asc')
     * @param empCode       optional employee code filter
     * @param empName       optional employee name filter
     * @param empDesignation      optional designation filter
     * @param empStatus     optional status filter
     * @param joiningDate   optional joining date filter
     * @return paginated list of Employee DTOs.
     */
    @GetMapping
    public ResponseEntity<Page<EmployeeDto>> getAllEmployees(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String orderBy,
            @RequestParam(required = false) String empCode,
            @RequestParam(required = false) String empName,
            @RequestParam(required = false) String empDesignation,
            @RequestParam(required = false) BigDecimal minSalary,
            @RequestParam(required = false) BigDecimal maxSalary,
            @RequestParam(required = false) Boolean empStatus,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate joiningDate
    ) {
        Pageable pageable = PageRequest.of(
                page,
                size,
                orderBy.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending()
        );


        Specification<Employee> specification = Specification.where(EmployeeSpecification.hasEmpCode(empCode))
                .and(EmployeeSpecification.hasEmpName(empName))
                .and(EmployeeSpecification.hasEmpDesignation(empDesignation))
                .and(EmployeeSpecification.hasSalaryRange(minSalary, maxSalary))
                .and(EmployeeSpecification.hasEmpStatus(empStatus))
                .and(EmployeeSpecification.hasJoiningDate(joiningDate));

        Page<EmployeeDto> employees = employeeService.getAllEmployees(specification, pageable);
        return ResponseEntity.ok(employees);
    }

    /**
     * Retrieves a {@link Employee} entity by its ID.
     *
     * @param id the ID of the entity to retrieve.
     * @return the retrieved {@link Employee}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDto> getEmployeeById(@PathVariable Long id) {
        return ResponseEntity.ok(employeeService.getEmployeeById(id));
    }

    /**
     * Deletes a {@link Employee} entity.
     *
     * @param id the ID of the entity to delete.
     * @return a ResponseEntity indicating success or failure of the operation.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        try {
            employeeService.deleteEmployee(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}


