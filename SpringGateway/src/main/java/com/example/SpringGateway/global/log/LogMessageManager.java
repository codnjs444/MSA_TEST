package com.example.SpringGateway.global.log;

import com.example.SpringGateway.global.log.util.LogUtils;
import lombok.extern.slf4j.Slf4j;
import com.example.SpringGateway.global.log.event.LoggingEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;

/**
 * 로그 메시지 출력 및 이벤트 발행 클래스
 *
 * - 콘솔 로그 출력(INFO, WARN, ERROR)
 * - LoggingEvent 발행 (LoggingEventListener → DB 저장)
 * - WebFlux 환경에서 ServerHttpRequest 기반 정보 추출
 */
@Slf4j
@Configuration
public class LogMessageManager implements ApplicationEventPublisherAware {

    // 이벤트 발행기 정적 변수로 보관
    private static ApplicationEventPublisher eventPublisher;

    /**
     * Spring의 ApplicationEventPublisher 주입
     * - Spring이 이 메서드를 통해 publisher를 설정함
     */
    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        LogMessageManager.eventPublisher = applicationEventPublisher;
    }

    /**
     * 로그를 콘솔에 출력하고 이벤트로 비동기 발행
     *
     * @param level    로그 레벨 (INFO, WARN, ERROR)
     * @param status   HTTP 상태
     * @param title    로그 제목
     * @param message  로그 본문
     * @param request  요청 객체
     */
    public void ConsoleLog(String level, HttpStatus status, String title, String message, ServerHttpRequest request) {
        if (request == null) return;

        String method = LogUtils.getMethod(request);
        String uri = LogUtils.getURIPath(request);
        String logMessage = String.format("[%d][%s][%s][Request Method : %s][Request URI : %s]",
                status.value(), title, message, method, uri);

        // 레벨별 로그 출력
        switch (level.toUpperCase()) {
            case "INFO" -> log.info(logMessage);
            case "ERROR" -> log.error(logMessage);
            case "WARN" -> log.warn(logMessage);
            default -> log.debug("[UNDEFINED LEVEL] " + logMessage);
        }

        // 이벤트 발행
        publishLogEvent(level.toUpperCase(), status, title, message, request);
    }

    /**
     * 로그 이벤트 발행 → DB 저장은 비동기로 처리
     *
     * @param type     로그 레벨
     * @param status   HTTP 상태 코드
     * @param title    로그 제목
     * @param message  로그 내용
     * @param request  요청 정보
     */
    private void publishLogEvent(String type, HttpStatus status, String title, String message, ServerHttpRequest request) {
        if(eventPublisher != null) {
            eventPublisher.publishEvent(
                    new LoggingEvent(
                            type,
                            status.value(),
                            LogUtils.getMethod(request),
                            LogUtils.getURIPath(request),
                            "[" + title + "] " + message,
                            LogUtils.getServerIp(),
                            LogUtils.getClientIp(request)
                    )
            );
        }
    }
}
