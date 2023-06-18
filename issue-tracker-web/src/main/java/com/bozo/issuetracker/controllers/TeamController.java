package com.bozo.issuetracker.controllers;

import com.bozo.issuetracker.annotation.PreAuthorizeRoleAdmin;
import com.bozo.issuetracker.annotation.PreAuthorizeRoleAdminTeamsTeamLeader;
import com.bozo.issuetracker.annotation.PreAuthorizeWithAnyRole;
import com.bozo.issuetracker.details.service.ApplicationUserDetailsService;
import com.bozo.issuetracker.details.user.ApplicationUser;
import com.bozo.issuetracker.enums.HTMLPaths;
import com.bozo.issuetracker.enums.UserRoles;
import com.bozo.issuetracker.model.Team;
import com.bozo.issuetracker.model.User;
import com.bozo.issuetracker.service.TeamService;
import com.bozo.issuetracker.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;


@RequestMapping("/team")
@Controller
@PreAuthorizeRoleAdmin
@AllArgsConstructor
public class TeamController {

    private final TeamService teamService;
    private final UserService userService;
    private final ApplicationUserDetailsService applicationUserDetailsService;

    @PreAuthorizeWithAnyRole
    @InitBinder
    public void setAllowedFields(WebDataBinder dataBinder){
        dataBinder.setDisallowedFields("id");
    }
// admin + team leader + user
    @PreAuthorizeWithAnyRole
    @GetMapping("/all")
    public String allTeamList(Model model){
        model.addAttribute("teamList", teamService.findAll());
        return HTMLPaths.TEAM_LIST.getPath();
    }
//     admin + team leader + user
    @PreAuthorizeWithAnyRole
    @GetMapping("/{teamId}")
    public String showTeam(@PathVariable Long teamId, Model model){
        Team teamById = teamService.findById(teamId);
        model.addAttribute("team", teamById);
        List<User> byMemberOfTeamIsNull = userService.findByMemberOfTeamIsNull();
        model.addAttribute("usersWithoutTeam", byMemberOfTeamIsNull);
        byMemberOfTeamIsNull.forEach(userService::updateUserInCache);
        Optional
                .ofNullable(teamById.getLeader())
                .ifPresentOrElse(
                        leader -> model.addAttribute("teamMembers", Set.of()),
                        () -> model.addAttribute("teamMembers", teamById.getMembers()));
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
//    admin + team leader
    @PreAuthorizeRoleAdminTeamsTeamLeader
    @GetMapping("/{teamId}/user/{userId}")
    public String addUserToTeam(@PathVariable Long teamId, @PathVariable Long userId){
        User user = userService.findById(userId);
        Team team = teamService.findById(teamId);
        user.setMemberOfTeam(team);
        team.getMembers().add(user);
        teamService.save(team);

        Optional.ofNullable(team.getLeader()).ifPresent(tl -> {
            userService.updateUserInCache(tl);
            User loggedUser = ((ApplicationUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
            if (Objects.equals(loggedUser.getId(), tl.getId())) {
                UserDetails userDetails = applicationUserDetailsService.loadUserByUsername(tl.getUserName());
                SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities()));
            }
        });

        return "redirect:/team/" + teamId;
    }
//    admin
    @PreAuthorizeRoleAdmin
    @GetMapping("/{teamId}/setleader/{userId}")
    public String setNewTeamLeader(@PathVariable Long teamId, @PathVariable Long userId){
        User user = userService.findById(userId);
        Team team = teamService.findById(teamId);
        user.setLeaderOfTeam(team);
        user.setRole(UserRoles.TEAM_LEADER);
        team.setLeader(user);
        Team savedTeam = teamService.save(team);

        Optional.ofNullable(savedTeam.getLeader()).ifPresent(tl -> {
            userService.updateUserInCache(tl);
            User loggedUser = ((ApplicationUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
            if (Objects.equals(loggedUser.getId(), tl.getId())) {
                UserDetails userDetails = applicationUserDetailsService.loadUserByUsername(tl.getUserName());
                SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities()));
            }
        });

        return "redirect:/team/" + teamId;
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
