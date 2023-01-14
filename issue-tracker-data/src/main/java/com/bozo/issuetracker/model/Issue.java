package com.bozo.issuetracker.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "issue")
public class Issue extends BaseEntity {

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "app_user_id")
    private User issueCreator;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "issue")
    private List<IssueComment> comments;
}
