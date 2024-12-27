package com.example.library.config;

import com.example.library.interceptor.LoginInterceptor;
import com.example.library.interceptor.RoleInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 登录拦截器
        registry.addInterceptor(new LoginInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/",
                        "/login",
                        "/register",
                        "/user/login",
                        "/user/register",
                        "/css/**",
                        "/js/**",
                        "/img/**",
                        "/error"
                );

        // 角色拦截器
        registry.addInterceptor(new RoleInterceptor())
                .addPathPatterns("/**");
    }
} 