package com.bozo.issuetracker.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

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
    private List<IssueComment> comments;

    @Builder
    public Issue(Long id, String description, User issueCreator, List<IssueComment> comments) {
        super(id);
        this.description = description;
        this.issueCreator = issueCreator;
        this.comments = comments;
    }
}
