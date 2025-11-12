package com.library_book.auth_service.service;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
public class RefreshTokenService {

    private final StringRedisTemplate redisTemplate;
    @Value("${jwt.refreshTtlMs}")
    private long refreshTtlMs;

    public RefreshTokenService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    private String key(String username)
    {

        return "auth:refresh:"+username;
    }


    public String create (String username)
    {
        String refresh = UUID.randomUUID().toString();
        redisTemplate.opsForValue()
                .set(key(username), refresh, java.time.Duration.ofMillis(refreshTtlMs));
        return refresh;
    }

    public boolean verify(String username, String token)
    {
        String stored = String.valueOf(redisTemplate.opsForValue().get((key(username))));
        return stored != null && stored.equals(token);
    }

    public void revoke(String username)
    {
        redisTemplate.delete(key(username));
    }

}
