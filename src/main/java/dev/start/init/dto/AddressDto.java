package dev.start.init.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Data Transfer Object for the Address entity.
 *
 * @author Md Jewel Rana
 * @version 1.0
 * @since 1.0
 */
@Data
@EqualsAndHashCode()
public class AddressDto  {

    private String village;

    private String postOffice;

    private String thana;

    private String district;

    private String fileName;

    private String filePath;

    private String fileType;
}

