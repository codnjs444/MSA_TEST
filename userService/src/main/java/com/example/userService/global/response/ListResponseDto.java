package com.example.userService.global.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.List;

@Getter
@Schema(title = "공통응답")
public class ListResponseDto<T> {

	private boolean success;
	private String code;
	private String msg;
	private List<T> data;

	public ListResponseDto(int statusCode, boolean success, String code, String message, List<T> data) {
		this.success = success;
		if (statusCode > 299) {
			this.success = false;
		}
		this.code = code;
		this.msg = message;
		this.data = data;
	}
}
