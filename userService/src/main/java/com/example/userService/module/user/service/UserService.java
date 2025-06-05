package com.example.userService.module.user.service;

import com.example.userService.global.exception.CCommonException;
import com.example.userService.global.exception.CDuplicateIdException;
import com.example.userService.global.exception.CValidationUserIdException;
import com.example.userService.global.keycloack.util.KeycloakUtil;
import com.example.userService.global.redis.util.RedisUtil;
import com.example.userService.global.utils.RegexUtils;
import com.example.userService.global.utils.SecurityCrypto;
import com.example.userService.module.user.dto.UserResponseDto;
import com.example.userService.module.user.dto.UserSignUpDto;
import com.example.userService.module.user.entity.Role;
import com.example.userService.module.user.entity.User;
import com.example.userService.module.user.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Base64;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	
	private final RedisUtil redisUtil;
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Transactional
	public UserResponseDto signUp(HttpServletResponse response, UserSignUpDto userSignUpDto) throws Exception {
		try {
			UserResponseDto dto = new UserResponseDto();
			User user = null;
			Optional<User> entity = userRepository.findByUserId(userSignUpDto.getUserId());
			if (entity.isPresent()) {
				user = entity.get();//TODO 중복회원가입임을 알려주기 위함인가? -> id가 유니크값이므로 중복 방지
			} else {
				String password = new String(Base64.getDecoder().decode(userSignUpDto.getPassword()));
				String phone = "";
				if(userSignUpDto.getPhone() != null) {
					phone = userSignUpDto.getPhone().trim();
				}
				user = User.builder()
					.uid(UUID.randomUUID().toString())
					.userId(userSignUpDto.getUserId().trim())
					.email(SecurityCrypto.encryptString(userSignUpDto.getEmail().trim()))
					.name(userSignUpDto.getName().trim())
					.password(password)
					.addrBasic(SecurityCrypto.encryptString(userSignUpDto.getAddrBasic()))
					.addrDetail(SecurityCrypto.encryptString(userSignUpDto.getAddrDetail()))
					.postNo(SecurityCrypto.encryptString(userSignUpDto.getPostNo()))
					.role(Role.USER)
					//TODO 역할 레벨 변경 필요 
					.phone(SecurityCrypto.encryptString(phone))
					.useYn("Y")//TODO 타입 Boolean 값 변경 필요
					.build();
				user.passwordEncode(passwordEncoder);
				userRepository.save(user);

				//키클록에 토큰 요청
				String accessToken = KeycloakUtil.getAdminAccessToken();
				if(accessToken != null) {
					//레디스에 Access Token 토큰 등록 요청
					String key = "at:" + user.getUid();
					redisUtil.saveAccessToken(key, accessToken, 60);

					//키클록에 유저 등록 요청
					String error = KeycloakUtil.createUser(
							accessToken,
							"piiep",
							user.getUid(),
							userSignUpDto.getUserId().trim(),
							userSignUpDto.getEmail().trim(),
							password,
							3
					);
					if(error != null) {
						System.err.println("유저 등록 실패");
						throw new CCommonException();//TODO 오류로그 다시 봐야함
					}
					//레디스에 토큰 삭제 요청
					redisUtil.deleteToken(key);
				}else {
					System.err.println("토큰 발급 실패");
					throw new CCommonException();//TODO 오류로그 다시 봐야함
				}
				
			}
			
			dto.setUid(user.getUid());
			dto.setUserId(user.getUserId());
			dto.setEmail(SecurityCrypto.decryptString(user.getEmail()));
			dto.setName(user.getName());
			dto.setAddrBasic(SecurityCrypto.decryptString(user.getAddrBasic()));
			dto.setAddrDetail(SecurityCrypto.decryptString(user.getAddrDetail()));
			dto.setPostNo(SecurityCrypto.decryptString(user.getPostNo()));
			dto.setRole(user.getRole().name());
			dto.setPhone(SecurityCrypto.decryptString(user.getPhone()));
				
			return dto;
		} catch (DataAccessException e) {
			throw new CCommonException(HttpStatus.INTERNAL_SERVER_ERROR, "eDataAccessFail", "USER SIGN-UP FAILED", e.getMessage(), e);
		}
	}
	

	public void checkUserId(String userId) {
		if (!RegexUtils.isUserId(userId)) {
			throw new CValidationUserIdException(userId);
		}
		Optional<User> entity = userRepository.findByUserId(userId);
		if (!entity.isEmpty()) {
			throw new CDuplicateIdException(userId);
		}
		
	}
}
