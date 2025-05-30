package com.community.issuereporting.controller;

import com.community.issuereporting.entity.Issue;
import com.community.issuereporting.entity.IssueStatus;
import com.community.issuereporting.service.IssueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private IssueService issueService;

    @PutMapping("/issues/{id}/status")
    public Issue updateStatus(@PathVariable Long id, @RequestParam IssueStatus status) {
        return issueService.updateIssueStatus(id, status);
    }

    @GetMapping("/issues/status/{status}")
    public List<Issue> getIssuesByStatus(@PathVariable IssueStatus status) {
        return issueService.getByStatus(status);
    }
}
