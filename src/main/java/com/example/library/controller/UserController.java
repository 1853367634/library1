package com.example.library.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.library.annotation.RequireRole;
import com.example.library.common.Result;
import com.example.library.entity.User;
import com.example.library.enums.UserRole;
import com.example.library.service.UserService;
import com.example.library.service.BorrowRecordService;
import com.example.library.vo.BorrowRecordVO;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private BorrowRecordService borrowRecordService;

    @PostMapping("/login")
    public Result<User> login(@RequestBody User user, HttpSession session) {
        User loginUser = userService.login(user.getUsername(), user.getPassword());
        session.setAttribute("user", loginUser);
        return Result.success(loginUser);
    }

    @PostMapping("/register")
    public Result<User> register(@RequestBody User user) {
        userService.register(user);
        return Result.success(user);
    }

    @PostMapping("/logout")
    public Result<?> logout(HttpSession session) {
        session.invalidate();
        return Result.success();
    }

    @GetMapping("/info")
    public Result<User> info(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return Result.error("请先登录");
        }
        return Result.success(userService.getById(user.getId()));
    }

    @PostMapping("/update")
    public Result<?> update(@RequestBody User user, HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            return Result.error("请先登录");
        }

        // 只能修改自己的基本信息
        if (!user.getId().equals(currentUser.getId())) {
            return Result.error("只能修改自己的信息");
        }

        userService.updateProfile(user);
        // 更新session中的用户信息
        session.setAttribute("user", userService.getById(user.getId()));
        return Result.success();
    }

    @PostMapping("/update-password")
    public Result<?> updatePassword(@RequestBody Map<String, String> params, HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            return Result.error("请先登录");
        }

        String oldPassword = params.get("oldPassword");
        String newPassword = params.get("password");

        try {
            userService.updatePassword(currentUser.getId(), oldPassword, newPassword);
            session.invalidate();
            return Result.success();
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/page")
    @RequireRole({UserRole.ADMIN, UserRole.SUPER_ADMIN})
    public Result<Page<User>> page(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String search) {
        return Result.success(userService.page(pageNum, pageSize, search));
    }

    @GetMapping("/{id}")
    @RequireRole({UserRole.ADMIN, UserRole.SUPER_ADMIN})
    public Result<User> getById(@PathVariable Long id) {
        return Result.success(userService.getById(id));
    }

    @GetMapping("/borrow-history/{userId}")
    @RequireRole({UserRole.ADMIN})
    public Result<List<BorrowRecordVO>> borrowHistory(@PathVariable Long userId, HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        if (UserRole.SUPER_ADMIN.name().equals(currentUser.getRole())) {
            return Result.error("超级管理员不能查看借阅历史");
        }
        return Result.success(borrowRecordService.listByUserId(userId));
    }

    @PostMapping("/update-role")
    @RequireRole(UserRole.SUPER_ADMIN)
    public Result<?> updateRole(@RequestBody User user, HttpSession session) {
        // 不允许修改自己的角色
        User currentUser = (User) session.getAttribute("user");
        if (currentUser.getId().equals(user.getId())) {
            return Result.error("不能修改自己的角色");
        }

        // 获取目标用户当前的角色
        User targetUser = userService.getById(user.getId());
        if (targetUser == null) {
            return Result.error("用户不存在");
        }

        // 不允许修改超级管理员的角色
        if (UserRole.SUPER_ADMIN.name().equals(targetUser.getRole())) {
            return Result.error("不能修改超级管理员的角色");
        }

        // 不允许将用户设置为超级管理员
        if (UserRole.SUPER_ADMIN.name().equals(user.getRole())) {
            return Result.error("不能将用户设置为超级管理员");
        }

        // 执行角色更新
        User updatedUser = userService.updateRole(user);
        return Result.success(updatedUser);
    }
} 