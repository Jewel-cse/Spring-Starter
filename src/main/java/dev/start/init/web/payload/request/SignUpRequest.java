package dev.start.init.web.payload.request;

import lombok.Data;

/**
 * This class models the format of the signUp request allowed through the controller endpoints.
 *
 * @author Md Jewel
 * @version 1.0
 * @since 1.0
 */
@Data
public final class SignUpRequest {

    private String username;

    private String email;

    private String password;
}

