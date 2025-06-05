package com.example.userService.global.exception;

public class CDuplicateIdException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public CDuplicateIdException(String message, Throwable t) {
        super(message, t);
    }
	
	public CDuplicateIdException(String message) {
        super(message);
    }
	
	public CDuplicateIdException() {
        super();
    }
}
