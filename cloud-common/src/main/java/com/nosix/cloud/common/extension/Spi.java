package com.nosix.cloud.common.extension;

import java.lang.annotation.*;

/**
 * auther:nosix
 * nosix.me@gmail.com
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Spi {

    String name() default "";

    SpiScope scope() default SpiScope.SINGLETON;
}
