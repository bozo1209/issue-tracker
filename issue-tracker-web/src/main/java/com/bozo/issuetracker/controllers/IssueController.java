package com.bozo.issuetracker.controllers;

import com.bozo.issuetracker.annotation.PreAuthorizeRoleAdmin;
import com.bozo.issuetracker.enums.HTMLPaths;
import com.bozo.issuetracker.model.Issue;
import com.bozo.issuetracker.model.IssueComment;
import com.bozo.issuetracker.service.IssueService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/issue")
@Controller
@PreAuthorizeRoleAdmin
@AllArgsConstructor
public class IssueController {

    private final IssueService issueService;
// admin + user
    @InitBinder
    public void setAllowedFields(WebDataBinder dataBinder){
        dataBinder.setDisallowedFields("id");
    }
// admin + user
    @GetMapping("/all")
    public String allIssueList(Model model){
        model.addAttribute("issueList", issueService.findAll());
        return HTMLPaths.ISSUE_LIST.getPath();
    }
// admin + user
    @GetMapping("/{issueId}")
    public String showIssue(@PathVariable Long issueId, Model model){
        model.addAttribute("issue", issueService.findById(issueId));
        model.addAttribute("comment", IssueComment.builder().build());
        return HTMLPaths.ISSUE.getPath();
    }
// admin + user
    @GetMapping("/new")
    public String addNewIssue(Model model){
        model.addAttribute("issue", Issue.builder().build());
        return HTMLPaths.ADD_EDIT_ISSUE.getPath();
    }
// admin + user
    @PostMapping("/new")
    public String processAddingIssue(@Valid Issue issue, BindingResult result){
        if (result.hasErrors()){
            return HTMLPaths.ADD_EDIT_ISSUE.getPath();
        }

        Issue issueById = issueService.findById(1L);
        issue.setIssueCreator(issueById.getIssueCreator());
        issueById.getIssueCreator().getIssuesObserve().add(issue);
        Issue savedIssue = issueService.save(issue);
        return "redirect:/issue/" + savedIssue.getId();
    }
// admin + user
    @GetMapping("/{issueId}/edit")
    public String editIssue(@PathVariable Long issueId, Model model){
        model.addAttribute("issue", issueService.findById(issueId));
        return HTMLPaths.ADD_EDIT_ISSUE.getPath();
    }
// admin + user
    @PostMapping("/{issueId}/edit")
    public String processEditingIssue(@Valid Issue issue, @PathVariable Long issueId, BindingResult result){
        if (result.hasErrors()){
            return HTMLPaths.ADD_EDIT_ISSUE.getPath();
        }
        issue.setId(issueId);
        Issue issueById = issueService.findById(issueId);
        issue.setIssueCreator(issueById.getIssueCreator());
        issue.setComments(issueById.getComments());
        Issue savedIssue = issueService.save(issue);
        return "redirect:/issue/" + savedIssue.getId();
    }
// admin + user
    @GetMapping("/{issueId}/delete")
    public String deleteIssue(@PathVariable Long issueId){
        Issue issueById = issueService.findById(issueId);
        issueById.getIssueCreator().getIssuesObserve().remove(issueById);
        issueService.deleteById(issueId);
        return "redirect:/issue/all";
    }
}
