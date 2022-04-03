package com.company.enroller.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    @GetMapping(value = "")
    public String  goToMain() {
        return "redirect:https://sadamingo.github.io/";
    }
}