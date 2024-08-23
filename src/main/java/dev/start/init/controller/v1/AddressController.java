package dev.start.init.controller.v1;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.start.init.constants.apiEndPoint.API_V1;
import dev.start.init.dto.AddressDto;
import dev.start.init.entity.Address;
import dev.start.init.service.address.AddressService;
import dev.start.init.util.specification.AddressSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


/**
 * REST controller for managing Addresses. Provides endpoints for creating,
 * updating, deleting, and fetching Addresses.
 *
 * @Author Md Jewel Rana
 * @version 1.0
 * @since 1.0
 */
@RestController
@RequestMapping(API_V1.API_V1_ADDRESS_URL)
@RequiredArgsConstructor
@Slf4j
public class AddressController {

    private static final Logger logger = LoggerFactory.getLogger(AddressController.class);
    private final AddressService addressService;
    @Value("${address.image.storage.path}")
    private String addressImageBasePath;

    /**
     * This endpoint is able to create a new address entity with file
     * @param addressData       contains the data with string format
     * @param addressFile       contains the file like image/pdf/docs etc
     * @return a response of created address entity.
     */

    @PostMapping
    public ResponseEntity<AddressDto> createAddress(
            @RequestParam(value = "addressData", required = false) String addressData,
            @RequestParam(value = "addressFile", required = false) MultipartFile addressFile) {
        log.info("Creating new Address: {}", addressData);

        try {
            // Convert the JSON string to AddressDto object
            ObjectMapper objectMapper = new ObjectMapper();
            AddressDto addressDto = objectMapper.readValue(addressData, AddressDto.class);

            // Call the service method to create the address
            AddressDto createdAddress = addressService.createAddress(addressDto, addressFile);

            log.info("Created Address: {}", createdAddress);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdAddress);

        } catch (JsonProcessingException e) {
            log.error("Error processing JSON for AddressDto", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * This endpoint able to update an address entity using its id
     * @param id                a number which is identified the entity
     * @param addressData       contains the data with string format
     * @param addressFile       contains the file like image/pdf/docs etc
     * @return a response of updated address entity.
     */

    @PutMapping("/{id}")
    public ResponseEntity<AddressDto> updateAddress(@PathVariable Long id,
                                                    @RequestParam(value = "addressData", required = false) String addressData,
                                                    @RequestParam(value = "addressFile", required = false) MultipartFile addressFile) {
        try {
            // Convert the JSON string to AddressDto object
            ObjectMapper objectMapper = new ObjectMapper();
            AddressDto addressDto = objectMapper.readValue(addressData, AddressDto.class);

            // Call the service method to create the address
            AddressDto updatedAddress = addressService.updateAddress(id, addressDto,addressFile);

            log.info("Updating Address with id {}: {}", id, addressData);
            return ResponseEntity.status(HttpStatus.CREATED).body(updatedAddress);

        } catch (JsonProcessingException e) {
            log.error("Error processing JSON for AddressDto", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * This endpoint able to delete an address entity using their id
     * @param id        a number which is identified the entity
     * @return no content return
     */

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAddress(@PathVariable Long id) {
        log.info("Deleting Address with id {}", id);
        addressService.deleteAddress(id);
        log.info("Deleted Address with id {}", id);
        return ResponseEntity.noContent().build();
    }

    /**
     * This endpoint able to get an address entity using their id
     * @param id        a number which is identified the entity
     * @return an address entity
     */

    @GetMapping("/{id}")
    public ResponseEntity<AddressDto> getAddressById(@PathVariable Long id) {
        log.info("Fetching Address with id {}", id);
        AddressDto addressDto = addressService.getAddressById(id);
        log.info("Fetched Address: {}", addressDto);
        return ResponseEntity.ok(addressDto);
    }

    /**
     *
     * @param page            page number (default: 0)
     * @param size            number of items per page (default: 10)
     * @param sortBy          field to sort by (default: 'id')
     * @param orderBy         sort orderBy (default: 'asc')
     * @param village         village filter
     * @param postOffice      postOffice filter
     * @param thana           thana filter
     * @param district        district filter
     * @return  a set of address based on filtering criteria and given condition
     */

    @GetMapping
    public ResponseEntity<Page<AddressDto>> getAllAddresses(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String orderBy,
            @RequestParam(required = false) String village,
            @RequestParam(required = false) String postOffice,
            @RequestParam(required = false) String thana,
            @RequestParam(required = false) String district) {

        logger.info("Fetching all Addresses with filters - page: {}, size: {}, sortBy: {}, direction: {}, village: {}, postOffice: {}, thana: {}, district: {}",
                page, size, sortBy, orderBy, village, postOffice, thana, district);

        Pageable pageable = PageRequest.of(page, size,
                orderBy.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending());

        // Building the specification for the query
        Specification<Address> specification = Specification.where(AddressSpecification.hasVillage(village)
                .and(AddressSpecification.hasPostOffice(postOffice))
                .and(AddressSpecification.hasThana(thana))
                .and(AddressSpecification.hasDistrict(district))
        );

        Page<AddressDto> addressesPage = addressService.getAllAddresses(specification, pageable);
        logger.info("Fetched Addresses page: {}", addressesPage);
        return ResponseEntity.ok(addressesPage);
    }

    /**
     * This endpoint able to get file using file name
     * @param filename      contains the filename
     * @return return a file like image or other docs
     */

    @GetMapping("/file")
    public ResponseEntity<Resource> getImage(@RequestParam(required = false) String filename) {
        try {
            Path path = Paths.get(addressImageBasePath).resolve(filename).normalize();
            if (!Files.exists(path) || !Files.isReadable(path)) {
                log.warn("File not found or not readable: {}", filename);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));
            String contentType = Files.probeContentType(path);

            if (contentType == null) {
                contentType = "application/octet-stream";
            }

            log.info("Serving file: {}", filename);
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + path.getFileName().toString() + "\"")
                    .contentLength(resource.contentLength())
                    .body(resource);
        } catch (IOException e) {
            log.error("Error occurred while serving file: {}", filename, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

