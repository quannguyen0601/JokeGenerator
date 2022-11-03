package com.quan.filter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RateLimiterByQueryRequired {
    String parameter();

    int rateLimit() default  5;

    int timeLimit() default 60;



}
