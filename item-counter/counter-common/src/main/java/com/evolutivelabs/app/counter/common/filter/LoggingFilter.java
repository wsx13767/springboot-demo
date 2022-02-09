package com.evolutivelabs.app.counter.common.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * filter可使用來處理request與response
 * 每次request 會將 client ip帶入log紀錄，並於response時清除
 * @since 2022-01-03
 * @author lucas
 */
@Component
@WebFilter
public class LoggingFilter extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(LoggingFilter.class);
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        MDC.put("USER_IP", request.getRemoteAddr());
        try {
            filterChain.doFilter(request, response);
        } finally {
            MDC.remove("USER_IP");
        }
    }
}
