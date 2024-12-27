package com.example.library.interceptor;

import com.example.library.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 检查是否已登录
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            // 如果是AJAX请求，返回401状态码
            String xhr = request.getHeader("X-Requested-With");
            if ("XMLHttpRequest".equals(xhr)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return false;
            }
            // 如果是普通请求，重定向到登录页
            response.sendRedirect("/login");
            return false;
        }
        return true;
    }
} 