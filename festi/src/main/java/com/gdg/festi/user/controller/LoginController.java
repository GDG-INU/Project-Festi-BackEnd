package com.gdg.festi.user.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class LoginController {

    @RequestMapping("/login")
    public String loginPage() {
        return "user/login";
    }
}
