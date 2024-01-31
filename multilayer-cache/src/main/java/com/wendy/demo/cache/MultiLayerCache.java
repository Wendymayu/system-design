package com.wendy.demo.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.wendy.demo.utils.BeanUtils;

import java.util.Objects;

/**
 * @Description TODO
 * @Author wendyma
 * @Date 2024/1/28 13:40
 * @Version 1.0
 */
public class MultiLayerCache implements MetaCache {
    private CaffeineCache caffineCache;

    private RedisCache redisCache;

    public MultiLayerCache(Cache cache) {
        this.caffineCache = new CaffeineCache(cache);
    }

    private void initRedisCache(){
        this.redisCache = BeanUtils.getBean(RedisCache.class);
    }

    @Override
    public String get(String key) {
        String value;
        value = caffineCache.get(key);
        if (Objects.nonNull(value)) {
            return value;
        }

        initRedisCache();
        value = redisCache.get(key);
        if (Objects.nonNull(value)) {
            return value;
        }
        return value;
    }

    @Override
    public void put(String key, String value) {
        caffineCache.put(key, value);
        System.out.println("put key-value in caffeine");

        redisCache.put(key, value);
        System.out.println("put key-value in redis");
    }
}
