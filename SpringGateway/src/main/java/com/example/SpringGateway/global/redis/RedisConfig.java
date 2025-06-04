package com.example.SpringGateway.global.redis;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;

/**
 * Redis 설정 클래스 (Reactive 방식)
 * 
 * - Lettuce 기반 Reactive Redis 연결 구성
 * - 기본 Key/Value 직렬화: String → 문자열로 단순 저장
 * - Repository 자동 스캔 범위 설정
 */
@Configuration
@EnableRedisRepositories(basePackages = "kr.co.igns.global.redis.redisRepository")
public class RedisConfig {

	// Redis 서버 호스트 주소
	@Value("${spring.data.redis.host}")
	private String host;
	
	// Redis 서버 포트 번호
	@Value("${spring.data.redis.port}")
	private int port;
	
	/**
     * Reactive Redis 연결 팩토리 설정
     * - Lettuce는 비동기/논블로킹 방식으로 WebFlux와 호환
     */
	@Bean
	@Primary
	ReactiveRedisConnectionFactory reactiveRedisConnectionFactory() {
		return new LettuceConnectionFactory(host, port);
	}
	
	/**
     * Reactive RedisTemplate 설정
     * - 기본 직렬화 방식: String ↔ Object
     * - 모든 Key, Value를 문자열로 처리
     */
	@Bean
	ReactiveRedisTemplate<String, Object> reactiveRedisTemplate(@Qualifier("reactiveRedisConnectionFactory") ReactiveRedisConnectionFactory factory) {
		RedisSerializationContext<String, Object> context = RedisSerializationContext
                .<String, Object>newSerializationContext(new StringRedisSerializer())	// Key Serializer
                .value(new GenericJackson2JsonRedisSerializer())						// Value Serializer
                .build();
        return new ReactiveRedisTemplate<>(factory, context);
    }
}
