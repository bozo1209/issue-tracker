package com.bozo.issuetracker.controllers;

import com.bozo.issuetracker.annotation.PreAuthorizeRoleAdmin;
import com.bozo.issuetracker.annotation.PreAuthorizeRoleAdminOrRoleUser;
import com.bozo.issuetracker.details.user.ApplicationUser;
import com.bozo.issuetracker.model.Issue;
import com.bozo.issuetracker.model.IssueComment;
import com.bozo.issuetracker.model.User;
import com.bozo.issuetracker.service.IssueCommentService;
import com.bozo.issuetracker.service.IssueService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/issue/{issueId}/comment")
@Controller
@PreAuthorizeRoleAdmin
@AllArgsConstructor
public class IssueCommentController {

    private final IssueCommentService issueCommentService;
    private final IssueService issueService;
// admin + user
    @PreAuthorizeRoleAdminOrRoleUser
    @PostMapping("/new")
    public String processAddingComment(@Valid IssueComment comment, @PathVariable Long issueId, BindingResult result){
        User loggedUser = ((ApplicationUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        Issue issueById = issueService.findById(issueId);
        comment.setIssue(issueById);
        comment.setCommentCreator(loggedUser);
        issueCommentService.save(comment);
        return "redirect:/issue/" + issueId;
    }
    // admin + user
    @PreAuthorizeRoleAdminOrRoleUser
    @PostMapping("/{commentId}/edit")
    public String processEditingComment(@Valid IssueComment comment, @PathVariable Long issueId, @PathVariable Long commentId, BindingResult result){
        Issue issueById = issueService.findById(issueId);
        comment.setId(commentId);
        comment.setIssue(issueById);
        comment.setCommentCreator(issueCommentService.findById(commentId).getCommentCreator());
        issueCommentService.save(comment);
        return "redirect:/issue/" + issueId;
    }
    // admin + user
    @PreAuthorizeRoleAdminOrRoleUser
    @GetMapping("/{commentId}/delete")
    public String deleteComment(@PathVariable Long commentId, @PathVariable Long issueId){
        issueService.findById(issueId).getComments().remove(issueCommentService.findById(commentId));
        issueCommentService.deleteById(commentId);
        return "redirect:/issue/" + issueId;
    }
}
