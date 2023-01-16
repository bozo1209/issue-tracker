package com.bozo.issuetracker.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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

    @Builder
    public IssueComment(Long id, String comment, User commentCreator, Issue issue) {
        super(id);
        this.comment = comment;
        this.commentCreator = commentCreator;
        this.issue = issue;
    }
}
