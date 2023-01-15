package com.bozo.issuetracker.repository;

import com.bozo.issuetracker.model.IssueComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IssueCommentRepository extends JpaRepository<IssueComment, Long> {
}
