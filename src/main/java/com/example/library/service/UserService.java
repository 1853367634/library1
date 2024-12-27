package com.example.library.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.library.entity.User;

public interface UserService extends IService<User> {
    User login(String username, String password);
    void register(User user);
    void updateProfile(User user);
    void updatePassword(Long userId, String oldPassword, String newPassword);
    Page<User> page(Integer pageNum, Integer pageSize, String search);
    User updateRole(User user);
} 