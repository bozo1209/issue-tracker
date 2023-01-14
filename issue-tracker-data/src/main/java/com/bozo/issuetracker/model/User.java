package com.bozo.issuetracker.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "app_user")
public class User extends BaseEntity {

    @Column(name = "user_name")
    private String userName;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "issueCreator")
    private List<Issue> issuesCreated;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "app_user_issue",
        joinColumns = @JoinColumn(name = "app_user_id"),
        inverseJoinColumns = @JoinColumn(name = "issue_id"))
    private List<Issue> issuesObserve;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "commentCreator")
    private List<IssueComment> commentsCreated;

}
