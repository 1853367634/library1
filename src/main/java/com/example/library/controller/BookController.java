package com.example.library.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.library.annotation.RequireRole;
import com.example.library.common.Result;
import com.example.library.entity.Book;
import com.example.library.enums.UserRole;
import com.example.library.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/book")
public class BookController {

    @Autowired
    private BookService bookService;

    /**
     * 分页查询图书
     */
    @GetMapping("/page")
    public Result<IPage<Book>> page(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String search) {
        return Result.success(bookService.page(pageNum, pageSize, search));
    }

    /**
     * 获取单个图书信息
     */
    @GetMapping("/{id}")
    @RequireRole({UserRole.ADMIN, UserRole.SUPER_ADMIN})
    public Result<Book> getById(@PathVariable Long id) {
        return Result.success(bookService.getById(id));
    }

    /**
     * 新增或更新图书
     */
    @PostMapping("/save")
    @RequireRole({UserRole.ADMIN, UserRole.SUPER_ADMIN})
    public Result<?> save(@RequestBody Book book) {
        if (book.getId() == null) {
            bookService.add(book);
        } else {
            bookService.update(book);
        }
        return Result.success();
    }

    /**
     * 删除图书
     */
    @DeleteMapping("/{id}")
    @RequireRole({UserRole.ADMIN, UserRole.SUPER_ADMIN})
    public Result<?> delete(@PathVariable Long id) {
        bookService.delete(id);
        return Result.success();
    }
} 