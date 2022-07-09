package com.cathaypacific.mbcommon.thread;

import java.util.concurrent.Executor;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.sleuth.instrument.async.LazyTraceExecutor;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
/**
 * Async config, will set sprint context to child thread.</br>
 * There are three environment variables:</br>
 * threadpool.corepoolsize</br>
 * threadpool.maxpoolsize</br>
 * threadpool.queuecapacity
 * @author zilong.bu
 *
 */
public class ExecutorConfig extends AsyncConfigurerSupport {
	
	@Value("${threadpool.corepoolsize:0}")
	private int corePoolSize;
	
	@Value("${threadpool.maxpoolsize:0}")
	private int maxPoolSize;
	
	@Value("${threadpool.queuecapacity:0}")
	private int queueCapacity;
	
	@Autowired
    private BeanFactory beanFactory;
	
    @Override
	public Executor getAsyncExecutor() {
		ContextAwarePoolExecutor executor = new ContextAwarePoolExecutor();
		if (corePoolSize != 0) {
			executor.setCorePoolSize(corePoolSize);
		}
		if (maxPoolSize != 0) {
			executor.setMaxPoolSize(maxPoolSize);
		}
		if (queueCapacity != 0) {
			executor.setQueueCapacity(queueCapacity);
		}
		executor.initialize();
		return new LazyTraceExecutor(beanFactory, executor);
	}
}
