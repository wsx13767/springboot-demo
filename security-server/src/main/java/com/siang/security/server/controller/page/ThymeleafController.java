package com.siang.security.server.controller.page;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ThymeleafController {
    @RequestMapping("/mylogin.html")
    public String mylogin() {
        return "mylogin";
    }
}
