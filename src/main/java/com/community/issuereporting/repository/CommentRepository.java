package com.community.issuereporting.repository;

import com.community.issuereporting.entity.Comment;
import com.community.issuereporting.entity.Issue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByIssue(Issue issue);
}
