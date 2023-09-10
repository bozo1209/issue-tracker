package com.bozo.issuetracker.controllers;

import com.bozo.issuetracker.annotation.PreAuthorizeRoleAdmin;
import com.bozo.issuetracker.annotation.PreAuthorizeWithAnyRole;
import com.bozo.issuetracker.enums.HTMLPaths;
import com.bozo.issuetracker.model.Project;
import com.bozo.issuetracker.service.ProjectService;
import com.bozo.issuetracker.service.TeamService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.Optional;

@RequestMapping("/project")
@Controller
@PreAuthorizeRoleAdmin
@AllArgsConstructor
public class ProjectController {

    private final ProjectService projectService;
    private final TeamService teamService;
// admin + team leader + user
    @PreAuthorizeWithAnyRole
    @InitBinder
    public void setAllowedFields(WebDataBinder dataBinder){
        dataBinder.setDisallowedFields("id");
    }
// admin + team leader + user
    @PreAuthorizeWithAnyRole
    @GetMapping("/all")
    public String allProjectList(Model model){
        model.addAttribute("projectList", projectService.findAll());
        return HTMLPaths.PROJECT_LIST.getPath();
    }
// admin + team leader + user
    @PreAuthorizeWithAnyRole
    @GetMapping("/{projectId}")
    public String showProject(@PathVariable Long projectId, Model model){
        model.addAttribute("project", projectService.findById(projectId));
        return HTMLPaths.PROJECT.getPath();
    }
// admin + team leader of team that gets project
    @PreAuthorizeRoleAdmin
    @GetMapping("/new")
    public String addNewProject(Model model){
        model.addAttribute("project", Project.builder().build());
        model.addAttribute("teamList", teamService.findAll());
        return HTMLPaths.ADD_EDIT_PROJECT.getPath();
    }
// admin + team leader of team that gets project
    @PreAuthorizeRoleAdmin
    @PostMapping("/new")
    public String processAddingProject(@Valid Project project, BindingResult result){
        if (result.hasErrors()){
            return HTMLPaths.ADD_EDIT_PROJECT.getPath();
        }

        Optional.ofNullable(project.getAssignedTeam()).ifPresent(team -> team.getProjects().add(project));
        Project savedProject = projectService.save(project);
        return "redirect:/project/" + savedProject.getId();
    }
// admin + team leader of team that gets project
    @PreAuthorizeRoleAdmin
    @GetMapping("/{projectId}/edit")
    public String editProject(@PathVariable Long projectId, Model model){
        model.addAttribute("project", projectService.findById(projectId));
        model.addAttribute("teamList", teamService.findAll());
        return HTMLPaths.ADD_EDIT_PROJECT.getPath();
    }
// admin + team leader of team that gets project
    @PreAuthorizeRoleAdmin
    @PostMapping("/{projectId}/edit")
    public String processEditingProject(@Valid Project project, @PathVariable Long projectId, BindingResult result){
        if (result.hasErrors()){
            return HTMLPaths.ADD_EDIT_PROJECT.getPath();
        }
        Project projectById = projectService.findById(projectId);
        projectById.setProjectName(project.getProjectName());

        Optional.ofNullable(project.getAssignedTeam()).ifPresent(team -> {
            Optional.ofNullable(projectById.getAssignedTeam()).ifPresent(assignedTeam -> {
                if (!Objects.equals(team.getId(), assignedTeam.getId())){
                    projectById.getAssignedTeam().getProjects().remove(projectById);
                    team.getProjects().add(projectById);
                }

            });
        });

        projectById.setAssignedTeam(project.getAssignedTeam());

        Project savedProject = projectService.save(projectById);
        return "redirect:/project/" + savedProject.getId();
    }
// admin
    @PreAuthorizeRoleAdmin
    @GetMapping("/{projectId}/delete")
    public String deleteProject(@PathVariable Long projectId){
        Project projectById = projectService.findById(projectId);
        projectById.getAssignedTeam().getProjects().remove(projectById);
        projectService.deleteById(projectId);
        return "redirect:/project/all";
    }
}
