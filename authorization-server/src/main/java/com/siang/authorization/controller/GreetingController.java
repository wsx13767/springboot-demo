package com.siang.authorization.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

@Controller
public class GreetingController {
    @GetMapping("/greeting")
    public String greeting(@AuthenticationPrincipal OidcUser oidcUser, Model model,
                           @RegisteredOAuth2AuthorizedClient("okta")OAuth2AuthorizedClient oAuth2AuthorizedClient) {
        model.addAttribute("username", oidcUser.getEmail());
        model.addAttribute("idToken", oidcUser.getIdToken());
        model.addAttribute("accessToken", oAuth2AuthorizedClient.getAccessToken());

        return "greeting";
    }

    public Predicate<String> test = e -> true;
}
