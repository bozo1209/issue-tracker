package com.bozo.issuetracker.controllers;

import com.bozo.issuetracker.annotation.PreAuthorizeRoleAdmin;
import com.bozo.issuetracker.annotation.PreAuthorizeRoleAdminOrRoleUser;
import com.bozo.issuetracker.details.user.ApplicationUser;
import com.bozo.issuetracker.enums.HTMLPaths;
import com.bozo.issuetracker.model.Issue;
import com.bozo.issuetracker.model.IssueComment;
import com.bozo.issuetracker.model.Project;
import com.bozo.issuetracker.model.User;
import com.bozo.issuetracker.service.IssueService;
import com.bozo.issuetracker.service.ProjectService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@PreAuthorizeRoleAdmin
@AllArgsConstructor
public class IssueController {

    private final IssueService issueService;
    private final ProjectService projectService;
// admin + user
    @PreAuthorizeRoleAdminOrRoleUser
    @InitBinder
    public void setAllowedFields(WebDataBinder dataBinder){
        dataBinder.setDisallowedFields("id");
    }
// admin + user
    @PreAuthorizeRoleAdminOrRoleUser
    @GetMapping("/issue/all")
    public String allIssueList(Model model){
        model.addAttribute("issueList", issueService.findAll());
        return HTMLPaths.ISSUE_LIST.getPath();
    }
// admin + user
    @PreAuthorizeRoleAdminOrRoleUser
    @GetMapping("/issue/{issueId}")
    public String showIssue(@PathVariable Long issueId, Model model){
        model.addAttribute("issue", issueService.findById(issueId));
        model.addAttribute("comment", IssueComment.builder().build());
        return HTMLPaths.ISSUE.getPath();
    }
// admin + user
    @PreAuthorizeRoleAdminOrRoleUser
    @GetMapping("/project/{projectId}/issue/new")
    public String addNewIssue(@PathVariable Long projectId, Model model){
        model.addAttribute("project", projectService.findById(projectId));
        model.addAttribute("issue", Issue.builder().build());
        model.addAttribute("issueComment", IssueComment.builder().build());
        return HTMLPaths.ADD_EDIT_ISSUE.getPath();
    }
// admin + user
    @PreAuthorizeRoleAdminOrRoleUser
    @PostMapping("/project/{projectId}/issue/new")
    public String processAddingIssue(@Valid Issue issue, @Valid IssueComment issueComment, @PathVariable Long projectId, BindingResult result){
        if (result.hasErrors()){
            return HTMLPaths.ADD_EDIT_ISSUE.getPath();
        }

        User loggedUser = ((ApplicationUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        issue.setIssueCreator(loggedUser);
        loggedUser.getIssuesObserve().add(issue);

        Optional.ofNullable(issueComment.getComment()).ifPresent(comment -> {
            if (!comment.isEmpty()){
                issueComment.setCommentCreator(loggedUser);
                loggedUser.getCommentsCreated().add(issueComment);
                issueComment.setIssue(issue);
                issue.getComments().add(issueComment);
            }
        });

        Project projectById = projectService.findById(projectId);
        projectById.getIssues().add(issue);
        issue.setProject(projectById);
        Issue savedIssue = issueService.save(issue);
        return "redirect:/issue/" + savedIssue.getId();
    }
// admin + user
    @PreAuthorizeRoleAdminOrRoleUser
    @GetMapping("/project/{projectId}/issue/{issueId}/edit")
    public String editIssue(@PathVariable Long issueId, @PathVariable Long projectId, Model model){
        model.addAttribute("project", projectService.findById(projectId));
        model.addAttribute("issue", issueService.findById(issueId));
        return HTMLPaths.ADD_EDIT_ISSUE.getPath();
    }
// admin + user
    @PreAuthorizeRoleAdminOrRoleUser
    @PostMapping("/project/{projectId}/issue/{issueId}/edit")
    public String processEditingIssue(@Valid Issue issue, @PathVariable Long issueId, @PathVariable Long projectId, BindingResult result){
        if (result.hasErrors()){
            return HTMLPaths.ADD_EDIT_ISSUE.getPath();
        }
        issue.setId(issueId);
        Issue issueById = issueService.findById(issueId);
        issue.setIssueCreator(issueById.getIssueCreator());
        issue.setUsersObserving(issueById.getUsersObserving());
        issue.setComments(issueById.getComments());
        issue.setProject(issueById.getProject());
        Issue savedIssue = issueService.save(issue);
        return "redirect:/issue/" + savedIssue.getId();
    }
// admin + user
    @PreAuthorizeRoleAdminOrRoleUser
    @GetMapping("/issue/{issueId}/delete")
    public String deleteIssue(@PathVariable Long issueId){
        Issue issueById = issueService.findById(issueId);
        issueById.getIssueCreator().getIssuesObserve().remove(issueById);
        issueById.getUsersObserving().forEach(u -> u.getIssuesObserve().remove(issueById));
        issueService.deleteById(issueId);
        return "redirect:/project/" + issueById.getProject().getId();
    }
}
