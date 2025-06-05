package com.example.userService.module.user.controller;


import com.example.userService.global.keycloack.util.KeycloakUtil;
import com.example.userService.global.message.MessageService;
import com.example.userService.global.response.CommonResponse;
import com.example.userService.global.response.ListResponseDto;
import com.example.userService.global.response.SingleResponseDto;
import com.example.userService.module.user.dto.TokenResponseDto;
import com.example.userService.module.user.dto.UserResponseDto;
import com.example.userService.module.user.dto.UserSignUpDto;
import com.example.userService.module.user.dto.UserSignUpDto.userId;
import com.example.userService.module.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Tag(name = "유저", description = "회원가입, 사용자 정보 조회 및 변경 등 유저 관련 서비스")
@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/igns/auth")
public class UserController {

	private final UserService userService;
	private final MessageService messageService;

	@PostMapping("/sign-up")
	@Operation(summary = "회원 가입", description = "회원 등록한다")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "가입 성공"),
			@ApiResponse(responseCode = "400", description = "비밀번호 유효성 실패"),
			@ApiResponse(responseCode = "409", description = "아이디 & 이메일 중복"),
			@ApiResponse(responseCode = "500", description = "회원가입 실패")
	})
	public ResponseEntity<ListResponseDto<UserResponseDto>> signUp(@Valid HttpServletResponse response, @RequestBody UserSignUpDto userSignUpDto) throws Exception {
		UserResponseDto res = userService.signUp(response, userSignUpDto);
		if (res != null) {
			List<UserResponseDto> result = new ArrayList<UserResponseDto>();
			HttpHeaders headers = new HttpHeaders();
			result.add(res);
			return new ResponseEntity<>(CommonResponse.ListResponse(HttpStatus.OK, true, "0000", "ok", result).getBody(), headers, HttpStatus.valueOf(200));
		} else {
			return CommonResponse.ListResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, messageService.getMessage("signupFail.code"), messageService.getMessage("signupFail.msg"), null);
		}
	}

	@PostMapping("/user/checkId")
	@Operation(summary = "아이디 중복 체크", description = "아이디 중복 체크한다")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "사용 가능"),
			@ApiResponse(responseCode = "400", description = "아이디 유효성 실패"),
			@ApiResponse(responseCode = "409", description = "중복된 아이디"),
			@ApiResponse(responseCode = "500", description = "서버 에러")
	})
	public ResponseEntity<SingleResponseDto<String>> checkUserId(@Valid @RequestBody userId param) throws Exception {
		userService.checkUserId(param.getUserId());
		return CommonResponse.SingleResponse(HttpStatus.OK, true, messageService.getMessage("possiableId.code"), messageService.getMessage("possiableId.msg"), null);
	}

	@GetMapping("/user/accesstoken")
	@Operation(summary = "AccessToken 발급", description = "AccessToken을 발급한다")
	public ResponseEntity<SingleResponseDto<TokenResponseDto>> issueAccessToken(@Valid String userId, @Valid String password) throws Exception {
		TokenResponseDto jwt = KeycloakUtil.getAccessToken(userId, password);
		return CommonResponse.SingleResponse(HttpStatus.OK, true, messageService.getMessage("tokenIssue.code"), messageService.getMessage("tokenIssue.msg"), jwt);
	}
}
