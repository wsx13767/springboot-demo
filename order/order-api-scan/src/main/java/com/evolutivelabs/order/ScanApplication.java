package com.evolutivelabs.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.PropertySource;

@EnableEurekaClient
@EnableDiscoveryClient
@SpringBootApplication
public class ScanApplication {
    public static void main(String[] args) {
        SpringApplication.run(ScanApplication.class, args);
    }

}
