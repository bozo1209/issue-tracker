package com.bozo.issuetracker.enums;

public enum HTMLPaths {
    INDEX("index"),
    ISSUE_LIST("issue/issueList"),
    ISSUE("issue/issue"),
    ADD_EDIT_ISSUE("issue/addEditIssue"),
    USER_LIST("user/userList"),
    USER("user/user"),
    ADD_EDIT_USER("user/addEditUser"),
    PROJECT_LIST("project/projectList"),
    TEAM_LIST("team/teamList");

    private final String htmlPath;

    HTMLPaths(String htmlPath){
        this.htmlPath = htmlPath;
    }

    public String getPath(){
        return htmlPath;
    }
}
