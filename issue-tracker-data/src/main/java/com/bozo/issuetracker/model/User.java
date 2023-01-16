package com.bozo.issuetracker.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

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
    private List<Issue> issuesCreated;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "app_user_issue",
        joinColumns = @JoinColumn(name = "app_user_id"),
        inverseJoinColumns = @JoinColumn(name = "issue_id"))
    private List<Issue> issuesObserve;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "commentCreator")
    private List<IssueComment> commentsCreated;

    @Builder
    public User(Long id, String userName, List<Issue> issuesCreated, List<Issue> issuesObserve, List<IssueComment> commentsCreated) {
        super(id);
        this.userName = userName;
        this.issuesCreated = issuesCreated;
        this.issuesObserve = issuesObserve;
        this.commentsCreated = commentsCreated;
    }
}
