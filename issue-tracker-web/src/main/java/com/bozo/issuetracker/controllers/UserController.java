package com.bozo.issuetracker.controllers;

import com.bozo.issuetracker.enums.HTMLPaths;
import com.bozo.issuetracker.model.User;
import com.bozo.issuetracker.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;

@RequestMapping("/user")
@Controller
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/all")
    public String allUserList(Model model){
        model.addAttribute("userList", userService.findAll());
        return HTMLPaths.USER_LIST.getPath();
    }

    @GetMapping("/{userId}")
    public String showUserById(@PathVariable Long userId, Model model){
        model.addAttribute("user", userService.findById(userId));
        return HTMLPaths.USER.getPath();
    }

    @GetMapping("/new")
    public String addNewUser(Model model){
        model.addAttribute("user", User.builder().build());
        return HTMLPaths.ADD_EDIT_USER.getPath();
    }

    @PostMapping("/new")
    public String processAddingUser(@Valid User user, BindingResult result){
        if (result.hasErrors()){
            return HTMLPaths.ADD_EDIT_USER.getPath();
        }
        User savedUser = userService.save(user);
        return "redirect:/user/" + savedUser.getId();
    }

    @GetMapping("/{userId}/edit")
    public String editUser(@PathVariable Long userId, Model model){
        model.addAttribute("user", userService.findById(userId));
        return HTMLPaths.ADD_EDIT_USER.getPath();
    }

    @PostMapping("/{userId}/edit")
    public String processEditingUser(@Valid User user, @PathVariable Long userId, BindingResult result){
        if (result.hasErrors()){
            return HTMLPaths.ADD_EDIT_USER.getPath();
        }
        User userById = userService.findById(userId);
        userById.setUserName(user.getUserName());
        User savedUser = userService.save(userById);
        return "redirect:/user/" + savedUser.getId();
    }

    @GetMapping("/{userId}/delete")
    public String deleteUser(@PathVariable Long userId){
        User userById = userService.findById(userId);
        userById.getIssuesCreated().forEach(issue -> issue.setIssueCreator(null));
        userById.getCommentsCreated().forEach(comment -> comment.setCommentCreator(null));
        userService.deleteById(userId);
        return "redirect:/user/all";
    }
}
