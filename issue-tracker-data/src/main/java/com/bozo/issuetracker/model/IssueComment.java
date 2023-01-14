package com.bozo.issuetracker.model;

public class IssueComment extends BaseEntity {

    private String comment;
    private User commentCreator;
    private Issue issue;
}
