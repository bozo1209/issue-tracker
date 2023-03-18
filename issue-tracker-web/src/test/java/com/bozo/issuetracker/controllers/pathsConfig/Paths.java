package com.bozo.issuetracker.controllers.pathsConfig;

public enum Paths {
    USER_PATH("/user"),
    TEAM_PATH("/team"),
    PROJECT_PATH("/project"),
    ISSUE_PATH("/issue"),
    COMMENT_PATH("/issue/{issueId}/comment");

    private final String path;

    Paths(String path){
        this.path = path;
    }

    public String getPath(){
        return path;
    }
}
