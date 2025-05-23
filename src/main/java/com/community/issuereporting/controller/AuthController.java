package com.community.issuereporting.controller;

import com.community.issuereporting.entity.User;
import com.community.issuereporting.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody User user) {
        return ResponseEntity.ok(userService.registerUser(user));
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user) {
        // Placeholder login logic, should use real JWT validation
        return ResponseEntity.ok("Login successful (JWT not implemented here)");
    }
}
