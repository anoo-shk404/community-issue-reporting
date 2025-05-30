package com.community.issuereporting.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    // Use a strong secret key - in production, store this securely (environment variable, config server, etc.)
    private final String SECRET_KEY = "Qf7sR3!vY9xLp8Z2hUwN4MtGkB0cXeFo";
    private final long EXPIRATION_TIME = 86400000; // 1 day in milliseconds

    // Generate secret key from string
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
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
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
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