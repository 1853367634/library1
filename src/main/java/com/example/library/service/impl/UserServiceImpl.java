package com.example.library.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.library.entity.User;
import com.example.library.enums.UserRole;
import com.example.library.mapper.UserMapper;
import com.example.library.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public User login(String username, String password) {
        if (!StringUtils.hasText(username) || !StringUtils.hasText(password)) {
            throw new RuntimeException("用户名或密码不能为空");
        }

        // 查询用户
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, username)
              .eq(User::getPassword, password);
        User user = getOne(wrapper);

        if (user == null) {
            throw new RuntimeException("用户名或密码错误");
        }

        return user;
    }

    @Override
    public void register(User user) {
        if (!StringUtils.hasText(user.getUsername()) || !StringUtils.hasText(user.getPassword())) {
            throw new RuntimeException("用户名或密码不能为空");
        }

        // 检查用户名是否已存在
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, user.getUsername());
        if (count(wrapper) > 0) {
            throw new RuntimeException("用户名已存在");
        }

        // 设置默认角色
        user.setRole(UserRole.USER.name());

        // 保存用户
        save(user);
    }

    @Override
    public void updateProfile(User user) {
        // 获取原用户信息
        User oldUser = getById(user.getId());
        if (oldUser == null) {
            throw new RuntimeException("用户不存在");
        }

        // 只更新基本信息
        oldUser.setPhone(user.getPhone());

        updateById(oldUser);
    }

    @Override
    public void updatePassword(Long userId, String oldPassword, String newPassword) {
        if (!StringUtils.hasText(oldPassword) || !StringUtils.hasText(newPassword)) {
            throw new RuntimeException("密码不能为空");
        }

        // 获取用户信息
        User user = getById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        // 校验旧密码
        if (!oldPassword.equals(user.getPassword())) {
            throw new RuntimeException("原密码错误");
        }

        // 更新密码
        user.setPassword(newPassword);
        updateById(user);
    }

    @Override
    public Page<User> page(Integer pageNum, Integer pageSize, String search) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(search)) {
            wrapper.like(User::getUsername, search)
                    .or()
                    .like(User::getPhone, search);
        }
        return page(new Page<>(pageNum, pageSize), wrapper);
    }

    @Override
    public User updateRole(User user) {
        // 获取原用户信息
        User oldUser = getById(user.getId());
        if (oldUser == null) {
            throw new RuntimeException("用户不存在");
        }

        // 只更新角色
        oldUser.setRole(user.getRole());
        updateById(oldUser);

        return oldUser;
    }
} 