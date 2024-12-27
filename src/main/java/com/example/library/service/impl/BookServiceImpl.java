package com.example.library.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.library.entity.Book;
import com.example.library.mapper.BookMapper;
import com.example.library.service.BookService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class BookServiceImpl extends ServiceImpl<BookMapper, Book> implements BookService {

    @Override
    public IPage<Book> page(Integer pageNum, Integer pageSize, String search) {
        LambdaQueryWrapper<Book> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(search)) {
            queryWrapper.like(Book::getName, search)
                    .or()
                    .like(Book::getAuthor, search)
                    .or()
                    .like(Book::getPublisher, search)
                    .or()
                    .like(Book::getCategory, search);
        }
        queryWrapper.orderByDesc(Book::getCreateTime);
        return this.page(new Page<>(pageNum, pageSize), queryWrapper);
    }

    @Override
    public void add(Book book) {
        this.save(book);
    }

    @Override
    public void update(Book book) {
        this.updateById(book);
    }

    @Override
    public void delete(Long id) {
        this.removeById(id);
    }
} 