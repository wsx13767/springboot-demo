package com.example.sion.gateway.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.GroupedOpenApi;
import org.springdoc.core.SwaggerUiConfigParameters;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.context.SmartLifecycle;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableDiscoveryClient
public class OpenApiConfig {

    @Bean
    public CommandLineRunner commandLineRunner(SwaggerUiConfigParameters swaggerUiConfigParameters,
                                               RouteDefinitionLocator locator) {
        return args -> {
          locator.getRouteDefinitions().collectList().block()
                  .stream().filter(routeDefinition -> routeDefinition.getId().endsWith("HELLO"))
                  .peek(routeDefinition -> System.out.println(routeDefinition.getId()))
                  .map(routeDefinition -> routeDefinition.getId().split("_")[1])
                  .forEach(group -> swaggerUiConfigParameters.addGroup(group));
        };
    }

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info().title("gateway openapi")
                        .description("test")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("name")
                                .email("test@fadsa.casc")));
    }


}
