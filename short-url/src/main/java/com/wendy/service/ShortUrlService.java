package com.wendy.service;

import com.wendy.entity.LongUrl;

/**
 * @Description TODO
 * @Author wendyma
 * @Date 2023/8/27 14:25
 * @Version 1.0
 */
public interface ShortUrlService {
    /**
     *
     * @param longUrl 长链
     * @return 生成的短链
     */
    String createShortUrl(LongUrl longUrl);

    /**
     *
     * @param shortUrl 短链
     * @return 短链对应的长链
     */
    String getLongUrl(String shortUrl);
}