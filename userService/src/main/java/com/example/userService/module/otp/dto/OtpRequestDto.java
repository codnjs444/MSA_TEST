package com.example.userService.module.otp.dto;

import com.example.userService.module.otp.entity.OtpEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Schema(title = "OTP_REQ : 인증 요청")
public class OtpRequestDto {

	@Schema(description = "사용자 이메일", example = "otp@i-gns.co.kr")
	private String email;
	
	@Schema(description = "OTP 번호 (8자리(숫자+영어)랜덤)", example = "ad12307f")
	private String password;
	
	@JsonIgnore
	@Schema(description = "요청 시간 (yyyy-MM-dd HH:mm:ss.sss)", example = "2024-12-12 12:12:12.121")
	private Date requestedTime;
	
	@JsonIgnore
	@Schema(description = "시크릿 키 (signature 생성)", example = "1231SD46")
	private String secretKey;
	
	@JsonIgnore
	@Schema(description = "인증 여부 (Y, N)", example = "Y")
	private String isCertificated;

	public OtpEntity toEntity() {
		return OtpEntity.builder().email(email).oneTimePassword(password).otpRequestedTime(requestedTime)
				.secretKey(secretKey).isCertificated(isCertificated).build();
	}
}
