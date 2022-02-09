package com.evolutivelabs.app.counter.common.config.properties;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Getter
@Component
public class EvolutivelabsProperties {
    @Autowired
    private ServletWebServerApplicationContext webServerAppCtxt;

    // 需紀錄request and response的log路徑
    @Value("${evolutivelabs.logging.path.filter:/api}")
    private String PATH_PATTERNS_FILTER;
    // 此application的基礎path
    @Value("${server.servlet.context-path:}")
    private String CONTEXT_PATH;
    // swagger 文件上所需顯示的path
    @Value("${evolutivelabs.swagger.path:/api}")
    private String SWAGGER_PATH;
    // 目前profile
    @Value("${spring.profiles.active:}")
    private String profile;

    @Value("${evolutivelabs.redis.hostname:localhost}")
    private String REDIS_HOSTNAME;

    @Value("${spring.application.name}")
    private String SPRING_APPLICATION_NAME;

    @Value("${evolutivelabs.gateway.hostname:localhost}")
    private String GATEWAY_HOSTNAME;

    public int getPort() {
        return webServerAppCtxt.getWebServer().getPort();
    }

}
