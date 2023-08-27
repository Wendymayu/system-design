package com.wendy.dao;

import com.wendy.po.UrlMapPo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * @Description TODO
 * @Author wendyma
 * @Date 2023/8/27 14:33
 * @Version 1.0
 */
@Repository
public interface UrlMapRepository extends CrudRepository<UrlMapPo, String> {
    UrlMapPo queryByShortUrl(@Param("shortUrl") String shortUrl);
}