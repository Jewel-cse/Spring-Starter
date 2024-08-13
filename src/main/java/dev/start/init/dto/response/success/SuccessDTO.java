package dev.start.init.dto.response.success;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;
import java.util.List;
import java.util.Map;

/**
 * @author Md jewel Rana
 * @version 1.0
 * @since 1.0
 *
 * A generic Data Transfer Object (DTO) for representing successful responses.
 * @param <T> the type of the body of the success message
 */
@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SuccessDTO<T> implements java.io.Serializable {

    /**
     * The body of the success message, can be any type.
     */
    private T body;

    /**
     * The length of the body, defaults to 1.
     */
    private int length = 0;

    /**
     * The success message as a string.
     */
    private String message = null;


    /**
     * The success message as a string.
     */
    private Integer totalPages = null;

    /**
     * The success message as a string.
     */
    private Long totalElements = null;

    /**
     * The success message as a string.
     */
    private Integer currentPage = null;


    /**
     * Constructs a new SuccessDTO with the specified body, length, and message.
     * @param body the body of the success message
     * @param length the length of the body
     * @param message the success message
     */
    public SuccessDTO(T body, int length, String message) {
        this.body = body;
        this.length = length;
        this.message = message;
        if (length == 0) {
            if (this.body instanceof List<?>) this.length = ((List<?>) this.body).size();
            if (this.body instanceof Map<?,?>) this.length = ((Map<?,?>) this.body).size();
        }
    }

    /**
     * Constructs a new SuccessDTO with the specified body and message.
     * @param body the body of the success message
     * @param message the success message
     */
    public SuccessDTO(T body, String message) {
        this.body = body;
        this.message = message;
        if (this.body instanceof List) this.length = ((List<?>) this.body).size();
        if (this.body instanceof Map) this.length = ((Map<?,?>) this.body).size();
    }

    /**
     * Constructs a new SuccessDTO with the specified body and length.
     * @param body the body of the success message
     * @param length the length of the body
     */
    public SuccessDTO(T body, Integer length) {
        this.body = body;
        this.length = length;
    }

    /**
     * Constructs a new SuccessDTO with the specified body.
     * @param body the body of the success message
     */
    public SuccessDTO(T body) {
        this.body = body;
        if( body instanceof Page<?>) {
            this.body = (T) ((Page<?>) body).getContent();
            this.totalElements = ((Page<?>) body).getTotalElements();
            this.totalPages = ((Page<?>) body).getTotalPages();
            this.currentPage = ((Page<?>) body).getNumber();
        }
        if (this.body instanceof List) this.length = ((List<?>) this.body).size();
        if (this.body instanceof Map) this.length = ((Map<?,?>) this.body).size();
        if (this.body instanceof Map) this.length = ((Map<?,?>) this.body).size();
    }

}


