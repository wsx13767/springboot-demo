package com.evolutivelabs.app.counter.gateway.service;

import com.evolutivelabs.app.counter.database.mysql.entity.ApUser;
import com.evolutivelabs.app.counter.database.mysql.repository.ApUserRepository;
import com.evolutivelabs.app.counter.database.mysql.repository.ApiRoleUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class UserServiceImpl implements ReactiveUserDetailsService {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ApUserRepository apUserRepository;
    @Autowired
    private ApiRoleUserRepository apiRoleUserRepository;

    Map<String, UserDetails> users = new HashMap<>();

    @PostConstruct
    private void init() {
        users.put("test", User.builder()
                .passwordEncoder(passwordEncoder::encode)
                .username("test")
                .password("test")
                .roles("api").build());
        users.put("admin", User.builder()
                .passwordEncoder(passwordEncoder::encode)
                .username("admin")
                .password("admin")
                .roles("ADMIN").build());
    }

    @Override
    public Mono<UserDetails> findByUsername(String s) {
        Optional<ApUser> apUserOptional = apUserRepository.findById(s);
        if (apUserOptional.isPresent()) {
            ApUser apUser = apUserOptional.get();

            return Mono.just(User.builder()
                    .passwordEncoder(passwordEncoder::encode)
                    .username(apUser.getAccount())
                    .roles(apiRoleUserRepository.findByAccount(apUser.getAccount())
                            .stream()
                            .map(role -> role.getRoleId())
                            .toArray(String[]::new))
                    .password(apUser.getPassword()).build());
        }

        return Mono.empty();
    }
}
