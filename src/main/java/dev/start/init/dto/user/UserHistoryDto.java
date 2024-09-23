package dev.start.init.dto.user;

import dev.start.init.enums.UserHistoryType;
import java.io.Serial;
import java.io.Serializable;

import dev.start.init.web.payload.pojo.SeparateDateFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * The UserHistoryDto transfers user history from outside into the application and vice versa.
 *
 * @author Md Jewel
 * @version 1.0
 * @since 1.0
 */
@Data
/*@EqualsAndHashCode(callSuper = true)*/
public final class UserHistoryDto  implements Serializable {
    @Serial private static final long serialVersionUID = -8842211126703873453L;

    private UserHistoryType userHistoryType;
    private String timeElapsedDescription;
    private SeparateDateFormat separateDateFormat;
}

