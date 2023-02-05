package com.bozo.issuetracker.controllers;

import com.bozo.issuetracker.enums.HTMLPaths;
import com.bozo.issuetracker.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

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

    @GetMapping("/{id}")
    public String showUserById(@PathVariable Long id, Model model){
        model.addAttribute("user", userService.findById(id));
        return HTMLPaths.USER.getPath();
    }
}
