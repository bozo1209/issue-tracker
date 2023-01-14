package com.bozo.issuetracker.model;

import java.util.List;

public class Issue extends BaseEntity {

    private String description;
    private User issueCreator;
    private List<User> issueObserver;
}
