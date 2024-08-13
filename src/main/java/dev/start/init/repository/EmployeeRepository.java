package dev.start.init.repository;

import dev.start.init.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * use @RepositoryRestResource(exported = false) for only that
 *
 * repository which is not available for http request
 *
 * if any repository marks by this that means all data should
 * persist in database, but we can not get json at http request
 *
 */
@Repository
public interface EmployeeRepository  extends JpaRepository<Employee, Long>, JpaSpecificationExecutor<Employee> {
}

