package com.community.issuereporting.service;

import com.community.issuereporting.entity.Comment;
import com.community.issuereporting.entity.Issue;
import com.community.issuereporting.repository.CommentRepository;
import com.community.issuereporting.repository.IssueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;
    private IssueRepository issueRepository;

    public Comment addComment(Comment comment) {
        return commentRepository.save(comment);
    }

    public List<Comment> getCommentsByIssue(Issue issue) {
        return commentRepository.findByIssue(issue);
    }

    public List<Comment> getCommentsByIssueId(Long issueId) {
        Issue issue = issueRepository.findById(issueId).orElse(null);
        if (issue == null) {
            throw new IllegalArgumentException("Issue not found with id: " + issueId);
        }
        return getCommentsByIssue(issue);
    }
    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }
}
