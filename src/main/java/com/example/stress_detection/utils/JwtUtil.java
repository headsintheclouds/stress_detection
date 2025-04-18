package com.example.stress_detection.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

public class JwtUtil {
    private static final String SECRET_KEY = "your_secret_key";
    private static final long EXPIRATION_TIME = 86400000; // JWT 过期时间，默认1天

    //生成jwt
    public static String createJWT(Map<String, Object> claims) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + EXPIRATION_TIME);

        JwtBuilder jwtBuilder = Jwts.builder()
                .setClaims(claims)
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY);

        return jwtBuilder.compact();
    }

    public static Claims parseJWT(String jwt) {
        Claims claims = Jwts.parser()
                //设置需要签名的密钥
                .setSigningKey(jwt.getBytes(StandardCharsets.UTF_8))
                //设置需要解析的jwt
                .parseClaimsJws(jwt).getBody();

        return claims;
    }
}
