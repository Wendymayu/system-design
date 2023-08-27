package com.wendy.controller;

import com.wendy.entity.LongUrl;
import com.wendy.service.ShortUrlService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @Description TODO
 * @Author wendyma
 * @Date 2023/8/27 12:15
 * @Version 1.0
 */
@RequestMapping("/v1/short-url")
@RestController
public class ShortUrlController {
    @Resource
    private ShortUrlService shortUrlService;

    @PostMapping
    public String createShortUrl(@RequestBody LongUrl longUrl) {
        return shortUrlService.createShortUrl(longUrl);
    }

    @GetMapping
    public String getLongUrl(@RequestParam("shortUrl") String shortUrl) {
        return shortUrlService.getLongUrl(shortUrl);
    }
}
