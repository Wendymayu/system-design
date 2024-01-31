package com.wendy.demo.cache;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @Description TODO
 * @Author wendyma
 * @Date 2024/1/28 13:39
 * @Version 1.0
 */
@Component
public class RedisCache implements MetaCache {
    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public String get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public void put(String key, String value) {
        redisTemplate.opsForValue().set(key, value, 300, TimeUnit.SECONDS);
    }
}
