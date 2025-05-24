package com.example.stress_detection.interceptor;

import com.example.stress_detection.utils.JwtUtil;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JwtAuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("这是在JwtAuthInterceptor JwtAuthInterceptor JwtAuthInterceptor JwtAuthInterceptor preHandle preHandle preHandle preHandle 内部 -------");
        String token = request.getHeader("Authorization");
        if (token == null || JwtUtil.isJWTExpired(token)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Token 无效或已过期");
            return false; // 阻止继续执行 Controller
        }

        return true;
    }
}
