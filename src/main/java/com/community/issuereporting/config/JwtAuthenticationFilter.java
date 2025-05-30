package com.community.issuereporting.config;

import com.community.issuereporting.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        setFilterProcessesUrl("/api/auth/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        try {
            // Check if request has content
            if (request.getContentLength() <= 0) {
                throw new BadCredentialsException("Empty request body");
            }

            // Read and parse the request body
            ObjectMapper mapper = new ObjectMapper();
            com.community.issuereporting.entity.User creds = mapper.readValue(
                request.getInputStream(), 
                com.community.issuereporting.entity.User.class
            );

            if (creds.getUsername() == null || creds.getUsername().trim().isEmpty() ||
                creds.getPassword() == null || creds.getPassword().trim().isEmpty()) {
                throw new BadCredentialsException("Username or password is missing");
            }

            UsernamePasswordAuthenticationToken authToken = 
                new UsernamePasswordAuthenticationToken(
                    creds.getUsername().trim(), 
                    creds.getPassword()
                );
            
            return authenticationManager.authenticate(authToken);
        } catch (IOException e) {
            throw new BadCredentialsException("Invalid request format", e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                          FilterChain chain, Authentication authResult) throws IOException {
        User springUser = (User) authResult.getPrincipal();
        String token = jwtUtil.generateToken(springUser.getUsername());
        
        // Don't set CORS headers manually - let SecurityConfig handle it
        response.addHeader("Authorization", "Bearer " + token);
        
        // Create JSON response
        Map<String, Object> tokenMap = new HashMap<>();
        tokenMap.put("token", token);
        tokenMap.put("username", springUser.getUsername());
        tokenMap.put("authorities", springUser.getAuthorities());
        tokenMap.put("message", "Login successful");
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);
        
        new ObjectMapper().writeValue(response.getWriter(), tokenMap);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            org.springframework.security.core.AuthenticationException failed) throws IOException {
        // Don't set CORS headers manually - let SecurityConfig handle it
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("error", "Authentication failed");
        errorMap.put("message", failed.getMessage());
        
        new ObjectMapper().writeValue(response.getWriter(), errorMap);
    }
}