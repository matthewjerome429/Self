/**
 * Cathay Pacific Confidential and Proprietary.
 * Copyright 2011, Cathay Pacific Airways Limited
 * All rights reserved.
 *
 */
package com.cathaypacific.mbcommon.loging;

import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.perf4j.StopWatch;
import org.perf4j.slf4j.Slf4JStopWatch;

public class PerformanceLogInterceptor {

	ThreadLocal<StopWatch> stopWatchLocal = new ThreadLocal<StopWatch>();
	
    public Object logPerformance(final ProceedingJoinPoint pjp) throws Throwable {
        try {
            logEntry(pjp);

            final Object mockResponse = pjp.proceed();

            return mockResponse;
        } finally {
            logExit(pjp);
        }
    }
    
    /**
     * Convenience method to log details at the entry of the intercepted
     * method.
     *
     * @param pjp - The ProceedingJoinPoint provided by Spring AOP.
     */
    private void logEntry(final ProceedingJoinPoint pjp) {
        if ( !isAnnotatedForEntryOrExit(pjp)) {
            return;
        }
        Slf4JStopWatch stopWatch = new Slf4JStopWatch();	
		stopWatch.start();
		stopWatchLocal.set(stopWatch);
    }

    /**
     * Convenience method to log details at the exit of the intercepted
     * method.
     *
     * @param pjp - The ProceedingJoinPoint provided by Spring AOP.
     */
    private void logExit(final ProceedingJoinPoint pjp) {
        if (!isAnnotatedForEntryOrExit(pjp)) {
            return;
        }

        Class<?> clazz = this.getTargetClass(pjp);
        //String className = clazz.getName();
        String methodName = this.getMethodFromJoinPoint(pjp).getName();

        final LogAgent LOGGER = LogAgent.getLogAgent(clazz);
        
        StopWatch stopWatch = stopWatchLocal.get();
		stopWatch.stop();
		
        LOGGER.info(methodName, stopWatch.getElapsedTime(), getMessage(pjp));
    }
    
    private Class<?> getTargetClass(final ProceedingJoinPoint pjp) {
        Class<?> paramClass = null;

        if (pjp.getTarget().getClass() != null) {
            paramClass = pjp.getTarget().getClass();
        }

        return paramClass;
    }


    /**
     * Checks whether the intercepted method is annotated with the
     * 'RequireLogging' annotation. Returns 'true' if annotated, else
     * 'false'.
     *
     * @param pjp - The ProceedingJoinPoint provided by Spring AOP.
     * @return If annotated with 'RequireLogging', returns 'true', else
     * 'false'.
     */
    private boolean isAnnotatedForEntryOrExit(final ProceedingJoinPoint pjp) {
        boolean flag = false;
        final Method method = getMethodFromJoinPoint(pjp);

        if (method.isAnnotationPresent(LogPerformance.class)) {
            flag = true;
        }

        return flag;
    }
    
    private String getMessage(final ProceedingJoinPoint pjp) {
        final Method method = getMethodFromJoinPoint(pjp);
        String message = null;
        if (method.isAnnotationPresent(LogPerformance.class)) {
        	LogPerformance annotation = method.getAnnotation(LogPerformance.class);
        	message = annotation.message();
        }
        return message;
    }
    
    private Method getMethodFromJoinPoint(final ProceedingJoinPoint pjp) {
        final MethodSignature signature = (MethodSignature) pjp.getSignature();
        final Method method = signature.getMethod();

        return method;
    }
}
