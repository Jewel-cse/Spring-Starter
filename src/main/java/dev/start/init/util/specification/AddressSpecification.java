package dev.start.init.util.specification;

import dev.start.init.entity.Address;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

/**
 * Utility class for creating JPA Specifications for filtering {@link Address} entities.
 *
 * <p>This class provides static methods to build {@link Specification} instances for various filtering criteria.</p>
 */
public class AddressSpecification {

    /**
     * Creates a Specification for filtering by street name (case-insensitive) and partial matching.
     *
     * @param street the street name to filter by.
     * @return a Specification for filtering by street name.
     */
    public static Specification<Address> hasStreet(String street) {
        return (root, query, builder) ->
                street == null ? null : builder.like(builder.lower(root.get("street")), "%" + street.toLowerCase() + "%");
    }

    /**
     * Creates a Specification for filtering by city name (case-insensitive) and partial matching.
     *
     * @param village the city name to filter by.
     * @return a Specification for filtering by city name.
     */
    public static Specification<Address> hasVillage(String village) {
        return (root, query, builder) ->
                village == null ? null : builder.like(builder.lower(root.get("village")), "%" + village.toLowerCase() + "%");
    }

    /**
     * Creates a Specification for filtering by state.
     *
     * @param thana the state name to filter by.
     * @return a Specification for filtering by state.
     */
    public static Specification<Address> hasThana(String thana) {
        return (root, query, builder) ->
                thana == null ? null : builder.equal(root.get("thana"), thana);
    }

    /**
     * Creates a Specification for filtering by postal code.
     *
     * @param hasPostOffice the postal code to filter by.
     * @return a Specification for filtering by postal code.
     */
    public static Specification<Address> hasPostOffice(String hasPostOffice) {
        return (root, query, builder) ->
                hasPostOffice == null ? null : builder.equal(root.get("hasPostOffice"), hasPostOffice);
    }

    /**
     * Creates a Specification for filtering by country.
     *
     * @param district the country name to filter by.
     * @return a Specification for filtering by country.
     */
    public static Specification<Address> hasDistrict(String district) {
        return (root, query, builder) ->
                district == null ? null : builder.equal(root.get("district"), district);
    }

    /**
     * Creates a Specification for filtering by address creation date.
     *
     * @param creationDate the creation date to filter by.
     * @return a Specification for filtering by creation date.
     */
    public static Specification<Address> hasCreationDate(LocalDate creationDate) {
        return (root, query, builder) ->
                creationDate == null ? null : builder.equal(root.get("creationDate"), creationDate);
    }

    /**
     * Creates a Specification for filtering by whether the address is active.
     *
     * @param active the active status to filter by.
     * @return a Specification for filtering by active status.
     */
    public static Specification<Address> isActive(Boolean active) {
        return (root, query, builder) ->
                active == null ? null : builder.equal(root.get("active"), active);
    }
}

