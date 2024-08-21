package com.winning.hmap.portal.dict.aop;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface WriteLog {

    String type();

    String description();

}
