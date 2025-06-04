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
 * API 정상 호출 로그 Entity
 *
 * - Gateway → WebFilter에서 발행된 LoggingEvent 기반으로 DB 저장
 * - 요청 메타정보와 처리 상태 정보를 기록
 */
@Getter
@Entity
@Builder
@DynamicInsert
@DynamicUpdate
@Table(name = "tb_gateway_log")
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LogEntity {

	@Id	
	@Comment("로그 ID")
	@Column(name = "UUID", updatable = false, nullable = false)
	private UUID id;
	
	@NotNull
	@Column(name = "log_type", length = 15)
	@Comment("로그 종류 (INFO 등)")
    private String logType;
	
	@NotNull
	@Column(name = "method", length = 100)
	@Comment("HTTP 메서드 (GET, POST 등)")
    private String method;
	
	@NotNull
	@Column(name = "api_url", length = 100)
	@Comment("요청한 API 경로")
    private String apiUrl;
	
	@Column(name = "payload", columnDefinition = "TEXT")
	@Comment("요청 본문 또는 메타데이터")
    private String payload;
	
	@NotNull
	@Column(name = "status")
	@Comment("HTTP 응답 코드 (예: 200, 201 등)")
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
	
//	@NotNull
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
	public LogEntity(String logType, int status, String method, String apiUrl, String payload, String serverIp, String clientIp, String serverType) {
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
