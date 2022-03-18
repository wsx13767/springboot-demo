package com.example.sion.config.service.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.cloud.config.environment.Environment;
import org.springframework.cloud.config.environment.PropertyValueDescriptor;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 如果有環境變數或系統變數就覆蓋掉原有的設定檔
 */
@Aspect
@Component
//@ConditionalOnBean(NativeEnvironmentRepository.class) TODO: 加上後無法作用，但確定NativeEnvironmentRepository bean已經在beanFactory
public class NativeEnvironmentRepositoryAOP {

    @Around("execution(public * org.springframework.cloud.config.server.environment.NativeEnvironmentRepository.findOne(..))")
    private Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Environment env = (Environment) joinPoint.proceed();
        StandardEnvironment standardEnvironment = new StandardEnvironment();
        env.getPropertySources().forEach(
                propertySource -> {
                    Map<Object, Object> map= (Map<Object, Object>) propertySource.getSource();
                    map.entrySet()
                            .stream()
                            .forEach(entry -> {
                                Object obj = entry.getValue();
                                Object value = null;
                                if (obj instanceof PropertyValueDescriptor) {
                                    value = new PropertyValueDescriptor(standardEnvironment.resolvePlaceholders(obj.toString()),
                                            ((PropertyValueDescriptor) obj).getOrigin());
                                } else {
                                    value = standardEnvironment.resolvePlaceholders(obj.toString());
                                }

                                entry.setValue(value);
                            });
                }
        );

        return env;
    }
}
