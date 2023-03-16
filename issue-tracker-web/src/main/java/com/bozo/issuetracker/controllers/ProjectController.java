package com.bozo.issuetracker.controllers;

import com.bozo.issuetracker.annotation.PreAuthorizeRoleAdmin;
import com.bozo.issuetracker.enums.HTMLPaths;
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
@PreAuthorizeRoleAdmin
@AllArgsConstructor
public class ProjectController {

    private final ProjectService projectService;
// admin + user
    @InitBinder
    public void setAllowedFields(WebDataBinder dataBinder){
        dataBinder.setDisallowedFields("id");
    }
// admin + user
    @GetMapping("/all")
    public String allProjectList(Model model){
        model.addAttribute("projectList", projectService.findAll());
        return HTMLPaths.PROJECT_LIST.getPath();
    }
// admin + user
    @GetMapping("/{projectId}")
    public String showProject(@PathVariable Long projectId, Model model){
        model.addAttribute("project", projectService.findById(projectId));
        return HTMLPaths.PROJECT.getPath();
    }
// admin
    @GetMapping("/new")
    public String addNewProject(Model model){
        model.addAttribute("project", Project.builder().build());
        return HTMLPaths.ADD_EDIT_PROJECT.getPath();
    }
// admin
    @PostMapping("/new")
    public String processAddingProject(@Valid Project project, BindingResult result){
        if (result.hasErrors()){
            return HTMLPaths.ADD_EDIT_PROJECT.getPath();
        }

        Project projectById = projectService.findById(1L);
        project.setAssignedTeam(projectById.getAssignedTeam());
        projectById.getAssignedTeam().getProjects().add(project);
        Project savedProject = projectService.save(project);
        return "redirect:/project/" + savedProject.getId();
    }
// admin
    @GetMapping("/{projectId}/edit")
    public String editProject(@PathVariable Long projectId, Model model){
        model.addAttribute("project", projectService.findById(projectId));
        return HTMLPaths.ADD_EDIT_PROJECT.getPath();
    }
// admin
    @PostMapping("/{projectId}/edit")
    public String processEditingProject(@Valid Project project, @PathVariable Long projectId, BindingResult result){
        if (result.hasErrors()){
            return HTMLPaths.ADD_EDIT_PROJECT.getPath();
        }
        project.setId(projectId);
        Project projectById = projectService.findById(1L);
        project.setAssignedTeam(projectById.getAssignedTeam());
        project.setIssues(projectById.getIssues());
        Project savedProject = projectService.save(project);
        return "redirect:/project/" + savedProject.getId();
    }
// admin
    @GetMapping("/{projectId}/delete")
    public String deleteProject(@PathVariable Long projectId){
        Project projectById = projectService.findById(projectId);
        projectById.getAssignedTeam().getProjects().remove(projectById);
        projectService.deleteById(projectId);
        return "redirect:/project/all";
    }
}
