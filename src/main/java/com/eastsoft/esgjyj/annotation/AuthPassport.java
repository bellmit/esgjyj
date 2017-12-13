package com.eastsoft.esgjyj.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 免登陆牌照，被注解的请求将跳过登陆验证，直接转向视图。
 * @author chenkai
 * @since 0.0.1-SNAPSHOT
 * @version 1.0.0
 */
@Documented
@Inherited
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthPassport {
    boolean validate() default false;
}