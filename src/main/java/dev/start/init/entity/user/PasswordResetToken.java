package dev.start.init.entity.user;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

//@Entity
@Data
@Table(name = "password_reset_tokens")
public class PasswordResetToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)  // Foreign key to Users table
    private User user;

    @Column(name = "token_value", nullable = false, unique = true, length = 256)
    private String tokenValue;  // Actual token sent to the user

    @Column(name = "expiry_date", nullable = false)
    private LocalDateTime expiryDate;
}

