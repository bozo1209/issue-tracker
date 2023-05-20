package com.bozo.issuetracker.controllers;

import com.bozo.issuetracker.annotation.PreAuthorizeRoleAdmin;
import com.bozo.issuetracker.annotation.PreAuthorizeWithAnyRole;
import com.bozo.issuetracker.annotation.PreAuthorizeRoleAdminOrUsersTeamLeaderOrUserWithSameId;
import com.bozo.issuetracker.details.service.ApplicationUserDetailsService;
import com.bozo.issuetracker.details.user.ApplicationUser;
import com.bozo.issuetracker.details.user.EncodePasswordForUser;
import com.bozo.issuetracker.enums.HTMLPaths;
import com.bozo.issuetracker.enums.UserRoles;
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

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

@RequestMapping("/user")
@Controller
@PreAuthorizeRoleAdmin
@AllArgsConstructor
public class UserController {

    private final UserService userService;
    private final TeamService teamService;
    private final ApplicationUserDetailsService applicationUserDetailsService;
    private final EncodePasswordForUser encodePasswordForUser;

    @PreAuthorizeWithAnyRole
    @InitBinder
    public void setAllowedFields(WebDataBinder dataBinder){
        dataBinder.setDisallowedFields("id");
    }
// user + team leader + admin
    @PreAuthorizeWithAnyRole
    @GetMapping("/all")
    public String allUserList(Model model){
        model.addAttribute("userList", userService.findAll());
        return HTMLPaths.USER_LIST.getPath();
    }
// user + team leader + admin
    @PreAuthorizeWithAnyRole
    @GetMapping("/{userId}")
    public String showUserById(@PathVariable Long userId, Model model){
        model.addAttribute("user", userService.findById(userId));
        return HTMLPaths.USER.getPath();
    }
// admin
    @PreAuthorizeRoleAdmin
    @GetMapping("/new")
    public String addNewUser(Model model){
        model.addAttribute("user", User.builder().build());
        model.addAttribute("roleList", Arrays.asList(UserRoles.values()));
        model.addAttribute("teamList", teamService.findAll());
        model.addAttribute("leaderTeamList", teamService.findByLeaderIdOrLeaderIsNull(null));
        return HTMLPaths.ADD_EDIT_USER.getPath();
    }
// admin
    @PreAuthorizeRoleAdmin
    @PostMapping("/new")
    public String processAddingUser(@Valid User user, BindingResult result){
        if (result.hasErrors()){
            return HTMLPaths.ADD_EDIT_USER.getPath();
        }

        Optional.ofNullable(user.getLeaderOfTeam()).ifPresentOrElse(team -> {
            user.setRole(UserRoles.TEAM_LEADER);
            user.setMemberOfTeam(team);
            team.getMembers().add(user);
        }, () -> Optional.ofNullable(user.getMemberOfTeam()).ifPresent(team -> team.getMembers().add(user)));

        user.setPassword(encodePasswordForUser.encodePasswordForUser(user.getPassword()));

        User savedUser = userService.save(user);

        Optional.ofNullable(savedUser.getMemberOfTeam())
                .flatMap(team -> Optional.ofNullable(team.getLeader())).ifPresent(tl -> {
            userService.updateUserInCache(tl);
            User loggedUser = ((ApplicationUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
            if (Objects.equals(loggedUser.getId(), tl.getId())) {
                UserDetails userDetails = applicationUserDetailsService.loadUserByUsername(tl.getUserName());
                SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities()));
            }
        });
        return "redirect:/user/" + savedUser.getId();
    }
// user with id + user's team leader + admin
    @PreAuthorizeRoleAdminOrUsersTeamLeaderOrUserWithSameId
    @GetMapping("/{userId}/edit")
    public String editUser(@PathVariable Long userId, Model model){
        model.addAttribute("user", userService.findById(userId));
        model.addAttribute("roleList", Arrays.asList(UserRoles.values()));
        model.addAttribute("teamList", teamService.findAll());
        model.addAttribute("leaderTeamList", teamService.findByLeaderIdOrLeaderIsNull(userId));
        return HTMLPaths.ADD_EDIT_USER.getPath();
    }
// user with id + user's team leader + admin
    @PreAuthorizeRoleAdminOrUsersTeamLeaderOrUserWithSameId
    @PostMapping("/{userId}/edit")
    public String processEditingUser(@Valid User user, @PathVariable Long userId, BindingResult result){
        if (result.hasErrors()){
            return HTMLPaths.ADD_EDIT_USER.getPath();
        }
        User userById = userService.findById(userId);
        userById.setUserName(user.getUserName());
        userById.setPassword(encodePasswordForUser.encodePasswordForUser(user.getPassword()));
        userById.setRole(user.getRole());
        Optional.ofNullable(userById.getMemberOfTeam()).ifPresent(team -> team.getMembers().remove(userById));
        Optional.ofNullable(user.getLeaderOfTeam()).ifPresentOrElse(leaderTeam -> {
            userById.setLeaderOfTeam(leaderTeam);
            userById.setRole(UserRoles.TEAM_LEADER);
            userById.setMemberOfTeam(leaderTeam);
        }, () -> {
            userById.setLeaderOfTeam(null);
            Optional.ofNullable(user.getMemberOfTeam()).ifPresent(userById::setMemberOfTeam);

        });
        Optional.ofNullable(userById.getMemberOfTeam()).ifPresent(team -> team.getMembers().add(userById));
        User savedUser = userService.save(userById);

        Optional.ofNullable(savedUser.getMemberOfTeam())
                .flatMap(team -> Optional.ofNullable(team.getLeader())).ifPresent(tl -> {
                    userService.updateUserInCache(tl);
                    User loggedUser = ((ApplicationUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
                    if (Objects.equals(loggedUser.getId(), tl.getId())) {
                        UserDetails userDetails = applicationUserDetailsService.loadUserByUsername(tl.getUserName());
                        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities()));
                    }
                });

        return "redirect:/user/" + savedUser.getId();
    }
// admin
    @PreAuthorizeRoleAdmin
    @GetMapping("/{userId}/delete")
    public String deleteUser(@PathVariable Long userId){
        User userById = userService.findById(userId);
        userById.getIssuesCreated().forEach(issue -> issue.setIssueCreator(null));
        userById.getCommentsCreated().forEach(comment -> comment.setCommentCreator(null));
        userService.delete(userById);
        return "redirect:/user/all";
    }
}
