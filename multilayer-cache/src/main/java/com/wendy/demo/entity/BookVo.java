package com.wendy.demo.entity;

import lombok.Data;

import java.util.Date;

/**
 * @Description TODO
 * @Author wendyma
 * @Date 2024/1/31 21:42
 * @Version 1.0
 */
@Data
public class BookVo {
    private long id;

    private String bookName;

    private String author;

    private String publisher;

    private Date publishedDate;
}
