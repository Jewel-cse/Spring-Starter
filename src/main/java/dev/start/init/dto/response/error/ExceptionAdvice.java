package dev.start.init.dto.response.error;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import java.util.Arrays;
import java.util.List;



/**
 * @author Md Jewel Rana
 * @version 1.0
 * @since 1.0
 *
 * A controller advice to handle exceptions globally.
 */
//@ControllerAdvice
public class ExceptionAdvice {
    boolean stackTrace = false;

    /**
     * Handles all exceptions and returns a standardized error response.
     * @param ex the exception that was thrown
     * @return an ErrorResponse containing the stack trace elements and the error message
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorResponse<List<StackTraceElement>> processAllError(Exception ex) {
        List<StackTraceElement> ele = null;
        if (stackTrace) ele = Arrays.asList(ex.getStackTrace());

        ErrorResponse<List<StackTraceElement>> response = new ErrorResponse<>(ele, ex.getMessage());
        return response;
    }
}
