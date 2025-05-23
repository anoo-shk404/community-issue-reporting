package com.community.issuereporting.repository;

import com.community.issuereporting.entity.Issue;
import com.community.issuereporting.entity.User;
import com.community.issuereporting.entity.IssueStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IssueRepository extends JpaRepository<Issue, Long> {
    List<Issue> findByReportedBy(User user);
    List<Issue> findByStatus(IssueStatus status);
}
