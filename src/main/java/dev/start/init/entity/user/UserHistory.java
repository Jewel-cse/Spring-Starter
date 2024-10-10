package dev.start.init.entity.user;

import dev.start.init.constants.SequenceConstants;
import dev.start.init.entity.base.BaseEntity;
import dev.start.init.enums.UserHistoryType;
import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * Class UserHistory captures activities happening to user such as profile update, password reset
 * etc.
 *
 * @author Md Jewel
 * @version 1.0
 * @since 1.0
 */
@Entity
@Data
@NoArgsConstructor
public class UserHistory implements Serializable {
    @Serial private static final long serialVersionUID = -418682848586685969L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SequenceConstants.USER_HISTORY_SEQUENCE)
    @SequenceGenerator(name = SequenceConstants.USER_HISTORY_SEQUENCE, initialValue = SequenceConstants.USER_HISTORY_SEQUENCE_INITIAL_VALUE, allocationSize = SequenceConstants.USER_HISTORY_SEQUENCE_ALLOCATION)
    @Column(name="logId")
    private Long id;

    @Enumerated(EnumType.STRING)
    @NotNull
    private UserHistoryType userHistoryType;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp = LocalDateTime.now();

    @Column(name = "description", length = 500)
    private String description;
}

