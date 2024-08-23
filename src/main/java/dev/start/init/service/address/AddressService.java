package dev.start.init.service.address;

import dev.start.init.dto.AddressDto;
import dev.start.init.entity.Address;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.multipart.MultipartFile;

/**
 * Service interface for managing addresses.
 */
public interface AddressService {

    /**
     * Retrieves all Address entities based on the provided specification and pageable.
     *
     * @param specification the criteria for filtering the entities
     * @param pageable      the pagination information
     * @return a page of AddressDto objects
     */
    Page<AddressDto> getAllAddresses(Specification<Address> specification, Pageable pageable);

    /**
     * Retrieves an Address entity by its ID.
     *
     * @param id the ID of the entity to retrieve
     * @return the found AddressDto
     */
    AddressDto getAddressById(Long id);

    /**
     * Creates a new Address entity based on the provided DTO.
     *
     * @param addressDto the DTO containing the data for the new entity
     * @return the created AddressDto
     */
    AddressDto createAddress(AddressDto addressDto,MultipartFile file);

    /**
     * Updates an existing Address entity with data from the provided DTO.
     *
     * @param id         the ID of the entity to update
     * @param addressDto the DTO containing the updated data
     * @param file       the MultipartFile containing the updated file
     * @return the updated AddressDto
     */
    AddressDto updateAddress(Long id, AddressDto addressDto, MultipartFile file);

    /**
     * Deletes an Address entity by its ID.
     *
     * @param id the ID of the entity to delete
     */
    void deleteAddress(Long id);
}

