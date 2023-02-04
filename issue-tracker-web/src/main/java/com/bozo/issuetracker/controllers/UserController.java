package com.bozo.issuetracker.controllers;

import com.bozo.issuetracker.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/user")
@Controller
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/all")
    public String allUserList(Model model){
        return null;
    }
}
