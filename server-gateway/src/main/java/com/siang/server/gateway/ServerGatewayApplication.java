package com.siang.server.gateway;

import com.siang.server.gateway.database.entity.ApiRouter;
import com.siang.server.gateway.database.repository.ApiRouterRepository;
import io.r2dbc.spi.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer;
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator;

import java.time.Duration;
import java.util.Arrays;

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
            repository.saveAll(Arrays.asList(
                    new ApiRouter("provider_info", "lb://SERVER-PROVIDER", "provider", "/info"),
                    new ApiRouter("provider_hello", "lb://SERVER-PROVIDER", "provider", "/hello")
            )).blockLast(Duration.ofSeconds(10));
            repository.findAll().doOnNext(apiRouter -> {
                log.info(apiRouter.toString());
            }).blockLast(Duration.ofSeconds(10));
        };
    }

}
