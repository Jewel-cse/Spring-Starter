package dev.start.init.util.core;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class AuthenticateUser {

    private static UserDetails getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            return (UserDetails) authentication.getPrincipal();
        }
        return null; // or throw an exception if appropriate
    }

    public static  String getAuthenticatedUserName() {
        UserDetails userDetails = getAuthenticatedUser();
        return (userDetails != null) ? userDetails.getUsername() : null;
    }
}

