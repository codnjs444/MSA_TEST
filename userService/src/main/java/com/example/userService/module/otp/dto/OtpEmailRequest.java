package com.example.userService.module.otp.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
@Schema(title = "OTP_EMAIL_REQ : 인증 메일 요청")
public class OtpEmailRequest {

	@Schema(description = "사용자 이메일", example = "otp@i-gns.co.kr")
	private String email;

	@NotBlank
	@Schema(description = "요청 타입 (C: 이메일 변경, J: 회원가입, P: 비밀번호 찾기)", example = "C")
	private String type;
}
