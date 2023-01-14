package com.bozo.issuetracker.model;

import java.util.List;

public class User extends BaseEntity {

    private String userName;
    private List<Issue> issuesCreated;
    private List<Issue> issuesObserve;
    private List<IssueComment> commentsCreated;

}
