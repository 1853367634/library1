package com.example.library.interceptor;

import com.example.library.annotation.RequireRole;
import com.example.library.entity.User;
import com.example.library.enums.UserRole;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Arrays;

public class RoleInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        RequireRole requireRole = handlerMethod.getMethodAnnotation(RequireRole.class);
        if (requireRole == null) {
            return true;
        }

        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        // 超级管理员拥有所有权限（除了特殊限制的接口）
        if (UserRole.SUPER_ADMIN.name().equals(user.getRole())) {
            String requestURI = request.getRequestURI();
            // 排除特定的接口（如借阅相关）
            if (!requestURI.contains("/borrow")) {
                return true;
            }
        }

        // 检查用户是否具有所需角色之一
        boolean hasRole = Arrays.stream(requireRole.value())
                .anyMatch(role -> role.name().equals(user.getRole()));

        if (!hasRole) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return false;
        }

        return true;
    }
} 