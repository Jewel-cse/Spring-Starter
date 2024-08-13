package dev.start.init.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import java.io.Serial;

/**
 * Responsible for user not found employee exception specifically.
 *
 * @author Md Jewel Rana
 * @version 1.0
 * @since 1.0
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Employee does not exist")
public class EmployeeNotFoundException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 499249948815528687L;

    /**
     * Constructs a new runtime exception with the specified detail message. The cause is not
     * initialized, and may subsequently be initialized by a call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for later retrieval by the
     *     {@link #getMessage()} method.
     */
    public EmployeeNotFoundException(String message) {
        super(message);
    }
}


