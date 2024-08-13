package dev.start.init.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import java.io.Serial;

/**
 * Processes ResourceNotFound exception.
 *
 * @author Md Jewel Rana
 * @version 1.0
 * @since 1.0
 */

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Resource not found")
public class ResourceNotFoundException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 499248994881528687L;

    public ResourceNotFoundException(String message) {
        super(message);
    }
}

