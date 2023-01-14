package com.bozo.issuetracker.model;

import jakarta.persistence.*;

@Entity
@Table(name = "comment")
public class IssueComment extends BaseEntity {

    @Column(name = "comment")
    private String comment;

    @ManyToOne
    @JoinColumn(name = "app_user_id")
    private User commentCreator;

    @ManyToOne
    @JoinColumn(name = "issue_id")
    private Issue issue;
}
