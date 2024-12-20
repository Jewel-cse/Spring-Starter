package dev.start.init.dto.response.success;

import dev.start.init.annotation.IgnoreResponseBinding;
import dev.start.init.dto.response.error.ErrorResponse;
import org.springframework.core.MethodParameter;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import jakarta.annotation.Resource;

import java.util.List;

/**
 * @author Md Jewel
 * @version 1.0
 * @since 1.0
 *
 * A controller advice to intercept and modify the response body before it is written to the client.
 */
@ControllerAdvice
public class CustomResponseAdvice implements ResponseBodyAdvice<Object> {

    /**
     * Determines whether this advice is applicable for the given controller method's return type.
     * @param returnType the return type of the controller method
     * @param converterType the selected converter type
     * @return {@code true} if this advice should be applied, {@code false} otherwise
     */
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    /**
     * Intercepts the response body before it is written to the client.
     * Wraps the response body in a SuccessResponse if it is not already an ErrorResponse or SuccessResponse,
     * unless the method is annotated with {@link dev.start.init.annotation.IgnoreResponseBinding}.
     * @param body the body to be written
     * @param returnType the return type of the controller method
     * @param selectedContentType the content type selected through content negotiation
     * @param selectedConverterType the converter type selected to write the response
     * @param request the current request
     * @param response the current response
     * @return the modified or unmodified body to be written
     */

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request, ServerHttpResponse response) {
        // Check if the response is from a RestController
        if (returnType.getContainingClass().isAnnotationPresent(RestController.class)) {
            // Skip if the method has the IgnoreResponseBinding annotation
            if (!returnType.getMethod().isAnnotationPresent(IgnoreResponseBinding.class)) {
                // Check for known response types to prevent wrapping
                if (body instanceof ErrorResponse || body instanceof SuccessResponse) {
                    return body; // Return these types as it is
                }

                // Check for binary responses
                if (body instanceof byte[] || body instanceof ByteArrayResource ||
                        body instanceof InputStreamResource || body instanceof org.springframework.core.io.Resource ||
                        body instanceof String) {
                    // If it's a byte array, we can just return it
                    return body;
                }

                // Handle paginated responses
                if (body instanceof Page<?>) {
                    return new SuccessResponse<>(body);
                }

                // Wrap other responses in a SuccessResponse
                return new SuccessResponse<>(body);
            }
        }
        // Return the body unchanged if no conditions were met
        return body;
    }

}


