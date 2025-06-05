package com.example.userService.module.user.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(title = "USER_RES : 사용자 정보 응답")
public class UserResponseDto {

	@Schema(description = "UUID", example = "ad12307f2131-23-dsa12312asd")
	private String uid;

	@Schema(description = "사용자 ID", example = "igns1234")
	private String userId;

	@Schema(description = "이메일", example = "igns@i-gns.co.kr")
	private String email;

	@Schema(description = "이름", example = "일주")
	private String name;

	@Schema(description = "기본주소", example = "부산광역시 동래구 사직북로 10-1")
	private String addrBasic;

	@Schema(description = "상세주소", example = "청호빌딩 5층")
	private String addrDetail;

	@Schema(description = "우편번호", example = "46687")
	private String postNo;

	@Schema(description = "휴대전화번호", example = "010-1234-5678")
	private String phone;

	@Schema(description = "역할 (GUEST, NORMAL, USER, ADMIN)", example = "GUEST")
	private String role;


}
