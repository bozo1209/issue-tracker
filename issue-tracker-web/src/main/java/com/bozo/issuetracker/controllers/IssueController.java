package com.bozo.issuetracker.controllers;

import com.bozo.issuetracker.enums.HTMLPaths;
import com.bozo.issuetracker.service.IssueService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;

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
}
