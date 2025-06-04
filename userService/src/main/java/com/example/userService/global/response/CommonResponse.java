package com.example.userService.global.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class CommonResponse {
	
	public static <T> ResponseEntity<SingleResponseDto<T>> SingleResponse(HttpStatus status, boolean success, String code , String message, T data) {
		SingleResponseDto<T> commonResponse = new SingleResponseDto<T>(status.value(), success, code , message, data);
	    return new ResponseEntity<>(commonResponse, HttpStatus.valueOf(status.value()));
	}
	 
	public static <T> ResponseEntity<ListResponseDto<T>> ListResponse(HttpStatus status, boolean success, String code , String message, List<T> data) {
		ListResponseDto<T> commonResponse = new ListResponseDto<T>(status.value(), success, code ,message, data);
	    return new ResponseEntity<>(commonResponse, HttpStatus.valueOf(status.value()));
	}
}
