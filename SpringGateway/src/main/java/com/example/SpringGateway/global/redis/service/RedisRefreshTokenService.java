package com.example.SpringGateway.global.redis.service;


import com.example.SpringGateway.global.redis.entity.RedisRefreshToken;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RedisRefreshTokenService {

	private final ReactiveRedisTemplate<String, Object> reactiveRedisTemplate;
	
	private static final String PREFIX = "rt:";

    /**
     * RefreshToken 저장 (Hash 구조, TTL 포함)
     * @param token RedisRefreshToken 객체
     * @param ttlSeconds 초 단위 TTL
     */
    public Mono<Boolean> saveWithExpire(RedisRefreshToken token) {
        String redisKey = PREFIX + token.getUserUUID();

        return reactiveRedisTemplate.<String, String>opsForHash()
            .put(redisKey, "refreshToken", token.getToken())
            .flatMap(result -> reactiveRedisTemplate.expire(redisKey, Duration.ofSeconds(token.getTtl())));
    }
	
    /**
     * RefreshToken 삭제
     */
    public Mono<Boolean> deleteByUid(String uid) {
        return reactiveRedisTemplate.delete(PREFIX + uid)
        		.map(count -> count > 0);
    }

    /**
     * RefreshToken 조회
     */
    public Mono<Object> findByUid(String uid) {
        return reactiveRedisTemplate.opsForHash().get(PREFIX + uid, "refreshToken");
    }
}
