package com.wendy.demo.cache;

/**
 * @Description TODO
 * @Author wendyma
 * @Date 2024/1/28 13:37
 * @Version 1.0
 */
public interface MetaCache {
    String get(String key);

    void put(String key, String value);
}