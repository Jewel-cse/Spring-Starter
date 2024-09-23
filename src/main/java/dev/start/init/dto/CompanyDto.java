package dev.start.init.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

/**
 * Data Transfer Object for Company details.
 *
 * <p>Transfers Company data between different layers of the application.</p>
 *
 * @author Md Jewel Rana
 * @version 1.0
 * @since 1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class CompanyDto extends BaseDto {

    private String publicId;

    private String comName;

    private String comCode;

    private LocalDate comEstablished;

}

