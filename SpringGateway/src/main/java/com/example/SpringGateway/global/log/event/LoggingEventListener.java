package com.example.SpringGateway.global.log.event;

import com.example.SpringGateway.global.log.service.LogService;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * LoggingEvent 이벤트 리스너
 *
 * WebFilter 또는 기타 컴포넌트에서 발생한 LoggingEvent를 비동기(@Async)로 수신
 * 로그 데이터를 LogService를 통해 DB에 저장
 */
@Component
public class LoggingEventListener {
	
	private final LogService service;
	
	public LoggingEventListener(LogService service) {
        this.service = service;
    }

	/**
	 * LoggingEvent 발생 시 비동기적으로 처리
	 * - INFO 로그는 일반 요청 로그 테이블에 저장
	 * - 그 외(WARN, ERROR 등)는 별도 테이블 또는 에러 로그로 저장
	 *
	 * @param loggingEvent 로그 정보 이벤트
	 */
	@Async
	@EventListener
	public void handleLoggingEvent(LoggingEvent loggingEvent) {
		if(loggingEvent.getLogType().equals("INFO")) {
			service.insertLogging(loggingEvent);
		} else {
			service.insertProblemLogging(loggingEvent);
		}
	}
}