package dev.start.init.util.specification;

import dev.start.init.entity.Company;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

/**
 * Utility class for creating JPA Specifications for filtering {@link Company} entities.
 *
 * <p>This class provides static methods to build {@link Specification} instances for various filtering criteria.</p>
 */
public class CompanySpecification {

    /**
     * Creates a Specification for filtering by company name (case-insensitive) and partial matching.
     *
     * @param comName the company name to filter by.
     * @return a Specification for filtering by company name.
     */
    public static Specification<Company> hasName(String comName) {
        return (root, query, builder) ->
                comName == null || comName.isEmpty() ? null : builder.like(builder.lower(root.get("comName")), "%" + comName.toLowerCase() + "%");
    }

    /**
     * Creates a Specification for filtering by company code (exact match, case-insensitive).
     *
     * @param comCode the company code to filter by.
     * @return a Specification for filtering by company code.
     */
    public static Specification<Company> hasCode(String comCode) {
        return (root, query, builder) ->
                comCode == null || comCode.isEmpty() ? null : builder.equal(builder.lower(root.get("comCode")), comCode.toLowerCase());
    }

    /**
     * Creates a Specification for filtering by the date a company was established.
     *
     * @param date the date to filter by.
     * @return a Specification for filtering by the establishment date.
     */
    public static Specification<Company> establishedAfter(LocalDate date) {
        return (root, query, builder) ->
                date == null ? null : builder.greaterThanOrEqualTo(root.get("comEstablished"), date);
    }

}



