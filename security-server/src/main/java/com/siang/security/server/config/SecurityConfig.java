package com.siang.security.server.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.siang.security.server.database.repository.UserRepository;
import com.siang.security.server.filter.CustomSecurityMetadataSource;
import com.siang.security.server.filter.JWTAuthenticationFilter;
import com.siang.security.server.filter.LoginFilter;
import com.siang.security.server.service.UserValidateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.UrlAuthorizationConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;

@Configuration
@EnableWebSecurity
@EnableRedisHttpSession
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);
    @Autowired
    private UserValidateService userValidateService;
    @Autowired
    private JWTAuthenticationFilter jwtAuthenticationFilter;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CustomSecurityMetadataSource customSecurityMetadataSource;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
//        ProviderManager manager = new ProviderManager(new KaptchaAuthenticationProvider()); // 驗證碼驗證
        return super.authenticationManagerBean();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/h2**/**");
    }

    @Bean
    LoginFilter loginFilter() throws Exception {
        LoginFilter loginFilter = new LoginFilter();
        loginFilter.setAuthenticationManager(authenticationManagerBean());
        loginFilter.setAuthenticationSuccessHandler((req, resp, auth) -> {
            resp.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
            PrintWriter out = resp.getWriter();
            out.write(new ObjectMapper().writeValueAsString(auth));
        });
        return loginFilter;
    }

//    @Bean
//    HttpFirewall httpFirewall() {
//        StrictHttpFirewall strictHttpFirewall = new StrictHttpFirewall();
//        Set<String> allowedHttpMethods = new HashSet<>();
//        allowedHttpMethods.add(HttpMethod.POST.name());
//        allowedHttpMethods.add(HttpMethod.GET.name());
//        strictHttpFirewall.setAllowedHttpMethods(allowedHttpMethods);
//        return strictHttpFirewall;
//    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.authenticationProvider(new TwjugAuthenticationProvider());
//        auth.userDetailsService(userValidateService);
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userValidateService);
        daoAuthenticationProvider.setPasswordEncoder(new BCryptPasswordEncoder());
        auth.authenticationProvider(
                daoAuthenticationProvider
        );
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        ApplicationContext applicationContext = http.getSharedObject(ApplicationContext.class);
        http.apply(new UrlAuthorizationConfigurer<>(applicationContext))
                .withObjectPostProcessor(
                        new ObjectPostProcessor<FilterSecurityInterceptor>() {
                            @Override
                            public <O extends FilterSecurityInterceptor> O postProcess(O object) {
                                object.setSecurityMetadataSource(customSecurityMetadataSource);
                                return object;
                            }
                        });
        http.formLogin()
                .and()
                .csrf().disable();
//        http.authorizeRequests()
//                .anyRequest().authenticated()
//                .and()
//                .formLogin()
//                .and()
//                .csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
//                .and()
//                .sessionManagement()
//                .maximumSessions(1);
    }


//    @Bean
//    public CookieSerializer cookieSerializer() {
//        DefaultCookieSerializer cookieSerializer = new DefaultCookieSerializer();
//        cookieSerializer.setSameSite("strict");
//        return cookieSerializer;
//    }

}
