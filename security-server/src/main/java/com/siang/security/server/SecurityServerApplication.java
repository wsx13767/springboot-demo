package com.siang.security.server;

import com.siang.security.server.database.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
public class SecurityServerApplication {
	private static final Logger logger = LoggerFactory.getLogger(SecurityServerApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(SecurityServerApplication.class, args);
	}

	@Bean
	public CommandLineRunner demo(UserRepository userRepository) {
		return (args) -> {
			userRepository.findAll().forEach(user -> logger.info("{}", user));
		};
	}
}
