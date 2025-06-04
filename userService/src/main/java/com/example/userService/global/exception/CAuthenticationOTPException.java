package com.example.userService.global.exception;

public class CAuthenticationOTPException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	
	public CAuthenticationOTPException(String message, Throwable t) {
        super(message, t);
    }
	
	public CAuthenticationOTPException(String message) {
        super(message);
    }
	
	public CAuthenticationOTPException() {
        super();
    }
}
