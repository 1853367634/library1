package com.example.library.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.library.entity.Book;

public interface BookService extends IService<Book> {
    
    /**
     * 分页查询图书
     */
    IPage<Book> page(Integer pageNum, Integer pageSize, String search);

    /**
     * 新增图书
     */
    void add(Book book);

    /**
     * 更新图书
     */
    void update(Book book);

    /**
     * 删除图书
     */
    void delete(Long id);
} 