package com.siang.security.server;

import com.siang.security.server.database.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

@EnableCaching
//@EnableEurekaClient
@SpringBootApplication
public class SecurityServerApplication {
	private static final Logger logger = LoggerFactory.getLogger(SecurityServerApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(SecurityServerApplication.class, args);
	}

	@Bean
	public CommandLineRunner demo(UserRepository userRepository, MenuRepository menuRepository, MenuRoleRepository menuRoleRepository,
								  RoleRepository roleRepository, UserRoleRepository userRoleRepository) {
		return (args) -> {
			logger.info("users: ");
			userRepository.findAll().forEach(user -> logger.info("{}", user));
			logger.info("menus: ");
			menuRepository.findAll().forEach(menu -> logger.info("{}", menu));
			logger.info("menuRole: ");
			menuRoleRepository.findAll().forEach(menuRole -> logger.info("{}", menuRole));
			logger.info("roles: ");
			roleRepository.findAll().forEach(role -> logger.info("{}", role));
			logger.info("userRole: ");
			userRoleRepository.findAll().forEach(userRole -> logger.info("{}", userRole));
		};
	}
}
