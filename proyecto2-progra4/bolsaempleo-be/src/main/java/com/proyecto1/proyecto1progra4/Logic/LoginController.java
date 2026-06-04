package com.proyecto1.proyecto1progra4.Logic;

import org.springframework.web.bind.annotation.GetMapping;

@org.springframework.stereotype.Controller
public class LoginController {
    @GetMapping("/login")
    public String login() {
        return "login/View";
    }

    @GetMapping("/notAuthorized")
    public String notAuthorized() {
        return "login/notAuthorized";
    }
};
