package com.bozo.issuetracker.controllers;

import com.bozo.issuetracker.annotation.PreAuthorizeRoleAdmin;
import com.bozo.issuetracker.annotation.PreAuthorizeRoleAdminOrRoleUser;
import com.bozo.issuetracker.enums.HTMLPaths;
import com.bozo.issuetracker.model.Team;
import com.bozo.issuetracker.service.TeamService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RequestMapping("/team")
@Controller
@PreAuthorizeRoleAdmin
@AllArgsConstructor
public class TeamController {

    private final TeamService teamService;

    @PreAuthorizeRoleAdminOrRoleUser
    @InitBinder
    public void setAllowedFields(WebDataBinder dataBinder){
        dataBinder.setDisallowedFields("id");
    }
// admin + user
    @PreAuthorizeRoleAdminOrRoleUser
    @GetMapping("/all")
    public String allTeamList(Model model){
        model.addAttribute("teamList", teamService.findAll());
        return HTMLPaths.TEAM_LIST.getPath();
    }
    // admin + user
    @PreAuthorizeRoleAdminOrRoleUser
    @GetMapping("/{teamId}")
    public String showTeam(@PathVariable Long teamId, Model model){
        model.addAttribute("team", teamService.findById(teamId));
        return HTMLPaths.TEAM.getPath();
    }
// admin
    @PreAuthorizeRoleAdmin
    @GetMapping("/new")
    public String addNewTeam(Model model){
        model.addAttribute("team", Team.builder().build());
        return HTMLPaths.ADD_EDIT_TEAM.getPath();
    }
// admin
    @PreAuthorizeRoleAdmin
    @PostMapping("/new")
    public String processAddingTeam(@Valid Team team, BindingResult result){
        if (result.hasErrors()){
            return HTMLPaths.ADD_EDIT_TEAM.getPath();
        }

        Team savedTeam = teamService.save(team);
        return "redirect:/team/" + savedTeam.getId();
    }
// admin
    @PreAuthorizeRoleAdmin
    @GetMapping("/{teamId}/edit")
    public String editTeam(@PathVariable Long teamId, Model model){
        model.addAttribute("team", teamService.findById(teamId));
        return HTMLPaths.ADD_EDIT_TEAM.getPath();
    }
// admin
    @PreAuthorizeRoleAdmin
    @PostMapping("/{teamId}/edit")
    public String processEditingTeam(@Valid Team team, @PathVariable Long teamId, BindingResult result){
        if (result.hasErrors()){
            return HTMLPaths.ADD_EDIT_TEAM.getPath();
        }

        team.setId(teamId);
        Team teamById = teamService.findById(teamId);
        team.setProjects(teamById.getProjects());
        team.setMembers(teamById.getMembers());
        team.setLeader(team.getLeader());
        Team savedTeam = teamService.save(team);
        return "redirect:/team/" + savedTeam.getId();
    }
// admin
    @PreAuthorizeRoleAdmin
    @GetMapping("/{teamId}/delete")
    public String deleteTeam(@PathVariable Long teamId){
        Team teamById = teamService.findById(teamId);
        Optional.ofNullable(teamById.getLeader()).ifPresent(leader -> leader.setLeaderOfTeam(null));
        teamById.setLeader(null);
        teamById.getMembers().forEach(user -> user.setMemberOfTeam(null));
        teamById.getProjects().forEach(project -> project.setAssignedTeam(null));
        teamService.deleteById(teamId);
        return "redirect:/team/all";
    }
}
