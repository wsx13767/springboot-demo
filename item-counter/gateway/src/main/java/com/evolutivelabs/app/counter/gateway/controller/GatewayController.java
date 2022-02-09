package com.evolutivelabs.app.counter.gateway.controller;

import com.evolutivelabs.app.counter.gateway.model.AuthRequest;
import com.evolutivelabs.app.counter.gateway.utils.JwtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.reactive.result.view.RedirectView;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Map;

@Controller
public class GatewayController {
    private static final Logger logger = LoggerFactory.getLogger(GatewayController.class);

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ReactiveUserDetailsService userDetailsService;

    @GetMapping("/loginWithToken")
    public String login(Model model) {
        return "loginPage";
    }

    @PostMapping("/doLoginView")
    public ResponseEntity doLoginRedirect(@RequestBody Map<String, String> paramMap, ServerWebExchange serverWebExchange) {
        UserDetails userDetails = userDetailsService.findByUsername(paramMap.get("account")).block();
        if (userDetails.getPassword().equals(passwordEncoder.encode(passwordEncoder.encode(paramMap.get("password"))))) {
            String token = JwtUtils.generateToken(userDetails.getUsername(), userDetails.getUsername(), userDetails.getAuthorities().stream().map(auth -> auth.getAuthority()).toArray(String[]::new));
            serverWebExchange.getResponse().getCookies().set("token", ResponseCookie.from("token", token).build());
            return ResponseEntity.status(HttpStatus.SEE_OTHER).header("Location", "/").build();
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @ResponseBody
    @GetMapping("/logout")
    public String logout(ServerWebExchange serverWebExchange) {
        serverWebExchange.getResponse().getCookies().set("token", ResponseCookie.from("token", "").build());
        return "登出";
    }

    @ResponseBody
    @PostMapping("/doLogin")
    public Mono<ResponseEntity<String>> doLogin(@RequestBody AuthRequest authRequest, ServerWebExchange serverWebExchange) {
        return userDetailsService.findByUsername(authRequest.getAccount())
                .filter(userDetails -> userDetails.getPassword().equals(passwordEncoder.encode(passwordEncoder.encode(authRequest.getPassword()))))
                .map(userDetails -> ResponseEntity.ok(JwtUtils.generateToken(userDetails.getUsername(), userDetails.getUsername(), userDetails.getAuthorities()
                    .stream()
                    .map(a -> a.getAuthority()).toArray(String[]::new))))
                .switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()));
    }
}
