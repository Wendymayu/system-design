package com.wendy.demo.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @Description TODO
 * @Author wendyma
 * @Date 2024/1/28 13:42
 * @Version 1.0
 */
public class CacheManagerImpl implements CacheManager {
    public static final CacheManager INSTANCE = new CacheManagerImpl();

    private final Map<String, MetaCache> cacheMap = new HashMap<>();

    @Override
    public MetaCache getMultiCache(String cacheName) {
        MetaCache metaCache = cacheMap.get(cacheName);
        if (Objects.nonNull(metaCache)) {
            return metaCache;
        }
        Cache<String, String> cache = createCaffeineCache();
        MultiLayerCache multiLayerCache = new MultiLayerCache(cache);
        cacheMap.put(cacheName, multiLayerCache);
        return multiLayerCache;
    }

    private Cache<String, String> createCaffeineCache() {
        return Caffeine.newBuilder()
                .expireAfterWrite(600, TimeUnit.SECONDS)
                .initialCapacity(100)
                .maximumSize(1000)
                .build();
    }
}
