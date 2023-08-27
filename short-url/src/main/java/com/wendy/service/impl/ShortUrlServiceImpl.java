package com.wendy.service.impl;

import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import com.wendy.dao.UrlMapRepository;
import com.wendy.entity.LongUrl;
import com.wendy.po.UrlMapPo;
import com.wendy.service.ShortUrlService;
import com.wendy.utils.BloomFilterUtils;
import com.wendy.utils.DecimalTo62Utils;
import com.wendy.utils.RedisClientUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.Random;

/**
 * @Description TODO
 * @Author wendyma
 * @Date 2023/8/27 14:28
 * @Version 1.0
 */
@Service
public class ShortUrlServiceImpl implements ShortUrlService {
    private final String SHORT_URL_BLOOM_FILTER = "short_url";

    @Resource
    private UrlMapRepository urlMapRepository;

    @Resource
    private BloomFilterUtils bloomFilterUtils;

    @Resource
    private RedisClientUtils redisClientUtils;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createShortUrl(LongUrl longUrl) {
        // 使用MurmurHash 生成短链
        HashFunction hashFunction = Hashing.murmur3_32_fixed();
        Long hash = hashFunction.hashString(longUrl.getLongUrl(), StandardCharsets.UTF_8).padToLong();
        String shortUrl = DecimalTo62Utils.decimalTo62(hash);

        // 校验生成的短链
        shortUrl = checkShortUrl(shortUrl);

        // 保存短链
        UrlMapPo urlMapPo = new UrlMapPo();
        urlMapPo.setShortUrl(shortUrl);
        urlMapPo.setLongUrl(longUrl.getLongUrl());
        urlMapRepository.save(urlMapPo);

        // 将短链放入Bloom过滤器
        redisClientUtils.set(shortUrl, longUrl.getLongUrl(), 300L);
        bloomFilterUtils.bfadd(SHORT_URL_BLOOM_FILTER, shortUrl);
        return shortUrl;
    }

    private String checkShortUrl(String shortUrl) {
        String newShortUrl = shortUrl;
        boolean exists = bloomFilterUtils.isExists(SHORT_URL_BLOOM_FILTER, shortUrl);
        if (!exists) {
            newShortUrl = shortUrl;
        } else {
            // 发生hash冲突，在短链后拼接一个随机大写字母
            newShortUrl = checkShortUrl(shortUrl + generateRandomChar());
        }
        return newShortUrl;
    }

    private String generateRandomChar() {
        Random random = new Random();
        int i = random.nextInt(26);
        char randomChar = (char) ('A' + i);
        return String.valueOf(randomChar);
    }

    @Override
    public String getLongUrl(String shortUrl) {
        // 先从缓存取长链值
        String longUrl;
        longUrl = redisClientUtils.get(shortUrl);
        if (StringUtils.isNotEmpty(longUrl)) {
            return longUrl;
        }

        // 从数据库获取长链值
        UrlMapPo urlMapPo = urlMapRepository.queryByShortUrl(shortUrl);
        longUrl = urlMapPo.getLongUrl();
        if (StringUtils.isEmpty(longUrl)) {
            return "this short does not exists";
        }
        redisClientUtils.set(shortUrl, longUrl, 300L);
        return longUrl;
    }
}
