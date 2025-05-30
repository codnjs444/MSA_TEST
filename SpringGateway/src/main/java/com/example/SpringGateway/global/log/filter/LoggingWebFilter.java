package com.example.SpringGateway.global.log.filter;


import com.example.SpringGateway.global.log.event.LoggingEvent;
import com.example.SpringGateway.global.log.util.LogUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * WebFlux 기반 전역 로깅 필터
 * 
 * - 요청(Request) 및 응답(Response)의 메타데이터를 로그로 출력
 * - 비즈니스 로직과 분리하여 로그 저장을 이벤트 기반으로 처리
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class LoggingWebFilter implements WebFilter {

    private final ApplicationEventPublisher eventPublisher;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        
        // [요청 정보 추출]
        String method = LogUtils.getMethod(request);		// HTTP Method
        String uriPath = LogUtils.getURIPath(request);		// 요청 경로
        String serverIp = LogUtils.getServerIp();			// 서버 IP
        String clientIp = LogUtils.getClientIp(request);	// 클라이언트 IP

        // [요청 로그 출력]
        log.info(
                "[REQUEST][{} {}, ServerIP: {}, ClientIP: {}]",               
                method,
                uriPath,
                serverIp,
                clientIp
        );

        // [요청 이벤트 발행 － 제외 경로 건너뜀]
        if (!isExcludedPath(uriPath)) {
        	eventPublisher.publishEvent(
                    new LoggingEvent(
                        "INFO",
                        0,
                        method,
                        uriPath,
                        "",			// Payload 추후 확장
                        serverIp,
                        clientIp
                    )
                );
        }
        
        
        // [응답 후 후처리]
        return chain.filter(exchange)
            .doOnSuccess(done -> {
                log.info("[RESPONSE] {} {} -> status {}", method, uriPath, response.getStatusCode());

                // [응답 이벤트 발행 - 제외 경로는 건너뜀]
                if (!isExcludedPath(uriPath)) {
                	eventPublisher.publishEvent(
                            new LoggingEvent(
                                "INFO",
                                response.getStatusCode() != null ? response.getStatusCode().value() : 200,
                                method,
                                uriPath,
                                "",
                                serverIp,
                                clientIp
                            )
                        );
                }
            });
    }
    
    // 제외 경로 목록
    private boolean isExcludedPath(String path) {
        return path.equals("/favicon.ico")
            || path.equals("/swagger-ui.html")
            || path.startsWith("/api-docs")
            || path.startsWith("/webjars/swagger-ui");
    }
}
