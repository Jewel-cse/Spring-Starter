package dev.start.init.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import dev.start.init.constants.SequenceConstants;
import dev.start.init.entity.base.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "COMPANY")
public class Company extends BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SequenceConstants.COMPANY_SEQUENCE)
    @SequenceGenerator(name = SequenceConstants.COMPANY_SEQUENCE,
            sequenceName = "CompanySequence",
            initialValue = SequenceConstants.COMPANY_SEQUENCE_INITIAL_VALUE,
            allocationSize = SequenceConstants.COMPANY_SEQUENCE_ALLOCATION)
    private Long id;

    private String comName;

    @Column(unique = true, nullable = false)
    private String comCode;

    private LocalDate comEstablished;

    /*@OneToMany(mappedBy = "company", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JsonManagedReference
    private List<Employee> employeeList;*/

    public Company(String ComName, String ComCode, LocalDate estDate) {
        this.comName = ComName;
        this.comCode = ComCode;
        this.comEstablished = estDate;
    }

}

