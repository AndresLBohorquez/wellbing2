package com.devalb.wellbing2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/")
    public String goToIndex() {
        return "index";
    }

    @GetMapping("/login")
    public String goToLoginb() {
        return "login";
    }

}
