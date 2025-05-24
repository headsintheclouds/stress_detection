package com.example.stress_detection.filter;


import com.example.stress_detection.utils.JwtUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName = "jwtAuthFilter", urlPatterns = "/auth/*")
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String  HEADER_STRING = "Authorization";
    private static final String  PREFIX = "Bearer ";


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 排除 /auth/login 路径，不进行 JWT 处理
        String uri = request.getRequestURI();

        if (uri.equals("/auth/login")) {
            filterChain.doFilter(request, response);
            return;
        }
        String header = request.getHeader(HEADER_STRING);
        System.out.println("这是在 doFilterInternal doFilterInternal doFilterInternal doFilterInternal 内部");
        if(header != null && header.startsWith(PREFIX)) {
            String token = header.substring(PREFIX.length());

            if (!JwtUtil.isJWTExpired(token)) {
                String userId = JwtUtil.getUserIdFromJWT(token);


                // 设置身份验证（通常是从数据库获取用户信息并验证角色等）
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userId, null, null);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        System.out.println("离开 离开 doFilterInternal doFilterInternal doFilterInternal doFilterInternal 内部");

        filterChain.doFilter(request, response);
    }
}
