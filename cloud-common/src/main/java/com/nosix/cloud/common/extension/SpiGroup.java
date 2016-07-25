package com.nosix.cloud.common.extension;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface SpiGroup {
	/**
	 * 分组名称
	 */
	String name() default "";
	
	/**
	 * sequence越小，在返回的list中的位置越靠前，尽量使用 0-100以内的数字
	 * 
	 */
	int sequence() default 20;
}