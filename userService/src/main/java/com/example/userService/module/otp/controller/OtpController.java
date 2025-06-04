package com.example.userService.module.otp.controller;

import com.example.userService.global.exception.CMailSenderException;
import com.example.userService.global.message.MessageService;
import com.example.userService.global.response.CommonResponse;
import com.example.userService.global.response.ListResponseDto;
import com.example.userService.global.response.SingleResponseDto;
import com.example.userService.module.otp.dto.OtpEmailRequest;
import com.example.userService.module.otp.dto.OtpRequestDto;
import com.example.userService.module.otp.service.OtpService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Tag(name = "OTP 인증", description = "OTP 인증 요청 및 자격 증명")
@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/igns/auth/otp")
public class OtpController {

	private final MessageService messageService;
	private final OtpService otpService;

	@PostMapping("")
	@Operation(summary = "인증 요청", description = "OTP 인증 메일을 전송한다")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "메일 전송 성공"),
		@ApiResponse(responseCode = "400", description = "유효하지 않은 메일 & 필수 값 누락"),
		@ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음"),
		@ApiResponse(responseCode = "409", description = "이미 인증번호를 발송 (중복)"),
		@ApiResponse(responseCode = "500", description = "메일 전송 실패")
	})
	public ResponseEntity<SingleResponseDto<String>> reqOtp(@Valid @RequestBody OtpEmailRequest param) throws CMailSenderException, Exception {
		otpService.reqOtp(param.getEmail(), param.getType());
		return CommonResponse.SingleResponse(HttpStatus.OK, true, messageService.getMessage("mailSendSuccess.code"), messageService.getMessage("mailSendSuccess.msg"), null);
	}

	@PostMapping("/certification")
	@Operation(summary = "자격 증명", description = "OTP 번호로 자격 증명을 실시한다 (C: 이메일 변경, J: 회원가입, P: 비밀번호 찾기)")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "인증 성공"),
		@ApiResponse(responseCode = "400", description = "유효하지 않은 인증 코드")
	})
	public ResponseEntity<ListResponseDto<String>> certification(@RequestBody OtpRequestDto param) throws Exception {
		String res = otpService.certification(param);
		List<String> result = new ArrayList<String>();
		result.add(res);
		return CommonResponse.ListResponse(HttpStatus.OK, true, messageService.getMessage("otpSuccess.code"), messageService.getMessage("otpSuccess.msg"), result);
	}
}
