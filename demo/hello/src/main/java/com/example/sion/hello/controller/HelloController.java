package com.example.sion.hello.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


//@Tag()
@RequestMapping("/hello")
@RestController
public class HelloController {
    @Value("${springdoc.api-docs.path:#{T(org.springdoc.core.Constants).DEFAULT_API_DOCS_URL}}/swagger-config")
    private String path;

    @GetMapping("/{name}")
    public String hello(@PathVariable String name) {
        return "Hello, " + name;
    }

    @GetMapping("/showAll")
    public String showAll() {
        return path;
    }

}
