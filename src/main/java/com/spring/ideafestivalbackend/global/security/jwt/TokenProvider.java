package com.spring.ideafestivalbackend.global.security.jwt;

import com.spring.ideafestivalbackend.global.exception.TokenExpirationException;
import com.spring.ideafestivalbackend.global.exception.TokenNotValidException;
import com.spring.ideafestivalbackend.global.security.auth.AuthDetailsService;
import com.spring.ideafestivalbackend.global.security.jwt.properties.JwtProperties;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.ZonedDateTime;
import java.util.Date;

@Component
@RequiredArgsConstructor
@Getter
public class TokenProvider {
    private final AuthDetailsService authDetailsService;
    private final JwtProperties jwtProperties;
    private final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 120; // 2시간
    private final long REFRESH_TOKEN_EXPIRE_TIME = ACCESS_TOKEN_EXPIRE_TIME * 12 * 7; // 일주일

    @AllArgsConstructor
    private enum TokenType {
        ACCESS_TOKEN("accessToken"),
        REFRESH_TOKEN("refreshToken");
        String value;
    }

    @AllArgsConstructor
    private enum TokenClaimName {
        USER_EMAIL("userEmail"),
        TOKEN_TYPE("tokenType");
        String value;
    }

    // 암호화된 키 값 가져오기
    private Key getSignInKey(String secretKey) {
        byte[] bytes = secretKey.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(bytes);
    }

    // Token 생성
    private String generateToken(String userEmail, TokenType tokenType, String secret, long expireTime) {
        final Claims claims = Jwts.claims();
        claims.put(TokenClaimName.USER_EMAIL.value, userEmail);
        claims.put(TokenClaimName.TOKEN_TYPE.value, tokenType);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expireTime))
                .signWith(getSignInKey(secret), SignatureAlgorithm.HS256)
                .compact();
    }

    // 모든 Claims 추출 ( Payload에 들어가는 값은 Claims 이라고 부른다 )
    public Claims extractAllClaims(String token, String secret) {
        token = token.replace("Bearer ", "");
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSignInKey(secret))
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw new TokenExpirationException("The token has expired.");
        } catch (JwtException e) {
            throw new TokenNotValidException("토큰이 올바르지 않습니다.");
        }

    }

    public ZonedDateTime getExpiredAtToken(String token, String secret) {
        return ZonedDateTime.now().plusSeconds(ACCESS_TOKEN_EXPIRE_TIME);
    }

    public String getUserEmail(String token, String secret) {
        return extractAllClaims(token, secret).get(TokenClaimName.USER_EMAIL.value, String.class);
    }

    public String getTokenType(String token, String secret) {
        return extractAllClaims(token, secret).get(TokenClaimName.TOKEN_TYPE.value, String.class);
    }

    public String generatedAccessToken(String email) {
        return generateToken(email, TokenType.ACCESS_TOKEN, jwtProperties.getAccessSecret(), ACCESS_TOKEN_EXPIRE_TIME);
    }

    public String generatedRefreshToken(String email) {
        return generateToken(email, TokenType.REFRESH_TOKEN, jwtProperties.getRefreshSecret(), REFRESH_TOKEN_EXPIRE_TIME);
    }

    public UsernamePasswordAuthenticationToken authentication(String userEmail) {
        UserDetails userDetails = authDetailsService.loadUserByUsername(userEmail);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

}