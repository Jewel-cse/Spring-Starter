package dev.start.init.service.address.impl;

import dev.start.init.dto.AddressDto;
import dev.start.init.dto.mapper.AddressMapper;
import dev.start.init.entity.Address;
import dev.start.init.exception.ResourceNotFoundException;
import dev.start.init.repository.AddressRepository;
import dev.start.init.service.address.AddressService;

import dev.start.init.util.FileStore;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


/**
 * Service implementation for managing addresses.
 */
@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private static final Logger logger = LoggerFactory.getLogger(AddressServiceImpl.class);

    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper = AddressMapper.MAPPER;
    private final FileStore fileStore;

    /**
     * Retrieves all Address entities based on the provided specification and pageable.
     *
     * @param specification the criteria for filtering the entities
     * @param pageable      the pagination information
     * @return a page of AddressDto objects
     */
    @Override
    public Page<AddressDto> getAllAddresses(Specification<Address> specification, Pageable pageable) {
        logger.info("Finding all Address entities with given specification");

        Page<Address> addresses = addressRepository.findAll(specification, pageable);
        Page<AddressDto> addressDtos = addresses.map(addressMapper::toDto);
        logger.info("Found {} Address entities", addressDtos.getTotalElements());

        return addressDtos;
    }

    /**
     * Retrieves an Address entity by its ID.
     *
     * @param id the ID of the entity to retrieve
     * @return the found AddressDto
     */
    @Override
    public AddressDto getAddressById(Long id) {
        logger.info("Finding Address entity with id: {}", id);

        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found with id: " + id));
        logger.info("Found Address entity: {}", address);

        return addressMapper.toDto(address);
    }

    /**
     * Creates a new Address entity based on the provided DTO.
     *
     * @param addressDto the DTO containing the data for the new entity
     * @return the created AddressDto
     */
    @Transactional
    @Override
    public AddressDto createAddress(AddressDto addressDto, MultipartFile file) {
        logger.info("Creating a new Address entity from DTO: {}", addressDto);
        Address address = addressMapper.toEntity(addressDto);

        if (file != null && !file.isEmpty()) {
            String fileName = fileStore.storeFile(file, address);
            address.setFileName(fileName);
            address.setFileType(FilenameUtils.getExtension(fileName));
        }
        Address savedAddress = addressRepository.save(address);
        logger.info("Successfully created Address entity: {}", savedAddress);

        AddressDto resultDto = addressMapper.toDto(savedAddress);
        logger.info("Result dto {}",resultDto);

        return resultDto;
        //return addressMapper.toDto(savedAddress);
    }

    /**
     * Updates an existing Address entity with data from the provided DTO.
     *
     * @param id          the ID of the entity to update
     * @param addressDto  the DTO containing the updated data
     * @return the updated AddressDto
     */
    @Override
    public AddressDto updateAddress(Long id, AddressDto addressDto, MultipartFile file) {
        logger.info("Updating Address entity with id: {}", id);

        Address existingAddress = addressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found with id: " + id));

        addressMapper.updateEntityFromDto(addressDto, existingAddress);
        if (file != null && !file.isEmpty()) {
            String fileName = fileStore.storeFile(file, existingAddress);
            existingAddress.setFileName(fileName);
            existingAddress.setFileType(FilenameUtils.getExtension(fileName));
        }
        Address savedAddress = addressRepository.save(existingAddress);
        logger.info("Successfully updated Address entity: {}", savedAddress);

        return addressMapper.toDto(savedAddress);
    }

    /**
     * Deletes an Address entity by its ID.
     *
     * @param id the ID of the entity to delete
     */
    @Override
    @Transactional
    public void deleteAddress(Long id) {
        logger.info("Deleting Address entity with id: {}", id);

        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found with id: " + id));

        addressRepository.delete(address);
        logger.info("Successfully deleted Address entity with id: {}", id);
    }
}

