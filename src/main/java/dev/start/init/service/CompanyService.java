package dev.start.init.service;

import dev.start.init.dto.CompanyDto;
import dev.start.init.dto.mapper.CompanyMapper;
import dev.start.init.entity.Company;
import dev.start.init.repository.CompanyRepository;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


/**
 * Service class for managing {@link Company} entities.
 *
 * <p>This class provides methods to:
 * <ul>
 *   <li>Create a new Company</li>
 *   <li>Update an existing Company</li>
 *   <li>Retrieve all Companies with filtering</li>
 *   <li>Retrieve a specific Company by ID</li>
 *   <li>Delete a Company</li>
 * </ul>
 * </p>
 *
 * @Author Md Jewel Rana
 * @version 1.0
 * @since 1.0
 */
@Service
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final CompanyMapper companyMapper;

    @Autowired
    public CompanyService(CompanyRepository companyRepository, CompanyMapper companyMapper) {
        this.companyRepository = companyRepository;
        this.companyMapper = companyMapper;
    }

    /**
     * Initialize  {@link Company} entity with some values}.
     *
     * @return the created table
     */

    @PostConstruct
    public void initCompanyTable(){
        List<Company>  companies= IntStream.rangeClosed(1,50)
                .mapToObj(i->new Company("Com_name_"+i,"Com_code_"+i, LocalDate.now().plusDays(i)))
                .collect(Collectors.toList());
        companyRepository.saveAll(companies);
    }

    /**
     * Creates a new {@link Company} entity from a {@link CompanyDto}.
     *
     * @param companyDto the DTO containing data for the new entity.
     * @return the created {@link CompanyDto}.
     */
    @Transactional
    public CompanyDto createCompany(CompanyDto companyDto) {
        Company company = companyMapper.toCompany(companyDto);
        return companyMapper.toCompanyDto(companyRepository.save(company));
    }

    /**
     * Creates a list of new {@link Company} entities from a list of {@link CompanyDto} objects.
     *
     * @param companyDtos the list of DTOs containing data for the new entities.
     * @return the list of created {@link CompanyDto} objects.
     */
    @Transactional
    public List<CompanyDto> createCompaniesBatch(List<CompanyDto> companyDtos) {
        try {
            // Convert the list of CompanyDto to a list of Company entities
            List<Company> companies = companyDtos.stream()
                    .map(companyMapper::toCompany)
                    .collect(Collectors.toList());

            // Save all Company entities in a batch operation
            List<Company> savedCompanies = companyRepository.saveAll(companies);

            // Convert the saved Company entities back to a list of CompanyDto and return
            return savedCompanies.stream()
                    .map(companyMapper::toCompanyDto)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Failed to save companies", e);
        }
    }


    /**
     * Updates an existing {@link Company} entity with data from a {@link CompanyDto}.
     *
     * @param id the ID of the entity to update.
     * @param companyDto the DTO containing updated data.
     * @return the updated {@link CompanyDto}.
     * @throws EntityNotFoundException if no entity with the given ID exists.
     */
    @Transactional
    public CompanyDto updateCompany(Long id, CompanyDto companyDto) {
        Company targetCompany = companyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Company not found with id " + id));

        companyMapper.updateEntityFromDto(companyDto, targetCompany);
        return companyMapper.toCompanyDto(companyRepository.save(targetCompany));
    }

    /**
     * Retrieves a pageable list of all companies using the provided Pageable object and filtering by criteria.
     *
     * @param specification the specification for querying companies.
     * @param pageable Pageable object specifying page number, size, and sorting parameters.
     * @return Page of {@link CompanyDto} objects.
     */
    @Transactional(readOnly = true)
    public Page<CompanyDto> getAllCompanies(Specification<Company> specification, Pageable pageable) {
        Page<Company> companyPage = companyRepository.findAll(specification, pageable);

        return companyPage.map(companyMapper::toCompanyDto);
    }


    /**
     * Retrieves a {@link Company} entity by its ID.
     *
     * @param id the ID of the entity to retrieve.
     * @return the retrieved {@link CompanyDto}.
     * @throws EntityNotFoundException if no entity with the given ID exists.
     */
    @Transactional(readOnly = true)
    public CompanyDto getCompanyById(Long id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Company not found with id " + id));
        return companyMapper.toCompanyDto(company);
    }

    /**
     * Deletes a {@link Company} entity by its ID.
     *
     * @param id the ID of the entity to delete.
     */
    @Transactional
    public void deleteCompany(Long id) {
        if (!companyRepository.existsById(id)) {
            throw new EntityNotFoundException("Company not found with id " + id);
        }
        companyRepository.deleteById(id);
    }
}

