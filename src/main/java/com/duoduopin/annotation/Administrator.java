package com.duoduopin.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @description 在Controller方法上使用该注解，检查是否为管理员
 * @author z217
 * @date 2021/01/25
 * @see com.duoduopin.interceptor.AdministratorInterceptor
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Administrator {}
