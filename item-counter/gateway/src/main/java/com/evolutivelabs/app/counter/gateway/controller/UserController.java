package com.evolutivelabs.app.counter.gateway.controller;

import com.evolutivelabs.app.counter.database.mysql.entity.ApUser;
import com.evolutivelabs.app.counter.database.mysql.repository.ApUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/user")
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserController {
    private final ApUserRepository apUserRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping
    public ApUser insert(@RequestBody ApUser apUser) {
        apUser.setPassword(passwordEncoder.encode(apUser.getPassword()));
        apUserRepository.save(apUser);
        return apUser;
    }
}
