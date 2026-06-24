package com.uit.se104.feedback_system.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/*
 - Service xử lý việc tạo sinh, giải mã và kiểm tra tính hợp lệ của JSON Web Token (JWT).
 - Hỗ trợ duy trì phiên đăng nhập an toàn của người dùng (UC01).
 */
@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    private Key getSigningKey() {
        byte[] keyBytes = io.jsonwebtoken.io.Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }


    // Lấy tên tài khoản (Email) từ nội dung token

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(getSigningKey())
            .build()
            .parseClaimsJws(token)
            .getBody();
    }

    //Tạo mới token JWT dựa trên thông tin UserDetails

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        // Đưa quyền hạn (Role) của user vào token để Frontend phân quyền giao diện
        claims.put("roles", userDetails.getAuthorities().stream()
            .map(auth -> auth.getAuthority()).toList());

        return Jwts.builder()
            .setClaims(claims)
            .setSubject(userDetails.getUsername()) // Subject là Email của User
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
            .signWith(getSigningKey(), SignatureAlgorithm.HS256)
            .compact();
    }

    //Kiểm tra xem Token có hợp lệ với tài khoản đăng nhập hay không

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}
