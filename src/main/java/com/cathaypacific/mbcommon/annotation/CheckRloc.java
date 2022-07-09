package com.cathaypacific.mbcommon.annotation;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Documented
@Target({ METHOD })
@Retention(RUNTIME)
public @interface CheckRloc {
	/**
	 * e.g. request.rloc 
	 * @return
	 */
	String rlocPath() ;
	/**
	 * The index of rloc in target method.</br>
	 * From 0
	 * @return
	 */
	int argIndex() ;
}
