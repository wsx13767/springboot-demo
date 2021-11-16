package com.siang.security.server.config;

import com.siang.security.server.database.repository.UserRepository;
import com.siang.security.server.filter.JWTAuthenticationFilter;
import com.siang.security.server.service.UserValidateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);
    @Autowired
    private UserValidateService userValidateService;
    @Autowired
    private JWTAuthenticationFilter jwtAuthenticationFilter;
    @Autowired
    private UserRepository userRepository;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        logger.info("init user detailsService");
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

        return username -> userRepository.findById(username).map(
                user -> User.builder()
                        .passwordEncoder(bCryptPasswordEncoder::encode)
                        .username(user.getId())
                        .password(user.getPassword())
                        .roles("USER").authorities("USER")
                        .build()
        ).get();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.authenticationProvider(new TwjugAuthenticationProvider());
//        auth.userDetailsService(userValidateService)
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userValidateService);
        daoAuthenticationProvider.setPasswordEncoder(new BCryptPasswordEncoder());
        auth.authenticationProvider(
                daoAuthenticationProvider
        );
//        auth.userDetailsService(userDetailsService())
//                .passwordEncoder(new BCryptPasswordEncoder());
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        http.addFilterBefore(new FriendlyFilter(authenticationManager()), LogoutFilter.class);
        http
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/users").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.GET, "/users/*").authenticated()
                .antMatchers(HttpMethod.GET).permitAll()
                .antMatchers(HttpMethod.POST, "/users").permitAll()
                .antMatchers(HttpMethod.POST, "/auth").permitAll()
                .antMatchers(HttpMethod.POST, "/auth/parse").permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .httpBasic()
                .and()
                .csrf().disable();
    }
}
