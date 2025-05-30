package com.example.SpringGateway.global.log.entity;

import com.example.SpringGateway.global.base.UUIDv7Generator;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * API 호출 실패/예외 로그 Entity
 *
 * - 예외 상황에서 발행된 LoggingEvent 기반으로 DB 저장
 * - 에러 상태 코드, 에러 메시지 중심의 저장 구조
 */
@Getter
@Entity
@Builder
@DynamicInsert
@DynamicUpdate
@Table(name = "tb_gateway_log_problem")
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LogProblemEntity {

	@Id	
	@Comment("로그 ID")
	@Column(name = "UUID", updatable = false, nullable = false)
	private UUID id;
	
	@NotNull
	@Column(name = "log_type", length = 15)
	@Comment("로그 종류 (ERROR, WARN 등)")
    private String logType;
	
	@NotNull
	@Column(name = "method", length = 100)
	@Comment("HTTP 메서드")
    private String method;
	
	@NotNull
	@Column(name = "api_url", length = 100)
	@Comment("API 요청 경로")
    private String apiUrl;
	
	@Column(name = "payload", columnDefinition = "TEXT")
	@Comment("에러 메시지 또는 요청 정보")
    private String payload;
	
	@NotNull
	@Column(name = "status")
	@Comment("에러 상태 코드 (예: 400, 500 등)")
    private int status;
	
	@Column(name = "server_ip", length = 30)
	@Comment("요청을 처리한 서버 IP")
    private String serverIp;
	
	@Column(name = "client_ip", length = 30)
	@Comment("클라이언트 요청 IP")
    private String clientIp;
	
	@NotNull
	@Column(name = "server_type", length = 30)
	@Comment("서버 구분 (gateway, backend 등)")
    private String serverType;
	
	@NotNull
	@Comment("로그 생성 시각")
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;
	
	@PrePersist
	public void assignUUID() {
	    if (this.id == null) {
	        this.id = UUIDv7Generator.generate();
	    }
	}
	
	@Builder
	public LogProblemEntity(String logType, int status, String method, String apiUrl, String payload, String serverIp, String clientIp, String serverType) {
		this.logType = logType;
		this.status = status;
		this.method = method;
		this.apiUrl = apiUrl;
		this.payload = payload;
		this.serverIp = serverIp;
		this.clientIp = clientIp;
		this.serverType = serverType;
	}
}