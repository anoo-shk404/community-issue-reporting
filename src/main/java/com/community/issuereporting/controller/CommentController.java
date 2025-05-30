package com.community.issuereporting.controller;

import com.community.issuereporting.entity.Comment;
import com.community.issuereporting.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping
    public Comment addComment(@RequestBody Comment comment) {
        return commentService.addComment(comment);
    }

    @GetMapping("/issue/{issueId}")
    public List<Comment> getCommentsByIssueId(@PathVariable Long issueId) {
        return commentService.getCommentsByIssueId(issueId);
    }

    @DeleteMapping("/{id}")
    public void deleteComment(@PathVariable Long id) {
        commentService.deleteComment(id);
    }
}
