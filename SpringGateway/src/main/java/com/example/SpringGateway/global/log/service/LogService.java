package com.example.SpringGateway.global.log.service;


import com.example.SpringGateway.global.exception.custom.CCommonException;
import com.example.SpringGateway.global.log.entity.LogEntity;
import com.example.SpringGateway.global.log.entity.LogProblemEntity;
import com.example.SpringGateway.global.log.event.LoggingEvent;
import com.example.SpringGateway.global.log.repository.LogProblemRepository;
import com.example.SpringGateway.global.log.repository.LogRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

/**
 * 로그 저장 서비스
 *
 * - LoggingEventListener로부터 이벤트를 수신하여 DB에 로그 저장 처리
 * - INFO 로그는 tb_gateway_log, 문제 로그는 tb_gateway_log_problem 테이블에 저장
 */
@RequiredArgsConstructor
@Service
public class LogService {

	// 서버의 식별자
	@Value("${spring.application.name}")
	private String SERVER_NAME;

	private final LogRepository repository;
	private final LogProblemRepository problemRepository;

	/**
	 * 정상 요청 로그 저장
	 *
	 * @param loggingEvent WebFilter 또는 예외 핸들러에서 발행한 이벤트 객체
	 */
	@Transactional
	public void insertLogging(LoggingEvent loggingEvent) {
		try {
			repository.save(
					new LogEntity(
							loggingEvent.getLogType(),
							loggingEvent.getStatus(),
							loggingEvent.getMethod(),
							loggingEvent.getApiUrl(),
							loggingEvent.getPayload(),
							loggingEvent.getServerIp(),
							loggingEvent.getClientIp(),
							SERVER_NAME
					)
			);
		}catch (DataAccessException e) {
			throw new CCommonException(HttpStatus.INTERNAL_SERVER_ERROR, "eDataAccessFail", "LOG(INFO) SAVE FAILED", e.getMessage(), e);
		}
	}

	/**
	 * 예외 또는 비정상 요청 로그 저장
	 *
	 * @param loggingEvent WebFilter 또는 예외 핸들러에서 발행한 이벤트 객체
	 */
	@Transactional
	public void insertProblemLogging(LoggingEvent loggingEvent) {
		try {
			problemRepository.save(
					new LogProblemEntity(
							loggingEvent.getLogType(),
							loggingEvent.getStatus(),
							loggingEvent.getMethod(),
							loggingEvent.getApiUrl(),
							loggingEvent.getPayload(),
							loggingEvent.getServerIp(),
							loggingEvent.getClientIp(),
							SERVER_NAME
					)
			);
		}catch (DataAccessException e) {
			throw new CCommonException(HttpStatus.INTERNAL_SERVER_ERROR, "eDataAccessFail", "LOG(PROBLEM) SAVE FAILED", e.getMessage(), e);
		}
	}
}
