package com.isssr.ticketing_system.logger.aspect;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation log an operation
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LogOperation {
    String[] inputArgs() default "";            // default: no input args will be serialized

    boolean returnObject() default false;      // default: no return object will be serialized

    String opName() default "";                 // default: method name as operation name

    String tag() default "";                    // default: no tag

    boolean isEnabled() default true;

    Class<?> jsonView() default Object.class;
}
