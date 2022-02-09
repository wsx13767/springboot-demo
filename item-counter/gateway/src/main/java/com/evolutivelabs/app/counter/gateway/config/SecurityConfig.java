package com.evolutivelabs.app.counter.gateway.config;

import com.evolutivelabs.app.counter.database.mysql.RepositoryFactory;
import com.evolutivelabs.app.counter.gateway.config.auth.AuthenticationManager;
import com.evolutivelabs.app.counter.gateway.config.auth.SecurityContextRepository;
import com.evolutivelabs.app.counter.gateway.service.RouteLocatorImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.util.Base64;

@EnableWebFluxSecurity
public class SecurityConfig {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private SecurityContextRepository securityContextRepository;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new PasswordEncoder() {

            @Override
            public String encode(CharSequence charSequence) {
                try {
                    return Base64.getEncoder().encodeToString(SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512")
                            .generateSecret(new PBEKeySpec(charSequence.toString().toCharArray(), "mysecret".getBytes(), 33, 256))
                            .getEncoded());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public boolean matches(CharSequence charSequence, String s) {
                return encode(charSequence).equals(s);
            }
        };
    }

    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder routeLocatorBuilder, RepositoryFactory repositoryFactory) {
        return new RouteLocatorImpl(routeLocatorBuilder, repositoryFactory);
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http.httpBasic().disable();
        http.formLogin().disable();
        http.csrf().disable();
        http.logout().disable();

        http.authenticationManager(authenticationManager);
        http.securityContextRepository(securityContextRepository);

        http.authorizeExchange()
                .pathMatchers("/").hasRole("ADMIN")
                .pathMatchers("/loginWithToken", "/doLoginView", "/doLogin", "/logout", "/favicon**").permitAll()
                .anyExchange().authenticated();
        return http.build();
    }
}
