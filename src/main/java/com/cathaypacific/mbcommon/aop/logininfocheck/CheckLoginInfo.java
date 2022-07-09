package com.cathaypacific.mbcommon.aop.logininfocheck;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Documented
@Target({ METHOD })
@Retention(RUNTIME)
public @interface CheckLoginInfo {
 
	/** require member login */
	boolean memberLoginRequired() default false;
	
	/** require nonmember login */
	boolean nonMemberLoginRequired() default false;
	
	/** require rloc login */
	boolean rlocLoginRequired() default false;

	/** require eticket login */
	boolean eticketLoginRequired() default false;
	 
}
