package com.cathaypacific.mbcommon.thread;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import org.slf4j.MDC;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

public class ContextAwarePoolExecutor extends ThreadPoolTaskExecutor {
    /**
	 * 
	 */
	private static final long serialVersionUID = -7217272494280903434L;

	@Override
    public <T> Future<T> submit(Callable<T> task) {
        return super.submit(new ContextAwareCallable<T>(task, RequestContextHolder.currentRequestAttributes(),MDC.getCopyOfContextMap()));
    }

    @Override
    public <T> ListenableFuture<T> submitListenable(Callable<T> task) {
        return super.submitListenable(new ContextAwareCallable<T>(task, RequestContextHolder.currentRequestAttributes(),MDC.getCopyOfContextMap()));
    }

    @Override
    public void execute(Runnable command) {
        super.execute(wrap(command, RequestContextHolder.currentRequestAttributes(), MDC.getCopyOfContextMap()));
    }

    public static Runnable wrap(final Runnable runnable, final RequestAttributes context, final Map<String, String> mdcContextMap) {
        return new Runnable() {
            @Override
            public void run() {
                if (context != null) {
        			RequestContextHolder.setRequestAttributes(context);
        		}
        		if (mdcContextMap != null) {
        			MDC.setContextMap(mdcContextMap);
        		}
        		try {
        			runnable.run();
        		} finally {
        			RequestContextHolder.resetRequestAttributes();
        			MDC.clear();
        		}
            }
        };
    }
}
