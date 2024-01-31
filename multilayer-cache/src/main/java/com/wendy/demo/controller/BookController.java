package com.wendy.demo.controller;

import com.wendy.demo.entity.BookVo;
import com.wendy.demo.service.BookService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description TODO
 * @Author wendyma
 * @Date 2024/1/31 21:41
 * @Version 1.0
 */
@RestController
@RequestMapping(value = "/multilayer-cache/v1/books")
public class BookController {
    @Resource
    private BookService bookService;

    @GetMapping
    public List<BookVo> queryAllBooks() {
        return bookService.queryAllBooks();
    }
}
