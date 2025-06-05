package com.example.userService.global.exception;

public class CValidationUserIdException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public CValidationUserIdException(String message, Throwable t) {
        super(message, t);
    }

    public CValidationUserIdException(String message) {
        super(message);
    }

    public CValidationUserIdException() {
        super();
    }
}
