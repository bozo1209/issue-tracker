package com.bozo.issuetracker.controllers;

import com.bozo.issuetracker.model.Project;
import com.bozo.issuetracker.service.ProjectService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/project")
@Controller
@AllArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @InitBinder
    public void setAllowedFields(WebDataBinder dataBinder){
        dataBinder.setDisallowedFields("id");
    }

    @GetMapping("/all")
    public String allProjectList(Model model){
        return null;
    }

    @GetMapping("/{projectId}")
    public String showProject(@PathVariable Long projectId, Model model){
        return null;
    }

    @GetMapping("/new")
    public String addNewProject(Model model){
        return null;
    }

    @PostMapping("/new")
    public String processAddingProject(@Valid Project project, BindingResult result){
        return null;
    }

    @GetMapping("/{projectId}/edit")
    public String editProject(@PathVariable Long projectId, Model model){
        return null;
    }

    @PostMapping("/{projectId}/edit")
    public String processEditingProject(@Valid Project project, @PathVariable Long projectId, BindingResult result){
        return null;
    }

    @GetMapping("/{projectId}/delete")
    public String deleteProject(@PathVariable Long projectId){
        return null;
    }
}
