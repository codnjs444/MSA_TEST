package com.example.SpringGateway.global.message;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;

import java.util.Locale;

/**
 * 다국어 메시지 조회를 위한 서비스 클래스
 * <p>
 * ServerWebExchange에 저장된 Locale 정보
 * 국제화 메시지 동적 반환
 */
@RequiredArgsConstructor
@Service
public class MessageService {
    private final MessageSource messageSource;

    /**
     * 인자 없는 메시지 조회
     *
     * @param code 메시지 키 (예: eUserNotFound.msg)
     * @param exchange 요청 내 Locale 정보 포함
     * @return 해당 로케일에 맞는 메시지 문자열
     */
    public String getMessage(String code, ServerWebExchange exchange) {
        Locale locale = (Locale) exchange.getAttribute(LocaleHeaderFilter.LOCALE_ATTR);
        if (locale == null) locale = Locale.KOREAN;
        return messageSource.getMessage(code, null, locale);
    }

    /**
     * 인자를 포함한 메시지 조회
     *
     * @param code 메시지 키
     * @param args 메시지 내 포맷 인자
     * @param exchange 요청 내 Locale 정보 포함
     * @return 로케일에 맞춰 인자가 포함된 메시지 문자열
     */
    public String getMessage(String code, Object[] args, ServerWebExchange exchange) {
        Locale locale = (Locale) exchange.getAttribute(LocaleHeaderFilter.LOCALE_ATTR);
        if (locale == null) locale = Locale.KOREAN;
        return messageSource.getMessage(code, args, locale);
    }
}
