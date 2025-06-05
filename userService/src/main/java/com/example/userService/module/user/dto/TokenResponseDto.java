package com.example.userService.module.user.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(title = "TOKEN_RES : 토큰 정보 응답")
public class TokenResponseDto {

	@Schema(description = "Access Token", example = "")
	private String accessToken;
	
	@Schema(description = "Refresh Token", example = "")
	private String refreshToken;
	


}
