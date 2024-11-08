package dev.start.init.exception.user;

public class EmailExistsException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    public EmailExistsException(final String message) {
        super(message);
    }
}
