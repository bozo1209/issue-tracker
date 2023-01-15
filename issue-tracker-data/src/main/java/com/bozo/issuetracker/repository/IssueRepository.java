package com.bozo.issuetracker.repository;

import com.bozo.issuetracker.model.Issue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IssueRepository extends JpaRepository<Issue, Long> {
}
