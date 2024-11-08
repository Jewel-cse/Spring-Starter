package dev.start.init.entity.base;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.envers.Audited;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * BaseEntity provides common fields and functionality for all entities in the system.
 * It includes fields for auditing, versioning, and a public UUID for external identification.
 *
 * @param <T> The type of the primary key for the entity
 */
@Data
@MappedSuperclass
//@Audited
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity<T>  {

    @Column(name = "PUBLIC_ID", unique = true, nullable = false, updatable = false)
    protected String publicId;

    @Version
    private short version;

    private boolean active;

    @ColumnDefault("0")
    private Integer stateId = 0;

    @ColumnDefault("0")
    private Integer actionId = 0;

    @CreatedDate
    @Column(name = "CREATED_AT", updatable = false)
    private LocalDateTime createdAt;

    @CreatedBy
    @Column(name = "CREATED_BY", updatable = false)
    private String createdBy;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @LastModifiedBy
    private String updatedBy;

    @Size(max = 100)
    private String machineName;

    @Size(max = 20)
    private String machineIp;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.createdBy = "Jewel";
        this.publicId = UUID.randomUUID().toString();
        this.active = true;
        this.actionId = (this.actionId != null) ? actionId : 1;
        this.stateId = (this.stateId != null) ? stateId : 1;
        if (machineIp == null) {
            try {
                InetAddress inetAddress = InetAddress.getLocalHost();
                String hostName = inetAddress.getHostName();
                String ipAddress = inetAddress.getHostAddress();
                this.machineIp = ipAddress;
                this.machineName = hostName;
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
        this.updatedBy = "Rana";
        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            String hostName = inetAddress.getHostName();
            String ipAddress = inetAddress.getHostAddress();
            this.machineIp = ipAddress;
            this.machineName = hostName;
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BaseEntity<?> that)) return false;
        return version == that.version &&
                Objects.equals(publicId, that.publicId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(publicId, version);
    }

    /**
     * Allows subclasses to redefine equality.
     *
     * @param other the other object to compare
     * @return true if other object can be equal to this
     */
    protected boolean canEqual(Object other) {
        return other instanceof BaseEntity;
    }
}
