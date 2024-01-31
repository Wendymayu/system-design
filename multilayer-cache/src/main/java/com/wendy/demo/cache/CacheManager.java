package com.wendy.demo.cache;

/**
 * @Description TODO
 * @Author wendyma
 * @Date 2024/1/28 13:42
 * @Version 1.0
 */
public interface CacheManager {
    MetaCache getMultiCache(String cacheName);
}