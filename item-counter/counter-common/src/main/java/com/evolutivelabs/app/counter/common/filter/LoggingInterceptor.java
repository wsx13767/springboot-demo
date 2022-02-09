package com.evolutivelabs.app.counter.common.filter;

import com.evolutivelabs.app.counter.common.annotation.PrintException;
import com.evolutivelabs.app.counter.common.config.properties.EvolutivelabsProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

/**
 * log相關的攔截設定
 * 使用了interceptor、aspect 與 controllerAdvice
 * interceptor需要在config設定攔截哪些路徑(攔截GET、DELETE)
 * controllerAdvice只要繼承與實作RequestBodyAdviceAdapter、ResponseBodyAdvice
 * RequestBodyAdviceAdapter(攔截POST、PUT的request body)
 * ResponseBodyAdvice(攔截所有正常回覆的結果)
 * Aspect(攔截出現exception的回覆結果)
 *
 * @since 2022-01-03
 * @author lucas
 */
@Order(Ordered.LOWEST_PRECEDENCE)
@Aspect
@ControllerAdvice(annotations = RestController.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class LoggingInterceptor extends RequestBodyAdviceAdapter implements HandlerInterceptor, ResponseBodyAdvice<Object> {
    private static final Logger logger = LoggerFactory.getLogger(LoggingInterceptor.class);

    private final HttpServletRequest httpServletRequest;
    private final ObjectMapper objectMapper;
    private final EvolutivelabsProperties evolutivelabsProperties;

    /**
     * 處理GET 、 DELETE 的 request
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String method = request.getMethod();
        if (HttpMethod.GET.matches(method) || HttpMethod.DELETE.matches(method)) {
            try {
                if (request.getQueryString() != null) {
                    logger.info("REQUEST DATA : {} {}, queryString=[{}]", method, request.getRequestURI(), request.getQueryString());
                } else {
                    logger.info("REQUEST DATA : {} {}", method, request.getRequestURI());
                }
            } catch (Throwable t) {
                logger.error(t.getMessage(), t);
            }
        }
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    /**
     * 處理有Request body
     * 判斷此路徑是否需要攔截，可於application.properties覆寫evolutivelabs.logging.path.filter
     * 預設為evolutivelabs.logging.path.filter=/api
     * @param methodParameter
     * @param targetType
     * @param converterType
     * @return
     */
    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return httpServletRequest.getRequestURI().startsWith(evolutivelabsProperties.getCONTEXT_PATH().concat(evolutivelabsProperties.getPATH_PATTERNS_FILTER()));
    }

    @Override
    public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        try {
            logger.info("REQUEST DATA : {} {}, queryString=[{}], payload={}", httpServletRequest.getMethod(),
                    httpServletRequest.getRequestURI(),
                    httpServletRequest.getQueryString(),
                    body);
        } catch (Throwable t) {
            logger.error(t.getMessage(), t);
        }

        return super.afterBodyRead(body, inputMessage, parameter, targetType, converterType);
    }

    /**
     * 處理共用Response部分
     * 判斷此路徑是否需要攔截，可於application.properties覆寫evolutivelabs.logging.path.filter
     * 預設為evolutivelabs.logging.path.filter=/api
     * @param returnType
     * @param converterType
     * @return
     */
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return httpServletRequest.getRequestURI().startsWith(evolutivelabsProperties.getCONTEXT_PATH().concat(evolutivelabsProperties.getPATH_PATTERNS_FILTER()));
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        try {
            if (body == null) {
                logger.info("RESPONSE DATA : status={}", ((ServletServerHttpResponse) response).getServletResponse().getStatus());
            } else {
                logger.info("RESPONSE DATA : status={}, body={}",
                        ((ServletServerHttpResponse) response).getServletResponse().getStatus(),
                        objectMapper.writeValueAsString(body));
            }
        } catch (Throwable t) {
            logger.error(t.getMessage(), t);
        }

        return body;
    }

    /**
     * exception handler package且有註解ExceptionHandler的方法做切入點
     */
    @Pointcut("execution(* com.evolutivelabs.app.counter.common.exception.handler..*.*(..)) " +
            "&& @annotation(org.springframework.web.bind.annotation.ExceptionHandler)")
    private void exceptionAdvicePointcut() {}

    /**
     * 打印出exception處理完後的結果或打印出exception錯誤
     */
    @Around("exceptionAdvicePointcut()")
    public Object logException(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        PrintException printException = method.getAnnotation(PrintException.class);
        // 必定要有回覆狀態
        ResponseStatus responseStatus = method.getAnnotation(ResponseStatus.class);
        Object obj = joinPoint.proceed();
        // exception handler 不應該再有其餘錯誤，故不再做catch，使用者在開發時能直接確認是否寫錯
        if (printException != null && printException.value()) {
            Object[] args = joinPoint.getArgs();
            Throwable ex = (Throwable) args[0];
            logger.error("RESPONSE DATA : status={}, body={}, exception={}", responseStatus.value().value(),
                    objectMapper.writeValueAsString(obj), ex);
        } else {
            logger.error("RESPONSE DATA : status={}, body={}", responseStatus.value().value(), objectMapper.writeValueAsString(obj));
        }
        return obj;
    }
}
