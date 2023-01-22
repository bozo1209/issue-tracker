package com.bozo.issuetracker.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "app_user")
public class User extends BaseEntity {

    @Column(name = "user_name")
    private String userName;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "issueCreator")
    private List<Issue> issuesCreated = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "app_user_issue_observe",
        joinColumns = @JoinColumn(name = "app_user_id"),
        inverseJoinColumns = @JoinColumn(name = "issue_id"))
    private List<Issue> issuesObserve = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "commentCreator")
    private List<IssueComment> commentsCreated = new ArrayList<>();

    @Builder
    public User(Long id, String userName, List<Issue> issuesCreated, List<Issue> issuesObserve, List<IssueComment> commentsCreated) {
        super(id);
        this.userName = userName;
        this.issuesCreated = Optional.ofNullable(issuesCreated).orElseGet(this::getIssuesCreated);
        this.issuesObserve = Optional.ofNullable(issuesObserve).orElseGet(this::getIssuesObserve);
        this.commentsCreated = Optional.ofNullable(commentsCreated).orElseGet(this::getCommentsCreated);
    }
}
