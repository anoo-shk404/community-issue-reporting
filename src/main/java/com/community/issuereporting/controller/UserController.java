package com.community.issuereporting.controller;

import com.community.issuereporting.entity.Issue;
import com.community.issuereporting.entity.User;
import com.community.issuereporting.service.IssueService;
import com.community.issuereporting.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private IssueService issueService;

    @GetMapping("/{userId}")
    public User getUserProfile(@PathVariable Long userId) {
        return userService.getById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
    }
    @GetMapping("/{userId}/issues")
    public List<Issue> getUserIssues(@PathVariable Long userId) {
        return issueService.getIssuesByUser(userId);
    }
}
