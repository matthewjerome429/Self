/**
 * Cathay Pacific Confidential and Proprietary.
 * Copyright 2011, Cathay Pacific Airways Limited
 * All rights reserved.
 *
 */
package com.cathaypacific.mbcommon.loging;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to be used by methods which require the Spring AOP to use the
 * around-advice interceptor and send the events to be logged.
 * <p/>
 * It is targeted to be used only by methods, hence it is annotated as
 * follows:
 * <pre>
 * &#064;Target(value = ElementType.METHOD)
 * </pre>
 * <p/>
 * It has a Runtime retention policy, which indicates the value be retained
 * by the VM at runtime, and may be read reflectively. It is annotated as
 * follows:
 * <pre>
 * &#064;Retention (value = RetentionPolicy.RUNTIME)
 * </pre>
 */
@Target(value = ElementType.METHOD)
@Retention (value = RetentionPolicy.RUNTIME)
public @interface LogPerformance {
	String message();
}
