package com.wendy.demo.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Description TODO
 * @Author wendyma
 * @Date 2024/1/31 21:42
 * @Version 1.0
 */
@Repository
public interface BookDao extends CrudRepository<BookPo, Long> {
    @Query(value = "select po from BookPo as po")
    List<BookPo> queryAllBooks();
}
