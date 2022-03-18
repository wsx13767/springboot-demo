package com.example.sion.hello.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Tag(name = "Hello", description = "hello api")
@RequestMapping("/hello")
@RestController
public class HelloController {
    @Value("${SION_HELLOWORLD:test}")
    private String test;
    
    @Value("${sion.helloWorld:test2}")
    private String test2;
    
    @GetMapping(value = "/test", produces = "application/vnd.spring-cloud.config-server.v2+json")
    public String hello1() {
        return test;
    }
    
    @GetMapping(value = "/test", produces = "application/vnd.spring-cloud.config-server.v2+xml")
    public String hello2() {
        return test2;
    }


    @Operation(summary = "Hello name",description = "response Hello, ${name}")
    @GetMapping("/{name}")
    public String hello(@PathVariable String name) {
        return "Hello, " + name;
    }
}
