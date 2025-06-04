package com.example.userService.global.exception;

public class CValidationEmailException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public CValidationEmailException(String message, Throwable t) {
        super(message, t);
    }

    public CValidationEmailException(String message) {
        super(message);
    }

    public CValidationEmailException() {
        super();
    }
}
