package com.cathaypacific.mbcommon.aop.tokenlevelcache;

import java.lang.reflect.Method;

import org.apache.log4j.MDC;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import com.cathaypacific.mbcommon.constants.MDCConstants;
import com.cathaypacific.mbcommon.token.MbTokenCacheRepository;
import com.cathaypacific.mbcommon.utils.EncodeUtil;

@Component
@Aspect
public class TokenLevelCacheHandler implements Ordered{
	
	private int order = 99;
	
	@Autowired
	private MbTokenCacheRepository mbTokenCacheRepository;

	@Pointcut(value = "@annotation(com.cathaypacific.mbcommon.aop.tokenlevelcache.TokenLevelCacheable)")
	public void tokenLevelCache() {
		// do nothing
	}
	
	@Around(value = "tokenLevelCache()&&@annotation(tokenLevelCacheable)")
	public Object tokenLevelCache(ProceedingJoinPoint joinPoint, TokenLevelCacheable tokenLevelCacheable) throws Throwable {
		// Get Token from MDC
		String mmbToken = (String) MDC.get(MDCConstants.MMB_TOKEN_MDC_KEY);
		Object result = null;
		
		Method method = getMethod(joinPoint);
		String additionalKey = generateKey(joinPoint, method);
		// check if has data in cache
		if (mbTokenCacheRepository.hasTokenKey(mmbToken, tokenLevelCacheable.name(), additionalKey)) {
			result = mbTokenCacheRepository.get(mmbToken, tokenLevelCacheable.name(), additionalKey, method.getReturnType());
		} else {
			result = joinPoint.proceed();
			if (result != null) {
				mbTokenCacheRepository.add(mmbToken, tokenLevelCacheable.name(), additionalKey, result);
			}
		}

		return result;
	}
	
	/**
	 * Generate unique key by method and parameters
	 * @return
	 */
	private String generateKey(ProceedingJoinPoint joinPoint,Method method){
		StringBuilder sb = new StringBuilder();
		sb.append(joinPoint.getTarget().getClass().getName());
		sb.append(".");
		sb.append(method.getName());
		for (Object obj : joinPoint.getArgs()) {
			sb.append("-");
			sb.append(EncodeUtil.encoderByMd5UTF8(obj));
		}
		return EncodeUtil.encoderByMd5UTF8(sb.toString());
	}
	/**
	 * Get response type of method from ProceedingJoinPoint
	 * @param joinPoint
	 * @return
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 */
	private Method getMethod(ProceedingJoinPoint joinPoint) throws NoSuchMethodException {

		Signature sig = joinPoint.getSignature();
		if (!(sig instanceof MethodSignature)) {
			throw new IllegalArgumentException("tokenLevelCacheable only used for method.");
		}
		MethodSignature msig = (MethodSignature) sig;
		Object target = joinPoint.getTarget();
		return target.getClass().getMethod(msig.getName(), msig.getParameterTypes());
	}
	
	@Override
	public int getOrder() {
		return order;
	}
}
