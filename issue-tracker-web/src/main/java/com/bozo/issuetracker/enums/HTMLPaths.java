package com.bozo.issuetracker.enums;

public enum HTMLPaths {
    INDEX("index"),
    ISSUE_LIST("issue/issueList"),
    ISSUE("issue/issue");

    private final String htmlPath;

    HTMLPaths(String htmlPath){
        this.htmlPath = htmlPath;
    }

    public String getPath(){
        return htmlPath;
    }
}
