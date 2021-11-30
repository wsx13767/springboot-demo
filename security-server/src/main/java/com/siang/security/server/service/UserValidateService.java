package com.siang.security.server.service;

import com.siang.security.server.database.entity.APUser;
import com.siang.security.server.database.entity.Role;
import com.siang.security.server.database.entity.UserRole;
import com.siang.security.server.database.repository.RoleRepository;
import com.siang.security.server.database.repository.UserRepository;
import com.siang.security.server.database.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.StreamSupport;

@Service
public class UserValidateService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserRoleRepository userRoleRepository;
    @Autowired
    private RoleRepository roleRepository;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Override
    public UserDetails loadUserByUsername(String account) throws UsernameNotFoundException {
        return userRepository.findById(account).map(user -> {
            List<Integer> roleIds = userRoleRepository
                    .findByUid(user.getId())
                    .stream()
                    .map(UserRole::getRid)
                    .toList();
            String[] roles = StreamSupport
                    .stream(roleRepository
                            .findAllById(roleIds)
                            .spliterator(), false)
                    .map(Role::getName)
                    .toArray(String[]::new);

            return User.builder()
                    .passwordEncoder(encoder::encode)
                    .username(user.getId())
                    .password(user.getPassword())
                    .roles(roles)//.authorities("USER")
                    .build();
        }).get();
    }
}
