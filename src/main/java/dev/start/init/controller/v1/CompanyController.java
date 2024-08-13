package dev.start.init.controller.v1;

import dev.start.init.constants.apiEndPoint.API_V1;
import dev.start.init.dto.CompanyDto;
import dev.start.init.entity.Company;
import dev.start.init.service.CompanyService;
import dev.start.init.util.specification.CompanySpecification;
import jakarta.persistence.EntityNotFoundException;
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

import java.time.LocalDate;
import java.util.List;

/**
 * REST controller for managing {@link dev.start.init.entity.Company} entities.
 *
 * <p>This controller provides endpoints to:
 * <ul>
 *   <li>Create a new Company</li>
 *   <li>Create multiple Companies in batch</li>
 *   <li>Retrieve a list of Companies with filtering options</li>
 * </ul>
 * </p>
 *
 * @Author Md Jewel Rana
 * @version 1.0
 * @since 1.0
 */
@RestController
@RequestMapping(API_V1.COMPANY_URL)
public class CompanyController {

    private final CompanyService companyService;
    @Autowired
    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    /**
     *  Create a new company.
     *
     * @param companyDto the DTO containing data for the new company.
     * @return the ResponseEntity with status 201 (Created) and with body the new companyDto, or with status 400 (Bad Request) if the company has already an ID.
     */
    @PostMapping
    public ResponseEntity<CompanyDto> createCompany(@RequestBody CompanyDto companyDto) {
        CompanyDto createdCompany = companyService.createCompany(companyDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCompany);
    }

    /**
     * Create multiple companies in a batch.
     *
     * @param companyDtos the list of DTOs containing data for the new companies.
     * @return the ResponseEntity with status 201 (Created) and with body the list of new companyDtos.
     */
    @PostMapping("/batch")
    public ResponseEntity<List<CompanyDto>> createCompaniesBatch(@RequestBody List<CompanyDto> companyDtos) {
        List<CompanyDto> createdCompanies = companyService.createCompaniesBatch(companyDtos);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCompanies);
    }

    /**
     * Get a list of companies with optional filters.
     * @param page            page number (default: 0)
     * @param size            number of items per page (default: 10)
     * @param sortBy          field to sort by (default: 'id')
     * @param orderBy         sort orderBy (default: 'asc')
     * @param comCode          optional company code filter
     * @param comName          optional company name filter
     * @param comEstablished   optional comEstablished filter
     * @return the ResponseEntity with the list of companies in body.
     */

    @GetMapping
    public ResponseEntity<Page<CompanyDto>> getAllCompanies(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String orderBy,
            @RequestParam(required = false) String comName,
            @RequestParam(required = false) String comCode,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate comEstablished
    ) {
        Pageable pageable = PageRequest.of(
                page,
                size,
                orderBy.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending()
        );

        Specification<Company> specification = Specification.where(CompanySpecification.hasName(comName))
                .and(CompanySpecification.hasCode(comCode))
                .and(CompanySpecification.establishedAfter(comEstablished));

        Page<CompanyDto> companies = companyService.getAllCompanies(specification, pageable);
        return ResponseEntity.ok(companies);
    }

    /**
     * Retrieves a {@link Company} entity by its ID.
     *
     * @param id the ID of the entity to retrieve.
     * @return the retrieved {@link Company}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CompanyDto> getCompanyById(@PathVariable Long id) {
        return ResponseEntity.ok(companyService.getCompanyById(id));
    }
    /**
     * Updates an existing {@link Company} entity.
     *
     * @param id the ID of the entity to update.
     * @param companyDto the DTO containing updated data.
     * @return the updated {@link Company}.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CompanyDto> updateCompany(@PathVariable Long id,@RequestBody CompanyDto companyDto) {
        CompanyDto createdCompany = companyService.updateCompany(id,companyDto);
        return ResponseEntity.ok(createdCompany);
    }

    /**
     * Deletes a {@link Company} entity.
     *
     * @param id the ID of the entity to delete.
     * @return a ResponseEntity indicating success or failure of the operation.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCompany(@PathVariable Long id) {
        try {
            companyService.deleteCompany(id);
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

