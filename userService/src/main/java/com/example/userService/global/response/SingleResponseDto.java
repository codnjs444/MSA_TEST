package com.example.userService.global.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(title = "공통응답")
public class SingleResponseDto<T> {

	private boolean success;
	private String code;
	private String msg;
	private T data;

	public SingleResponseDto(int statusCode, boolean success, String code , String message, T data) {
		this.success = success;
		if (statusCode > 299) {
			this.success = false;
		}
		this.code = code;
		this.msg = message;
		this.data = data;
	}
}
