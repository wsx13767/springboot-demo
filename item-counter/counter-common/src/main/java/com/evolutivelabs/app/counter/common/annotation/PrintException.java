package com.evolutivelabs.app.counter.common.annotation;

import java.lang.annotation.*;

/**
 * 於GlobalExceptionHandler註解是否打印exception log
 * @since 2022-01-03
 * @author lucas
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PrintException {
    boolean value() default true;
}
