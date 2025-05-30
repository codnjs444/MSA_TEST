package com.example.SpringGateway.global.message;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Locale;
import java.util.Optional;

/**
 * WebFlux 기반 다국어 메시지 처리를 위한 Locale 설정 필터
 *
 * HTTP 요청 헤더에서 언어 정보를 읽어 Locale 객체로 변환
 * 이후 메시지 소스(MessageSource)가 해당 Locale을 참조할 수 있도록 ServerWebExchange의 Attribute에 저장
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE) // 모든 WebFilter 중 가장 먼저 실행되도록 설정
public class LocaleHeaderFilter implements WebFilter {

    // WebExchange Attribute에 저장될 Locale 키 값
    public static final String LOCALE_ATTR = "LOCALE";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        // [1] 우선순위: X-LANG → Accept-Language → default(KOREAN)
        String lang = Optional.ofNullable(request.getHeaders().getFirst("X-LANG"))
                .orElse(request.getHeaders().getFirst("Accept-Language"));
        // [2] 유효한 언어 코드가 없을 경우 기본값(KOREAN)으로 설정
        Locale locale = lang != null ? Locale.forLanguageTag(lang) : Locale.KOREAN;

        // [3] 필터 체인 내 다른 컴포넌트가 참조할 수 있도록 Locale을 요청 컨텍스트에 저장
        exchange.getAttributes().put(LOCALE_ATTR, locale);

        // [4] 필터 체인 계속 진행
        return chain.filter(exchange);
    }
}
