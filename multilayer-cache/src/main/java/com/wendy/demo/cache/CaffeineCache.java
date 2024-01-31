package com.wendy.demo.cache;

import com.github.benmanes.caffeine.cache.Cache;

/**
 * @Description TODO
 * @Author wendyma
 * @Date 2024/1/28 13:38
 * @Version 1.0
 */
public class CaffeineCache implements MetaCache {
    private Cache<String, String> cache;

    public CaffeineCache(Cache<String, String> cache) {
        this.cache = cache;
    }

    @Override
    public String get(String key) {
        return cache.getIfPresent(key);
    }

    @Override
    public void put(String key, String value) {
        cache.put(key, value);
    }
}
