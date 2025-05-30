package com.example.SpringGateway.global.log.repository;


import com.example.SpringGateway.global.log.entity.LogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * 정상 로그 저장소
 *
 * - UUIDv7을 기반으로 로그 식별
 * - WebFilter에서 수집된 정상 요청 데이터를 저장
 */
@Repository
public interface LogRepository extends JpaRepository<LogEntity, UUID> {

	List<LogEntity> findAllByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
	
}
