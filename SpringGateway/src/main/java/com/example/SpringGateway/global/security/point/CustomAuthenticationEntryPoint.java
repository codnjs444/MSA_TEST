package com.example.SpringGateway.global.security.point;


import com.example.SpringGateway.global.log.LogMessageManager;
import com.example.SpringGateway.global.message.MessageService;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

/**
 * [AuthenticationEntryPoint]
 *
 * 인증되지 않은 사용자가 인증이 필요한 리소스에 접근할 경우
 * 401 Unauthorized 상태 코드와 함께 JSON 형태의 오류 메시지를 반환
 */
@Component
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements ServerAuthenticationEntryPoint {

    private final MessageService messageService;
    private final LogMessageManager logMessageManager;

    @Override
    public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException ex) {

        System.err.println("4444444444444444444444444444");

        String i18nKey = "unauthorized";

        // 다국어 메시지 추출
        String code = messageService.getMessage(String.format("%s.code", i18nKey), exchange);
        String msg = messageService.getMessage(String.format("%s.msg", i18nKey), exchange);

        // 응답 JSON 구성
        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("success", false);
        jsonResponse.put("code", code);
        jsonResponse.put("msg", msg);

        // 로그 기록 및 DB 저장 (LogMessageManager 내부에서 처리)
        logMessageManager.ConsoleLog(
                "ERROR",
                HttpStatus.UNAUTHORIZED,
                "UNAUTHORIZED",
                ex.getMessage(),
                exchange.getRequest()
        );

        // JSON 응답 반환
        byte[] bytes = jsonResponse.toString().getBytes(StandardCharsets.UTF_8);
        var response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        response.getHeaders().setContentLength(bytes.length);

        return response.writeWith(Mono.just(response.bufferFactory().wrap(bytes)));
    }
}
