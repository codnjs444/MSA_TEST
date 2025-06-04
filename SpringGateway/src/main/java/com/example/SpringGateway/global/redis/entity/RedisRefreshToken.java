package com.example.SpringGateway.global.redis.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

/**
 * Refresh Token 관리
 * TTL : 1일 (Keycloak 만료시간과 일치)
 * 
 * [예시] 
 * KEY : rt:{userUUID}
 * VALEU : token
 */

@RedisHash(value = "rt")
@NoArgsConstructor
@Getter
public class RedisRefreshToken {

	@Id
    private String userUUID;

    private String token;

    @TimeToLive
    private long ttl;

    public RedisRefreshToken(String userUUID, String token, long ttl) {
        this.userUUID = userUUID;
        this.token = token;
        this.ttl = ttl;
    }
}
