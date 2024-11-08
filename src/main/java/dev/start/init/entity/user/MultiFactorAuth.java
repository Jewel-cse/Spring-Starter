package dev.start.init.entity.user;

import com.fasterxml.jackson.annotation.JsonBackReference;
import dev.start.init.enums.MultiFactorMethodType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "mfa_factor_auth")
public class MultiFactorAuth {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    @ToString.Exclude
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MultiFactorMethodType methodType;

    private String secretCode;

    private boolean isActive;

    private boolean verified;

    private String verificationCode;  // For temporary verification codes (SMS/Email)

    private LocalDateTime codeExpiresAt;  // Expiry for the verification code

}

