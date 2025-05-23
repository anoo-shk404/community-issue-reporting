package com.community.issuereporting.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {
    
    // Use a strong secret key - in production, store this securely (environment variable, config server, etc.)
    private final String SECRET_KEY = "your_very_long_secret_key_that_should_be_at_least_256_bits_long_for_HS256_algorithm";
    private final long EXPIRATION_TIME = 86400000; // 1 day in milliseconds
    
    // Generate secret key from string
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }
    
    public String generateToken(String username) {
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSigningKey())
                .compact();
    }
    
    public String extractUsername(String token) {
        return parseClaims(token).getSubject();
    }
    
    // Check if token is expired
    public boolean isTokenExpired(String token) {
        return parseClaims(token).getExpiration().before(new Date());
    }
    
    // Validate token by username and expiration
    public boolean validateToken(String token, String username) {
        try {
            String tokenUsername = extractUsername(token);
            return (tokenUsername.equals(username) && !isTokenExpired(token));
        } catch (JwtException | IllegalArgumentException e) {
            // Token is invalid
            return false;
        }
    }
    
    // Helper method to parse claims safely
    private Claims parseClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (JwtException | IllegalArgumentException e) {
            throw new RuntimeException("Invalid JWT token", e);
        }
    }
    
    // Additional utility method to check if token is valid (without username comparison)
    public boolean isTokenValid(String token) {
        try {
            parseClaims(token);
            return !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }
    
    // Extract expiration date from token
    public Date extractExpiration(String token) {
        return parseClaims(token).getExpiration();
    }
    
    // Extract issued at date from token
    public Date extractIssuedAt(String token) {
        return parseClaims(token).getIssuedAt();
    }
}