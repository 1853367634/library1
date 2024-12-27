package com.example.library.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.library.common.Result;
import com.example.library.dto.BorrowRequest;
import com.example.library.entity.User;
import com.example.library.enums.UserRole;
import com.example.library.service.BorrowRecordService;
import com.example.library.vo.BorrowRecordVO;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/borrow")
public class BorrowRecordController {

    @Autowired
    private BorrowRecordService borrowRecordService;

    @GetMapping("/page")
    public Result<Page<BorrowRecordVO>> page(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return Result.error("请先登录");
        }
        if (UserRole.SUPER_ADMIN.name().equals(user.getRole())) {
            return Result.error("超级管理员不能查看借阅历史");
        }
        return Result.success(borrowRecordService.pageByUserId(user.getId(), pageNum, pageSize));
    }

    @PostMapping("/borrow")
    public Result<?> borrow(@RequestBody BorrowRequest request, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return Result.error("请先登录");
        }
        if (UserRole.SUPER_ADMIN.name().equals(user.getRole())) {
            return Result.error("超级管理员不能借阅图书");
        }
        try {
            borrowRecordService.borrow(user.getId(), request.getBookId());
            return Result.success();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/return/{recordId}")
    public Result<?> returnBook(@PathVariable Long recordId, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return Result.error("请先登录");
        }
        if (UserRole.SUPER_ADMIN.name().equals(user.getRole())) {
            return Result.error("超级管理员不能归还图书");
        }
        try {
            borrowRecordService.returnBook(recordId);
            return Result.success();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
} 