package com.cathaypacific.mbcommon.thread;

import java.util.Map;
import java.util.concurrent.Callable;

import org.slf4j.MDC;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

public class ContextAwareCallable<T> implements Callable<T> {
    private Callable<T> task;
    private RequestAttributes context;
    private Map<String, String> mdcContextMap;

    public ContextAwareCallable(Callable<T> task, RequestAttributes context,Map<String, String> mdcContextMap) {
        this.task = task;
        this.context = context;
        this.mdcContextMap = mdcContextMap;
    }

    @Override
	public T call() throws Exception {
		if (context != null) {
			RequestContextHolder.setRequestAttributes(context);
		}
		if (mdcContextMap != null) {
			MDC.setContextMap(mdcContextMap);
		}
		try {
			return task.call();
		} finally {
			RequestContextHolder.resetRequestAttributes();
			MDC.clear();
		}
	}
}