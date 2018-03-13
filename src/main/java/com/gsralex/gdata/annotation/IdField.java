package com.gsralex.gdata.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author gsralex
 * 2018/2/17
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface IdField {
    String name() default "";

    boolean autoGenerate() default true;
}
