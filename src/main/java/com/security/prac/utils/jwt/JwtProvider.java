package com.security.prac.utils.jwt;

import com.security.prac.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class JwtProvider {
    private final JwtProperties jwtProperties;

    public String generateToken(User user, Duration expiredAt) {
        Date now = new Date();
        return makeToken(new Date(now.getTime() + expiredAt.toMillis()), user);
    }

    // Token 생성 메서드
    private String makeToken(Date expiry, User user) {
        Date now = new Date();
        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE) // (헤더) typ : JWT
                .setIssuer(jwtProperties.getIssuer()) // (내용) iss : test.test.com
                .setIssuedAt(now) // (내용) iat : 현재 시간
                .setExpiration(expiry) // (내용) exp : expiry 멤버 변수 값, 만료 시간
                .setSubject(user.getEmail()) // (내용) sub : user 이메일
                .claim("email", user.getEmail()) // (내용) email : user.getEmail()
                // 서명
                .signWith(jwtProperties.getKey(), SignatureAlgorithm.HS256) // // 암호화된 비밀키 값 + 해시을 HS256 방식으로 암호화
                .compact();
    }

    // JWT 토큰 유효성 검사
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(jwtProperties.getKey()) // secret 값으로 복호화
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // 토큰 기반으로 인증 정보를 가져오는 메서드
    public Authentication getAuthentication(String token) {
        Claims claims = getClaims(token);
        Set<SimpleGrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));

        return new UsernamePasswordAuthenticationToken(new org.springframework.security.core.userdetails.User(
                claims.getSubject(), "", authorities),
                token,
                authorities);
    }

    public Long getUserId(String token) {
        Claims claims = getClaims(token);
        return claims.get("userId", Long.class);
    }

    // 클레임 조회 메서드(body 안에 한 쌍의 [키와 값] 이 클레임이라는 단위이다.)
    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(jwtProperties.getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

}
