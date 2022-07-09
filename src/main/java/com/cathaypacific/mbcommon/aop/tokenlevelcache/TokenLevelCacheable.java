package com.cathaypacific.mbcommon.aop.tokenlevelcache;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.cathaypacific.mbcommon.token.TokenCacheKeyEnum;

@Documented
@Target(ElementType.METHOD)
@Retention(RUNTIME)
public @interface TokenLevelCacheable {
	/** The key in cache*/
	TokenCacheKeyEnum name();
}
