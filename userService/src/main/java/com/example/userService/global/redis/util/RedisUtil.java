package com.example.userService.global.redis.util;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;

@Service
@RequiredArgsConstructor 
public class RedisUtil {


   private final RedisTemplate<String, String> redisHashTemplate;
    	  
   public void saveOtpToken(String key, String otp, String secretkey, int duration) {
	   redisHashTemplate.opsForHash().put(key, "otp", otp);
	   redisHashTemplate.opsForHash().put(key, "secretkey", secretkey);
	   redisHashTemplate.expire(key, Duration.ofSeconds(duration));
   }
   
   public Map<Object, Object> getOtpToken(String key) {
	   return redisHashTemplate.opsForHash().entries(key);
   }
   
   public void deleteToken(String key) {
	   redisHashTemplate.delete(key);
   }
   

   public void saveAccessToken(String key, String accessToken, int duration) {
	   redisHashTemplate.opsForHash().put(key, "accessToken", accessToken);
	   redisHashTemplate.expire(key, Duration.ofSeconds(duration));
   }

}
