package com.veltroz.chatbot.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    private final Key signingKey;
    private final long expirationMillis = 1000L * 60 * 60 * 24;

    public JwtUtil(@Value("${app.jwt.secret}") String secret) {
        byte[] keyBytes;
        try {
            keyBytes = Decoders.BASE64.decode(secret);
            if (keyBytes.length < 32) {
                keyBytes = secret.getBytes(StandardCharsets.UTF_8);
            }
        } catch (IllegalArgumentException ex) {
            keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        }
        this.signingKey = Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(String email) {
        Date now = new Date();
        Date expires = new Date(now.getTime() + expirationMillis);
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(now)
                .setExpiration(expires)
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(signingKey).build().parseClaimsJws(token);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public String extractEmail(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(signingKey).build().parseClaimsJws(token).getBody();
        return claims.getSubject();
    }
}
