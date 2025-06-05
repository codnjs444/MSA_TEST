package com.example.userService.global.exception;

import org.springframework.http.HttpStatus;

public class CCommonException extends RuntimeException {

	private static final long serialVersionUID = 1L;	
	private final HttpStatus status;
	private final String i18nKey;
	private final String title;
	private final String type;			// E: ERROR, W: WARN
	
	public CCommonException(HttpStatus status, String type, String i18nKey, String title, String message, Throwable t) {
        super(message, t);
        this.status = status;
        this.type = type;
        this.title = title;
        this.i18nKey = i18nKey;
    }
	
	public CCommonException(HttpStatus status, String i18nKey, String title, String message, Throwable t) {
        super(message, t);
        this.status = status;
        this.type = "E";
        this.title = title;
        this.i18nKey = i18nKey;
    }
	
	public CCommonException(HttpStatus status, String type, String i18nKey, String title, String message) {
        super(message);
        this.status = status;
        this.type = type;
        this.title = title;
        this.i18nKey = i18nKey;
    }
	
	public CCommonException(HttpStatus status, String i18nKey, String title, String message) {
        super(message);
        this.status = status;
        this.type = "E";
        this.title = title;
        this.i18nKey = i18nKey;
    }
	
	public CCommonException(String i18nKey, String title, String message, Throwable t) {
        super(message, t);
        this.status = HttpStatus.INTERNAL_SERVER_ERROR;
        this.type = "E";
        this.title = title;
        this.i18nKey = i18nKey;
    }
	
	public CCommonException(String i18nKey, String title, String message) {
        super(message);
        this.status = HttpStatus.INTERNAL_SERVER_ERROR;
        this.type = "E";
        this.title = title;
        this.i18nKey = i18nKey;
    }
	
	public CCommonException() {
        super();
        this.status = HttpStatus.INTERNAL_SERVER_ERROR;
        this.type = "E";
        this.title = "Exception";
        this.i18nKey = "eException";
    }
	
	public HttpStatus getStatus() {
        return status;
    }
	
	public String getType() {
		return type;
	}
	
	public String getTitle() {
		return title;
	}
	
	public String getI18nKey() {
		return i18nKey;
	}
}
