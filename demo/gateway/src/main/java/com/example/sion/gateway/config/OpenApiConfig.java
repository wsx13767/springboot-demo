package com.example.sion.gateway.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.SwaggerUiConfigParameters;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
public class OpenApiConfig {
    private final static String API_SUFFIX = "-SERVICE";

    @Bean
    @Lazy(false)
    public CommandLineRunner commandLineRunner(SwaggerUiConfigParameters swaggerUiConfigParameters,
                                               RouteDefinitionLocator locator) {
        return args -> {
          locator.getRouteDefinitions().collectList().block()
                  .stream()
                  .filter(routeDefinition -> routeDefinition.getId().endsWith(API_SUFFIX))
                  .peek(routeDefinition -> System.out.println(routeDefinition.getId()))
                  .map(routeDefinition -> {
                      String applicationName = routeDefinition.getId().split("_")[1];
                      return applicationName.substring(0, applicationName.indexOf(API_SUFFIX));
                  })
                  .forEach(group -> swaggerUiConfigParameters.addGroup(group));
        };
    }

//    @Bean
//    public OpenAPI openAPI() {
//        return new OpenAPI()
//                .info(new Info().title("gateway openapi")
//                        .description("test")
//                        .version("1.0.0")
//                        .contact(new Contact()
//                                .name("name")
//                                .email("test@fadsa.casc")));
//    }


}
