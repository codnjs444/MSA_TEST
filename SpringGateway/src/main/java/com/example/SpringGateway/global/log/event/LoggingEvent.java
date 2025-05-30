package com.example.SpringGateway.global.log.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 로그 수집용 도메인 이벤트 객체
 *
 * WebFilter 등에서 발생한 요청/응답 정보를 이벤트 객체로 캡슐화
 * 비동기 이벤트 핸들러(LoggingEventListener)로 전달
 */
@Getter
@RequiredArgsConstructor
public class LoggingEvent {

    private final String logType;	// 로그 레벨 (INFO, ERROR, WARN 등)
    private final int status;		// HTTP 상태 코드
    private final String method;	// HTTP 메서드 (GET, POST 등)
    private final String apiUrl;	// 요청한 API 경로
    private final String payload;	// 요청 본문 (필요 시 확장)
    private final String serverIp;	// 처리 서버의 IP 주소
    private final String clientIp;	// 클라이언트 IP 주소

}