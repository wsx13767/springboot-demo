package com.siang.security.server.service;

import com.siang.security.server.database.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserValidateService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Override
    public UserDetails loadUserByUsername(String account) throws UsernameNotFoundException {
        return userRepository.findById(account).map(user -> User.builder()
                .passwordEncoder(encoder::encode)
                .username(user.getId())
                .password(user.getPassword())
                .roles("USER").authorities("USER")
                .build()).get();
    }
}
