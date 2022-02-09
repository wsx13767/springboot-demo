package com.evolutivelabs.app.counter.gateway.model;

import lombok.Data;

@Data
public class AuthRequest {
    private String account;
    private String password;
}
