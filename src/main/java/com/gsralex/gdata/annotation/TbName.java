package com.gsralex.gdata.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author gsralex
 * 2018/2/18
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface TbName {

    String name() default "";
}
