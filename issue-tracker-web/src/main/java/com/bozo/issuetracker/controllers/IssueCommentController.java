package com.bozo.issuetracker.controllers;

import com.bozo.issuetracker.model.Issue;
import com.bozo.issuetracker.model.IssueComment;
import com.bozo.issuetracker.service.IssueCommentService;
import com.bozo.issuetracker.service.IssueService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/issue/{issueId}/comment")
@Controller
@AllArgsConstructor
public class IssueCommentController {

    private final IssueCommentService issueCommentService;
    private final IssueService issueService;

    @PostMapping("/new")
    public String processAddingComment(@Valid IssueComment comment, @PathVariable Long issueId, BindingResult result){
        Issue issueById = issueService.findById(issueId);
        comment.setIssue(issueById);
        comment.setCommentCreator(issueById.getIssueCreator());
        issueCommentService.save(comment);
        return "redirect:/issue/" + issueId;
    }
}