package com.wendy.demo.cache.business;

import com.fasterxml.jackson.core.type.TypeReference;
import com.wendy.demo.cache.CacheManagerImpl;
import com.wendy.demo.cache.CacheNames;
import com.wendy.demo.cache.MetaCache;
import com.wendy.demo.dao.BookDao;
import com.wendy.demo.dao.BookPo;
import com.wendy.demo.entity.BookVo;
import com.wendy.demo.utils.JsonUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @Description TODO
 * @Author wendyma
 * @Date 2024/1/31 20:48
 * @Version 1.0
 */
@Component
public class BookCache {
    private MetaCache multiCache = CacheManagerImpl.INSTANCE.getMultiCache(CacheNames.BOOK_CACHE);

    @Resource
    private BookDao bookDao;

    public List<BookVo> queryAllBooks() {
        String cacheKey = "queryAllBooks";
        String value = multiCache.get(cacheKey);
        if (Objects.nonNull(value)) {
            TypeReference<List<BookVo>> typeReference = new TypeReference<>(){};
            List<BookVo> bookVos = JsonUtils.json2Object(value, typeReference);
            return bookVos;
        }
        List<BookPo> bookPos = bookDao.queryAllBooks();
        List<BookVo> bookVos = bookPos.stream().map(po -> {
            BookVo vo = new BookVo();
            BeanUtils.copyProperties(po, vo);
            return vo;
        }).collect(Collectors.toList());
        multiCache.put(cacheKey, JsonUtils.toJson(bookVos));
        return bookVos;
    }
}
