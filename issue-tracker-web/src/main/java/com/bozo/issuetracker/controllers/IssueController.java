package com.bozo.issuetracker.controllers;

import com.bozo.issuetracker.enums.HTMLPaths;
import com.bozo.issuetracker.model.Issue;
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
@AllArgsConstructor
public class IssueController {

    private final IssueService issueService;

    @InitBinder
    public void setAllowedFields(WebDataBinder dataBinder){
        dataBinder.setDisallowedFields("id");
    }

    @GetMapping("/all")
    public String allIssueList(Model model){
        model.addAttribute("issueList", issueService.findAll());
        return HTMLPaths.ISSUE_LIST.getPath();
    }

    @GetMapping("/{id}")
    public String showIssue(@PathVariable Long id, Model model){
        model.addAttribute("issue", issueService.findById(id));
        return HTMLPaths.ISSUE.getPath();
    }

    @GetMapping("/new")
    public String addNewIssue(Model model){
        model.addAttribute("issue", Issue.builder().build());
        return HTMLPaths.ADD_EDIT_ISSUE.getPath();
    }

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

    @GetMapping("/{id}/edit")
    public String editIssue(@PathVariable Long id, Model model){
        model.addAttribute("issue", issueService.findById(id));
        return HTMLPaths.ADD_EDIT_ISSUE.getPath();
    }

    @PostMapping("/{id}/edit")
    public String processEditingIssue(@Valid Issue issue, @PathVariable Long id, BindingResult result){
        if (result.hasErrors()){
            return HTMLPaths.ADD_EDIT_ISSUE.getPath();
        }
        issue.setId(id);
        Issue issueById = issueService.findById(id);
        issue.setIssueCreator(issueById.getIssueCreator());
        issue.setComments(issueById.getComments());
        Issue savedIssue = issueService.save(issue);
        return "redirect:/issue/" + savedIssue.getId();
    }

    @GetMapping("{id}/delete")
    public String deleteIssue(@PathVariable Long id){
        Issue issueById = issueService.findById(id);
        issueById.getIssueCreator().getIssuesObserve().remove(issueById);
        issueService.deleteById(id);
        return "redirect:/issue/all";
    }
}
