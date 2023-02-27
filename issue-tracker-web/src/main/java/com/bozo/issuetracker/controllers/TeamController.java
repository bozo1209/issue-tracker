package com.bozo.issuetracker.controllers;

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
@AllArgsConstructor
public class TeamController {

    private final TeamService teamService;

    @InitBinder
    public void setAllowedFields(WebDataBinder dataBinder){
        dataBinder.setDisallowedFields("id");
    }

    @GetMapping("/all")
    public String allTeamList(Model model){
        return null;
    }

    @GetMapping("/{teamId}")
    public String showTeam(@PathVariable Long teamId, Model model){
        return null;
    }
    @GetMapping("/new")
    public String addNewTeam(Model model){
        return null;
    }

    @PostMapping("/new")
    public String processAddingTeam(@Valid Team team, BindingResult result){
        return null;
    }

    @GetMapping("/{teamId}/edit")
    public String editTeam(@PathVariable Long teamId, Model model){
        return null;
    }
    @PostMapping("/{teamId}/edit")
    public String processEditingTeam(@Valid Team team, @PathVariable Long teamId, BindingResult result){
        return null;
    }

    @GetMapping("/{teamId}/delete")
    public String deleteTeam(@PathVariable Long teamId){
        return null;
    }
}
