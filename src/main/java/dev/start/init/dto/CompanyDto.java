package dev.start.init.dto;

import dev.start.init.constants.CompanyConstants;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
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

    private Integer id;
    private String publicId;

    @NotBlank
    @Size(min = 3,max = 100,message = CompanyConstants.COMPANY_CODE)
    private String comCode;

    @NotBlank
    @Size(max = 100,message = CompanyConstants.COMPANY_NAME)
    private String comName;

    @NotNull
    @Past(message = CompanyConstants.COMPANY_ESTABLISH_DATE)
    private LocalDate comEstablished;
}

