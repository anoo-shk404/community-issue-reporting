package com.community.issuereporting.service;

import com.community.issuereporting.entity.Issue;
import com.community.issuereporting.entity.IssueStatus;
import com.community.issuereporting.entity.User;
import com.community.issuereporting.repository.IssueRepository;
import com.community.issuereporting.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class IssueService {

    @Autowired
    private IssueRepository issueRepository;
    private UserRepository userRepository;

    public Issue createIssue(Issue issue) {
        return issueRepository.save(issue);
    }

    public List<Issue> getAllIssues() {
        return issueRepository.findAll();
    }

    public List<Issue> getIssuesByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
        return issueRepository.findByReportedBy(user);
    }

    public List<Issue> getIssuesByStatus(IssueStatus status) {
        return issueRepository.findByStatus(status);
    }

    public Optional<Issue> getIssueById(Long id) {
        return issueRepository.findById(id);
    }

    public Issue updateIssueStatus(Long id, IssueStatus status) {
        Issue issue = issueRepository.findById(id).orElseThrow();
        issue.setStatus(status);
        return issueRepository.save(issue);
    }
    public List<Issue> getByStatus(IssueStatus status) {
        return issueRepository.findByStatus(status);
    }
}
