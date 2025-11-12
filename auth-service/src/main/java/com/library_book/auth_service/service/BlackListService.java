package com.library_book.auth_service.service;

import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class BlackListService {

    private final StringRedisTemplate redisTemplate;


    public BlackListService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void addToBlackList(String token, Long ttlMs)
    {
        redisTemplate.opsForValue().set("auth:blacklist" + token, "true", ttlMs);

    }


}
