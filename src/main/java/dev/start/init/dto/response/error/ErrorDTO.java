package dev.start.init.dto.response.error;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 *  * @author Md Jewel Rana
 *  * @version 1.0
 *  * @since 1.0
 *
 * A generic Data Transfer Object (DTO) for representing errors.
 * @param <T> the type of the body of the error message
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorDTO<T> implements java.io.Serializable {

    /**
     * The body of the error message, can be any type.
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T body;

    /**
     * The error message as a string.
     */
    private String message;

    /**
     * Constructs a new ErrorDTO with the specified body and message.
     * @param body the body of the error message
     * @param message the error message
     */
    public ErrorDTO(T body, String message) {
        this.body = body;
        this.message = message;
    }

}
