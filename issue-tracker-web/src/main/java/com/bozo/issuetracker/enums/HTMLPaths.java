package com.bozo.issuetracker.enums;

public enum HTMLPaths {
    INDEX("index"),
    ISSUELIST("issue/issueList");

    private final String htmlPath;

    HTMLPaths(String htmlPath){
        this.htmlPath = htmlPath;
    }

    public String getPath(){
        return htmlPath;
    }
}
