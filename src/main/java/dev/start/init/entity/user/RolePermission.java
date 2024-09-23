package dev.start.init.entity.user;

import dev.start.init.constants.SequenceConstants;
import dev.start.init.entity.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Data;


import java.io.Serializable;
import java.util.BitSet;

@Data
@Entity
@Table(name = "ROLE_PERMISSION")
public class RolePermission extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SequenceConstants.ROLE_PERMISSION_SEQUENCE)
    @SequenceGenerator(name = SequenceConstants.ROLE_PERMISSION_SEQUENCE, initialValue = SequenceConstants.ROLE_PERMISSION_SEQUENCE_INITIAL_VALUE, allocationSize = SequenceConstants.ROLE_PERMISSION_SEQUENCE_ALLOCATION)
    @Column(name = "ID")
    private Long id;

    @Column(name = "CAN_CREATE")
    private Integer canCreate;

    @Column(name = "CAN_READ")
    private Integer canRead;

    @Column(name = "CAN_UPDATE")
    private Integer canUpdate;

    @Column(name = "CAN_DELETE")
    private Integer canDelete;

    @Column(name= "isActive")
    private Integer isActive;

    @Column(name = "FIELD_1")
    private String field1;
    @Column(name = "FIELD_2")
    private String field2;
    @Column(name = "FIELD_3")
    private String field3;

    @Column(name = "FIELD_4")
    private String field4;

    @Column(name = "DOCUMENT_ID")
    private String documentId;

    @Column(name = "MODULE_ID")
    private String moduleId;

//  @ManyToOne
//  @JoinColumn(name = "ROLE_ID")

    @Column(name = "ROLE_ID")
    private Long roleId;

}
