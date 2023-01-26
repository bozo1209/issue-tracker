package com.bozo.issuetracker.controllers;

import com.bozo.issuetracker.enums.HTMLPaths;
import com.bozo.issuetracker.model.Issue;
import com.bozo.issuetracker.model.User;
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
    public String processAddingIssue(@Valid Issue issue, BindingResult result, Model model){
        if (result.hasErrors()){
            return HTMLPaths.ADD_EDIT_ISSUE.getPath();
        }
        issue.setIssueCreator(issueService.findById(1L).getIssueCreator());
        Issue savedIssue = issueService.save(issue);
        return "redirect:/issue/" + savedIssue.getId();
    }
}