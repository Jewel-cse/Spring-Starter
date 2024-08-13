package dev.start.init.dto.response.error;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Md Jewel Rana
 * @version 1.0
 * @since 1.0
 *
 * A generic class for representing the structure of an error response.
 * @param <T> the type of the error body
 */
@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse<T> {
    private ErrorDTO<T> error;

    /**
     * Constructs a new ErrorResponse with the specified object and message.
     * @param object the body of the error message
     * @param message the error message
     */
    public ErrorResponse(T object, String message) {
        error = new ErrorDTO<T>(object, message);
    }

}
