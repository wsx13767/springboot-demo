package com.siang.security.server.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/testRole")
@RestController
public class TestRoleController {
    @GetMapping("/admin/hello")
    public String admin() {
        return "Hello ADMIN";
    }
    @GetMapping("/user/hello")
    public String user() {
        return "Hello USER";
    }
    @GetMapping("/guest/hello")
    public String guest() {
        return "Hello GUEST";
    }
}
