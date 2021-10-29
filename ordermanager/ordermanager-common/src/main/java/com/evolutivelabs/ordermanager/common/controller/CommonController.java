package com.evolutivelabs.ordermanager.common.controller;

import javax.servlet.http.HttpServletRequest;

public abstract class CommonController {
    private HttpServletRequest httpServletRequest;

    public CommonController(HttpServletRequest httpServletRequest) {
        this.httpServletRequest = httpServletRequest;
    }
}
