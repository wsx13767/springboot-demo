package com.evolutivelabs.app.counter.common.config;

import com.evolutivelabs.app.counter.common.config.properties.EvolutivelabsProperties;
import com.evolutivelabs.app.counter.common.filter.LoggingInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

/**
 * 整個專案相關設定，如interceptor
 * @since 2021-11-17
 * @author lucas
 */
@Configuration
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EVOConfig implements WebMvcConfigurer {
    private final LoggingInterceptor loggingInterceptor;
    private final EvolutivelabsProperties evolutivelabsProperties;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loggingInterceptor).addPathPatterns(evolutivelabsProperties.getPATH_PATTERNS_FILTER().concat("/**"));
    }

}
