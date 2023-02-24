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
    PROJECT("project/project"),
    ADD_EDIT_PROJECT("project/addEditProject"),
    TEAM_LIST("team/teamList"),
    TEAM("team/team"),
    ADD_EDIT_TEAM("team/addEditTeam");

    private final String htmlPath;

    HTMLPaths(String htmlPath){
        this.htmlPath = htmlPath;
    }

    public String getPath(){
        return htmlPath;
    }
}
