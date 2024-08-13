package dev.start.init.dto.response.error;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@ControllerAdvice
public class CustomErrorAdvice {

    private static final Logger logger = LoggerFactory.getLogger(CustomErrorAdvice.class);

    // Handle validation exceptions
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        int statusCode = HttpStatus.BAD_REQUEST.value();
        ErrorResponse errorResponse = new ErrorResponse("VALIDATION_ERROR", "Validation failed", ex.getMessage(), statusCode);
        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(statusCode));
    }

    // Handle database constraint violations
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleDataIntegrityViolationException1(DataIntegrityViolationException ex) {
        String detailedMessage = ex.getMostSpecificCause().getMessage();
        String MessageForUser = "A database constraint was violated. Please ensure your data is valid.";
        logger.error("Data integrity violation: {}", detailedMessage);

        int statusCode = HttpStatus.CONFLICT.value();
        ErrorResponse errorResponse = new ErrorResponse("DATA_INTEGRITY_VIOLATION", MessageForUser, ex.getMessage(), statusCode);
        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(statusCode));
    }

    // Handle database constraint violations
    // @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        String detailedMessage = ex.getMostSpecificCause().getMessage();
        String MessageForUser = "A database constraint was violated. Please ensure your data is valid.";
        Map<String, String> errorDetails = new HashMap<>();

        // Parse the detailed message to extract the field causing the unique constraint violation
        Pattern pattern = Pattern.compile("ORA-00001: unique constraint \\(([^)]+)\\) violated");
        Matcher matcher = pattern.matcher(detailedMessage);
        if (matcher.find()) {
            String constraintName = matcher.group(1);
            // Map the constraint name to the corresponding field name
            String fieldName = mapConstraintToField(constraintName);
            if (fieldName != null) {
                MessageForUser = "The value for the field '" + fieldName + "' must be unique.";
                errorDetails.put(fieldName, "must be unique");
            }
        }

        logger.error("Data integrity violation: {}", detailedMessage);
        int statusCode = HttpStatus.CONFLICT.value();
        ErrorResponse errorResponse = new ErrorResponse("DATA_INTEGRITY_VIOLATION", MessageForUser, ex.getMessage(), statusCode);
        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(statusCode));
    }

    // Map database constraint names to field names
    private String mapConstraintToField(String constraintName) {
        // Map the constraint name to the corresponding field name
        // This mapping should be based on your database schema
        // Example:
        Map<String, String> constraintFieldMap = new HashMap<>();
        constraintFieldMap.put("dev.SYS_C0031803", "username");
        constraintFieldMap.put("dev.SYS_C0031804", "email");
        // Add more mappings as needed

        return constraintFieldMap.get(constraintName);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAllOtherExceptions(Exception ex, WebRequest request) {
        String MessageForUser = "An unknown error occurred. Please try again later.";
        logger.error("Unexpected error occurred: {}", ex.getMessage(), ex);

        int statusCode = HttpStatus.INTERNAL_SERVER_ERROR.value();

        ErrorResponse errorResponse = new ErrorResponse("INTERNAL_SERVER_ERROR", MessageForUser, ex.getMessage(), statusCode);

        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(statusCode));
    }

    // ErrorResponse class
    @Setter
    @Getter
    public static class ErrorResponse {
        private String errorCode;
        private String message;
        private Object details;
        private int statusCode;

        public ErrorResponse(String errorCode, String message) {
            this.errorCode = errorCode;
            this.message = message;
        }

        public ErrorResponse(String errorCode, String message, Object details) {
            this.errorCode = errorCode;
            this.message = message;
            this.details = details;
        }


        public ErrorResponse(String errorCode, String message, Object details, int statusCode) {
            this.errorCode = errorCode;
            this.message = message;
            this.details = details;
            this.statusCode=statusCode;
        }
    }
}

