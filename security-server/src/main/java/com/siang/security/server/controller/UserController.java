package com.siang.security.server.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.AsyncContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/user")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    @GetMapping
    public Map<String, Object> userInfo() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        Map<String, Object> result = new HashMap<>();
        result.put("name", authentication.getName());
        result.put("authorities", authentication.getAuthorities());
        return result;
    }

    @GetMapping("/test")
    public void userInfo(HttpServletRequest request, HttpServletResponse response) {
        AsyncContext asyncContext = request.startAsync();
        CompletableFuture.runAsync(() -> {
            try {
                PrintWriter out = asyncContext.getResponse().getWriter();
                out.write("hello");
                asyncContext.complete();
            } catch (IOException e) {
                logger.error("錯誤", e);
            }
        });
    }

    @GetMapping("/info")
    public Map<String, Object> info(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        result.put("remoteUser", request.getRemoteUser());
        result.put("name", request.getUserPrincipal().getName());
        result.put("admin", request.isUserInRole("ADMIN"));
        result.put("user", request.isUserInRole("USER"));
        return result;
    }

    @GetMapping("/authentication")
    public Authentication authentication(Authentication authentication) {
        logger.info(authentication.toString());
        return authentication;
    }

    @GetMapping("/principal")
    public Principal principal(Principal principal) {
        principal.getName();
        return principal;

    }
}
