package com.example.userService.module.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Schema(title = "SIGN_UP_REQ : 회원가입 요청")
public class UserSignUpDto {

	@Schema(description = "사용자 ID", example = "igns1234")
	private String userId;
	
	@Email
	@Schema(description = "이메일", example = "igns@i-gns.co.kr")
	private String email;
	
	@Schema(description = "비밀번호 (8자리 이상 20자리 이하 영문+숫자+특수문자)", example = "test1234!")
	private String password;
	
	@Schema(description = "이름", example = "ad12307f")
	private String name;
	
	@Schema(description = "휴대전화번호", example = "010-1234-5678")
	private String phone;
	
	@Schema(description = "기본주소", example = "부산광역시 동래구 사직북로 10-1")
	private String addrBasic;
	
	@Schema(description = "상세주소", example = "청호빌딩 5층")
	private String addrDetail;
	
	@Schema(description = "우편번호", example = "46687")
	private String postNo;
	
	@Schema(description = "인증키 (Signature)", example = "ad12307f2313")
	private String secretKey;
	
	@Getter
	@Schema(title = "ID_DUPLICATE_PARAMETER : 아이디 중복 체크 파라미터")
	public static class userId {
		@Schema(description = "사용자 ID", example = "igns1234")
		private String userId;
	}




}