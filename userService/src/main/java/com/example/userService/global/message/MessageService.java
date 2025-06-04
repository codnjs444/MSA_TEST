package com.example.userService.global.message;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RequiredArgsConstructor
@RestControllerAdvice
public class MessageService {

	private final MessageSource messageSource;
	
	// code 정보에 맞는 메시지 조회
	public String getMessage(String code) {
        return getMessage(code, null);
    }
	
	// 현재 locale에 맞는 메시지 조회
	private String getMessage(String code, Object[] args) {
        return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
    }
}
