package com.example.Gateway.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtUtil{

    private static final String SECRET_KEY = "your-very-secure-secret-key-which-should-be-256-bit-long";


    private SecretKey generateKey() {
        try {
            byte[] keyBytes = SECRET_KEY.getBytes();
            return Keys.hmacShaKeyFor(keyBytes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String extractUserName(String token) {
        return extractClaims(token, Claims -> Claims.getSubject());
    }

    private <T> T extractClaims(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        SecretKey key = generateKey();
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean validateToken(String token) {
        final String userName = extractUserName(token);
        return (userName.equals("john_doe_1") && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaims(token, Claims::getExpiration);
    }
}
