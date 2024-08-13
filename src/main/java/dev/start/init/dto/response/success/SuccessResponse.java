package dev.start.init.dto.response.success;

/**
 * @author Md Jewel Rana
 * @version 1.0
 * @since 1.0
 *
 * A generic class for representing the structure of a success response.
 * @param <T> the type of the response body
 */
public class SuccessResponse<T> {

    /**
     * The SuccessDTO containing the response details.
     */
    private SuccessDTO<T> response;

    /**
     * Retrieves the SuccessDTO of the response.
     * @return the SuccessDTO of the response
     */
    public SuccessDTO<T> getResponse() {
        return response;
    }

    /**
     * Sets the SuccessDTO of the response.
     * @param response the SuccessDTO to set
     */
    public void setResponse(SuccessDTO<T> response) {
        this.response = response;
    }

    /**
     * Constructs a new SuccessResponse with the specified object.
     * @param object the body of the success message
     */
    public SuccessResponse(T object) {
        this.response = new SuccessDTO<>(object);
    }

    /**
     * Constructs a new SuccessResponse with the specified object and message.
     * @param object the body of the success message
     * @param message the success message
     */
    public SuccessResponse(T object, String message) {
        this.response = new SuccessDTO<>(object, message);
    }

    /**
     * Constructs a new SuccessResponse with the specified object, length, and message.
     * @param object the body of the success message
     * @param length the length of the body
     * @param message the success message
     */
    public SuccessResponse(T object, Integer length, String message) {
        this.response = new SuccessDTO<>(object, length, message);
    }

    /**
     * Constructs a new SuccessResponse with the specified object and length.
     * @param object the body of the success message
     * @param length the length of the body
     */
    public SuccessResponse(T object, Integer length) {
        this.response = new SuccessDTO<>(object, length);
    }

}

