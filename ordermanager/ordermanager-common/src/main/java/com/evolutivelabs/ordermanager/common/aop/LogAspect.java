package com.evolutivelabs.ordermanager.common.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LogAspect {

    private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);

    @Around("execution(public * com.evolutivelabs.ordermanager.api.*.controller..*Controller.*(..))")
    public Object intercept(ProceedingJoinPoint joinPoint) throws Throwable {
        logger.debug("===================== Start controller intercept ======================");
        logger.debug("Class Method : {}.{}", joinPoint.getSignature().getDeclaringType(), joinPoint.getSignature().getName());
        for (Object obj : joinPoint.getArgs()) {
            logger.debug("args: {}", obj);
        }
        logger.debug("====================== End controller intercept =======================");
        return joinPoint.proceed();
    }
}
