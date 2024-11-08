package dev.start.init.service;

import dev.start.init.annotation.Loggable;
import dev.start.init.dto.EmployeeDto;
import dev.start.init.entity.Company;
import dev.start.init.mapper.EmployeeMapper;
import dev.start.init.entity.Employee;
import dev.start.init.repository.EmployeeRepository;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Service class for managing {@link Employee} entities.
 *
 * <p>This class provides methods to:
 * <ul>
 *   <li>Create a new Employee</li>
 *   <li>Update an existing Employee</li>
 *   <li>Retrieve all Employees</li>
 *   <li>Retrieve a specific Employee by ID</li>
 *   <li>Delete an Employee</li>
 * </ul>
 * </p>
 *
 * @Author Md Jewel Rana
 * @version 1.0
 * @since 1.0
 */
@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository, EmployeeMapper employeeMapper) {
        this.employeeRepository = employeeRepository;
        this.employeeMapper = employeeMapper;
    }

    /**
     * Initialize  {@link Employee} entity with some records.
     *
     * @return the created table
     */

/*    //@PostConstruct
    @Loggable(level = "trace")
    public void initEmployeeTable(){
        List<Employee>  employees= IntStream.rangeClosed(1,50)
                .mapToObj(i->new Employee("empCode_"+i,"empName_"+i,"empDesig_"+i,new BigDecimal(Math.random()*100000),true,LocalDate.now().minusDays(i)))
                .collect(Collectors.toList());
        employeeRepository.saveAll(employees);
    }*/


    /**
     * Creates a new {@link Employee} entity from an {@link EmployeeDto}.
     *
     * @param employeeDto the DTO containing data for the new entity.
     * @return the created {@link EmployeeDto}.
     */
    @Transactional
    @Loggable(level = "warn")
    public EmployeeDto createEmployee(EmployeeDto employeeDto) {
        Employee employee = employeeMapper.employeeDtoToEmployee(employeeDto);
        return employeeMapper.employeeToEmployeeDto(employeeRepository.save(employee));
    }

    @Transactional
    @Loggable(level = "warn")
    public List<EmployeeDto> createEmployeesBatch(List<EmployeeDto> employeeDtos) {
        try{
            List<Employee> employees = employeeDtos.stream()
                    .map(employeeMapper::employeeDtoToEmployee)
                    .collect(Collectors.toList());
            List<Employee> savedEmployees = employeeRepository.saveAll(employees);
            return savedEmployees.stream()
                    .map(employeeMapper::employeeToEmployeeDto)
                    .collect(Collectors.toList());
        }catch (Exception e) {
            throw new RuntimeException("Failed to save employees", e);
        }
    }

    /**
     * Updates an existing {@link Employee} entity with data from an {@link EmployeeDto}.
     *
     * @param id the ID of the entity to update.
     * @param employeeDto the DTO containing updated data.
     * @return the updated {@link EmployeeDto}.
     * @throws EntityNotFoundException if no entity with the given ID exists.
     */
    @Transactional
    @Loggable(level = "critical")
    public EmployeeDto updateEmployee(Long id, EmployeeDto employeeDto) {
        Employee targetEmployee = employeeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with id " + id));

        employeeMapper.updateEntityFromDto(employeeDto, targetEmployee);
        return employeeMapper.employeeToEmployeeDto(employeeRepository.save(targetEmployee));
    }

    /**
     * Retrieves a pageable list of all employees using the provided Pageable object.
     *
     * @param specification the specification for querying employees.
     * @param pageable Pageable object specifying page number, size, and sorting parameters.
     * @return Page of {@link EmployeeDto} objects.
     */
    @Transactional(readOnly = true)
    @Loggable(level = "trace")
    public Page<EmployeeDto> getAllEmployees(Specification<Employee> specification, Pageable pageable) {
        Page<Employee> employeePage = employeeRepository.findAll(specification, pageable);

        return employeePage.map(employeeMapper::employeeToEmployeeDto);
    }

    /**
     * Retrieves an {@link Employee} entity by its ID.
     *
     * @param id the ID of the entity to retrieve.
     * @return the retrieved {@link EmployeeDto}.
     * @throws EntityNotFoundException if no entity with the given ID exists.
     */
    @Transactional(readOnly = true)
    @Loggable(level = "trace")
    public EmployeeDto getEmployeeById(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with id " + id));
        return employeeMapper.employeeToEmployeeDto(employee);
    }

    /**
     * Deletes an {@link Employee} entity by its ID.
     *
     * @param id the ID of the entity to delete.
     */
    @Transactional
    @Loggable(level = "critical")
    public void deleteEmployee(Long id) {
        if (!employeeRepository.existsById(id)) {
            throw new EntityNotFoundException("Employee not found with id " + id);
        }
        employeeRepository.deleteById(id);
    }
}

