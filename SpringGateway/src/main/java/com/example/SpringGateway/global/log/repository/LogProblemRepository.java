package com.example.SpringGateway.global.log.repository;


import com.example.SpringGateway.global.log.entity.LogEntity;
import com.example.SpringGateway.global.log.entity.LogProblemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * 예외 로그 저장소
 *
 * - 예외 발생 시 LoggingEventListener를 통해 비동기로 저장
 * - UUIDv7을 기반으로 고유 로그 추적 가능
 */
@Repository
public interface LogProblemRepository extends JpaRepository<LogProblemEntity, UUID> {

	List<LogEntity> findAllByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
	
}
