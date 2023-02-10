package com.bozo.issuetracker.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "app_user")
public class User extends BaseEntity {

    @Column(name = "user_name")
    private String userName;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH}, mappedBy = "issueCreator")
    private List<Issue> issuesCreated = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "app_user_issue_observe",
        joinColumns = @JoinColumn(name = "app_user_id"),
        inverseJoinColumns = @JoinColumn(name = "issue_id"))
    private Set<Issue> issuesObserve = new HashSet<>();

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH}, mappedBy = "commentCreator")
    private List<IssueComment> commentsCreated = new ArrayList<>();

    @Builder
    public User(Long id, String userName, List<Issue> issuesCreated, Set<Issue> issuesObserve, List<IssueComment> commentsCreated) {
        super(id);
        this.userName = userName;
        this.issuesCreated = Optional.ofNullable(issuesCreated).orElseGet(this::getIssuesCreated);
        this.issuesObserve = Optional.ofNullable(issuesObserve).orElseGet(this::getIssuesObserve);
        this.commentsCreated = Optional.ofNullable(commentsCreated).orElseGet(this::getCommentsCreated);
    }
}
