package com.gdg.festi.user.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Base64;

@Service
public class JwtService {

    private final Key key;

    public JwtService(@Value("${jwt.secret}") String secretKey) {
        byte[] keyBytes = Base64.getDecoder().decode(secretKey); // Base64 디코딩 후 키 생성
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }


    // JWT 토큰에서 kakaoId 추출
    public String getUserIdFromToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key) // JWTUtil과 동일한 키 사용
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            return claims.getSubject(); // subject에서 kakaoId 가져오기
        } catch (Exception e) {
            return null; // 유효하지 않은 토큰
        }
    }

    public String extractBearerToken(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7); // "Bearer " 이후의 값 반환
        }
        return authorizationHeader; // Bearer가 없으면 원래 값 그대로 반환
    }



}
