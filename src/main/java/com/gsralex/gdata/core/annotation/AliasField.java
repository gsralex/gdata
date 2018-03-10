package com.gsralex.gdata.core.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author gsralex
 * @date 2018/2/17
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface AliasField {

    String name() default "";
}
