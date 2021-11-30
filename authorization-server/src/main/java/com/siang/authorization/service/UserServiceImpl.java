package com.siang.authorization.service;

import com.siang.authorization.model.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class UserServiceImpl implements UserDetailsService {
    private List<UserDTO> userList;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        String password = "123456";
        userList = new ArrayList<>();
        userList.add(new UserDTO(1L, "Jim", password, 1, Arrays.asList("ADMIN")));
        userList.add(new UserDTO(2L, "Tony", password, 1, Arrays.asList("TEST")));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDTO userDTO = Optional.ofNullable(userList.stream().filter(user -> username.equals(user.getUsername())).collect(Collectors.toList()))
                .get().get(0);
        return User.builder()
                .passwordEncoder(passwordEncoder::encode)
                .username(userDTO.getUsername())
                .password(userDTO.getPassword())
                .roles(userDTO.getRoles().stream().toArray(String[]::new))
                .disabled(userDTO.getStatus() != 1)
                .build();
    }
}
