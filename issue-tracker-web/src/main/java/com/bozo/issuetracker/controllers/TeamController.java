package com.bozo.issuetracker.controllers;

import com.bozo.issuetracker.annotation.PreAuthorizeRoleAdmin;
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


@RequestMapping("/team")
@Controller
@PreAuthorizeRoleAdmin
@AllArgsConstructor
public class TeamController {

    private final TeamService teamService;

    @InitBinder
    public void setAllowedFields(WebDataBinder dataBinder){
        dataBinder.setDisallowedFields("id");
    }

    @GetMapping("/all")
    public String allTeamList(Model model){
        model.addAttribute("teamList", teamService.findAll());
        return HTMLPaths.TEAM_LIST.getPath();
    }

    @GetMapping("/{teamId}")
    public String showTeam(@PathVariable Long teamId, Model model){
        model.addAttribute("team", teamService.findById(teamId));
        return HTMLPaths.TEAM.getPath();
    }

    @GetMapping("/new")
    public String addNewTeam(Model model){
        model.addAttribute("team", Team.builder().build());
        return HTMLPaths.ADD_EDIT_TEAM.getPath();
    }

    @PostMapping("/new")
    public String processAddingTeam(@Valid Team team, BindingResult result){
        if (result.hasErrors()){
            return HTMLPaths.ADD_EDIT_TEAM.getPath();
        }

        Team savedTeam = teamService.save(team);
        return "redirect:/team/" + savedTeam.getId();
    }

    @GetMapping("/{teamId}/edit")
    public String editTeam(@PathVariable Long teamId, Model model){
        model.addAttribute("team", teamService.findById(teamId));
        return HTMLPaths.ADD_EDIT_TEAM.getPath();
    }

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

    @GetMapping("/{teamId}/delete")
    public String deleteTeam(@PathVariable Long teamId){
        teamService.deleteById(teamId);
        return "redirect:/team/all";
    }
}
