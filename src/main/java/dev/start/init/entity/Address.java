package dev.start.init.entity;

import dev.start.init.entity.base.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;

@Data
@Entity
@Table(name = "ADDRESS")
public class Address extends BaseEntity<Long> implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Size(max = 100)
    @NotBlank(message = "village is not blank")
    @Column(name = "VILLAGE")
    private String village;

    @Size(max = 100)
    @Column(name = "POST_OFFICE")
    private String postOffice;

    @Size(max = 100)
    @Column(name = "THANA")
    private String thana;

    @Size(max = 100)
    @NotBlank(message = "district is not blank")
    @Column(name = "DISTRICT")
    private String district;

    @Size(max = 500)
    @Column(name = "FILENAME", length = 500)
    private String fileName;

    @Size(max = 500)
    @Column(name = "FILE_PATH")
    private String filePath;

    @Size(max = 16)
    @Column(name = "FILE_TYPE")
    private String fileType;
}

