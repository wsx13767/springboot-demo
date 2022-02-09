package com.evolutivelabs.app.counter.api.config;

import com.evolutivelabs.app.counter.common.config.properties.EvolutivelabsProperties;
import com.evolutivelabs.app.counter.database.mysql.RepositoryFactory;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.oas.web.OpenApiTransformationContext;
import springfox.documentation.oas.web.WebMvcOpenApiTransformationFilter;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * swagger ui設定
 * 預設路徑 {uri}:{port}/{context-path}/counter/swagger-ui 或 {uri}:{port}/{context-path}/counter/swagger-ui/index.html
 * @since 2022-01-03
 * @author lucas
 */
@Configuration
@EnableOpenApi
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SwaggerConfig implements WebMvcOpenApiTransformationFilter {
    private final EvolutivelabsProperties evolutivelabsProperties;
    private final RepositoryFactory repositoryFactory;

    /**
     * 建立doc只允許在測試區或開發環境使用
     */
    @Bean
    public Docket docket() {
        return new Docket(DocumentationType.OAS_30)
//                .servers(forGateway, local) // 目前3.0.0有bug
//                .groupName("API文件")
                .enable("dev".equals(evolutivelabsProperties.getProfile()) || "test".equals(evolutivelabsProperties.getProfile()))
                .apiInfo(apiInfo())
                .useDefaultResponseMessages(false)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.evolutivelabs.app.counter"))
                .paths(PathSelectors.regex(evolutivelabsProperties.getCONTEXT_PATH().concat(evolutivelabsProperties.getSWAGGER_PATH()).concat("/.*")))
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("數貨機相關api")
                .description("提供數貨機串接的api")
                .contact(new Contact("Lucas",
                        "https://sites.google.com/evolutivelabs.com/hrsop?pli=1&authuser=0",
                        "lucas.wang@evolutivelabs.com"))
                .version("1.0.0")
                .build();
    }

    @Override
    public OpenAPI transform(OpenApiTransformationContext<HttpServletRequest> context) {
        OpenAPI openApi = context.getSpecification();
        Server gatewayServer = new Server();
        gatewayServer.setDescription("gateway");
        gatewayServer.setUrl("http://" + evolutivelabsProperties.getGATEWAY_HOSTNAME() + "/" + evolutivelabsProperties.getSPRING_APPLICATION_NAME().toUpperCase());

        List<Server> servers = new ArrayList<>(openApi.getServers());
        servers.add(gatewayServer);
        openApi.setServers(servers);
        return openApi;
    }

    @Override
    public boolean supports(DocumentationType documentationType) {
        return documentationType.equals(DocumentationType.OAS_30);
    }
}
