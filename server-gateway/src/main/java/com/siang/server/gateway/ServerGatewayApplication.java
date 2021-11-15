package com.siang.server.gateway;

import com.siang.server.gateway.database.repository.ApiRouterRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;

@EnableCaching
@EnableEurekaClient
@SpringBootApplication
public class ServerGatewayApplication {
    private static final Logger log = LoggerFactory.getLogger(ServerGatewayApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(ServerGatewayApplication.class, args);
    }

    @Bean
    public CommandLineRunner demo(ApiRouterRepository repository) {
        return (args) -> {
            log.info("---------------------{}-------------------------", "show all router start");
            repository.findAll().iterator().forEachRemaining(
                    apiRouter -> log.info(apiRouter.toString())
            );
            log.info("---------------------{}-------------------------", "show all router end");
        };
    }

}
