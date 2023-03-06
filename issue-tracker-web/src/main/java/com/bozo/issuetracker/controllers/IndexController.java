package com.bozo.issuetracker.controllers;

import com.bozo.issuetracker.enums.HTMLPaths;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@PreAuthorize("hasRole('ROLE_ANONYMOUS')")
public class IndexController {

    @GetMapping({"/", "/index", "/index.html"})
    public String index(){
        return HTMLPaths.INDEX.getPath();
    }
}
