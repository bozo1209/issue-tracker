package com.bozo.issuetracker.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "issue")
public class Issue extends BaseEntity {

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "app_user_id")
    private User issueCreator;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "issue")
    private Set<IssueComment> comments = new HashSet<>();

    @ManyToMany(mappedBy = "issuesObserve")
    private Set<User> usersObserving = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    @Builder
    public Issue(
            Long id,
            String description,
            User issueCreator,
            Set<IssueComment> comments,
            Set<User> usersObserving,
            Project project) {
        super(id);
        this.description = description;
        this.issueCreator = issueCreator;
        this.comments = Optional.ofNullable(comments).orElseGet(this::getComments);
        this.usersObserving = Optional.ofNullable(usersObserving).orElseGet(this::getUsersObserving);
        this.project = project;
    }
}
