package com.siang.serverprovider.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/html")
@Controller
public class IndexController {

    @GetMapping("/")
    public String index(Model model) {
        return "/index";
    }
}
