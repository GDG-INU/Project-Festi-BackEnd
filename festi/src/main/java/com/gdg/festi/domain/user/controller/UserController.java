package com.gdg.festi.domain.user.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user/*")
public class UserController {

    @RequestMapping("loginPage")
    public String loginPage(){

        return "user/loginPage";
    }

    @RequestMapping("loginSuccess")
    public String loginTestPage(){

        return "user/loginSuccess";
    }
}
