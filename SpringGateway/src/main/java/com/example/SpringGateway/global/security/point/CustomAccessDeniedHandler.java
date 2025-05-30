package com.example.SpringGateway.global.security.point;

import com.example.SpringGateway.global.log.LogMessageManager;
import com.example.SpringGateway.global.message.MessageService;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

/**
 * [AccessDeniedHandler]
 *
 * 인증된 사용자(로그인 완료)는 맞지만,
 * 해당 리소스에 접근 권한이 없는 경우 (403 Forbidden)
 * 사용자에게 메시지 응답 + 로그 처리
 */
@Component
@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements ServerAccessDeniedHandler {

    private final MessageService messageService;
    private final LogMessageManager logMessageManager;

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, AccessDeniedException ex) {

        System.err.println("5555555555555555555555");

        String i18nKey = "accessDenied";

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
                HttpStatus.FORBIDDEN,
                "ACCESS_DENIED",
                ex.getMessage(),
                exchange.getRequest()
        );

        // JSON 응답 반환
        byte[] bytes = jsonResponse.toString().getBytes(StandardCharsets.UTF_8);
        var response = exchange.getResponse();
        response.setStatusCode(HttpStatus.FORBIDDEN);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        response.getHeaders().setContentLength(bytes.length);

        return response.writeWith(Mono.just(response.bufferFactory().wrap(bytes)));
    }
}
