package com.siang.security.server.filter;

import com.siang.security.server.model.ApiToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

public class FriendlyFilter extends OncePerRequestFilter {
    private AuthenticationManager authenticationManager;

    public FriendlyFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        SecurityContextHolder.getContext().setAuthentication(new ApiToken() {
//            @Override
//            public Collection<? extends GrantedAuthority> getAuthorities() {
//                return Arrays.asList(
//                        new SimpleGrantedAuthority("ROLE_ADMIN")
//                );
//            }
//        });
//        filterChain.doFilter(request, response);
        if (hasAuthenticated()) {
            filterChain.doFilter(request, response);
            return;
        }
        Authentication requestToken = asAuthentication(request);
        if (requestToken == null) {
            filterChain.doFilter(request, response);
            return;
        }

        Authentication authentication = authenticationManager.authenticate(requestToken);
        if (authentication == null || !authentication.isAuthenticated()) {
            filterChain.doFilter(request, response);
            return;
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);


    }

    private Authentication asAuthentication(HttpServletRequest request) {
        String token = request.getHeader("x-twjug-authorization");
        if (token == null) {
            return null;
        }
        return new ApiToken(token);
    }

    private boolean hasAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.isAuthenticated();
    }
}
