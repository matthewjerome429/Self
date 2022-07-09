package com.cathaypacific.mbcommon.loging;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.perf4j.StopWatch;
import org.perf4j.slf4j.Slf4JStopWatch;
import org.slf4j.MDC;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.resource.DefaultServletHttpRequestHandler;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import com.cathaypacific.mbcommon.constants.MDCConstants;


public class ApiPerformanceLogInterceptor extends HandlerInterceptorAdapter {

	private static final LogAgent LOGGER = LogAgent.getLogAgent(ApiPerformanceLogInterceptor.class);
	
	ThreadLocal<StopWatch> stopWatchLocal = new ThreadLocal<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {        
        
    	if (handler instanceof ResourceHttpRequestHandler || handler instanceof DefaultServletHttpRequestHandler) { 
    		return true; 
    	} 
    	
        String className;
		if (handler instanceof HandlerMethod) {
			className = ((HandlerMethod) handler).getBeanType().getSimpleName();
		} else {
			className = handler.getClass().getSimpleName();
		}
		
        if (handler instanceof HandlerMethod) {
			String methodName = ((HandlerMethod) handler).getMethod().getName();
			if (!methodName.equals("health") && !methodName.equals("index")) {
				/*Object controller = ((HandlerMethod) handler).getBean();
				String path = request.getRequestURI();
				if (controller instanceof BasicErrorController) {
					BasicErrorController bec = (BasicErrorController) controller;
					ResponseEntity<Map<String, Object>> re = bec.error(request);
					if (re != null && re.getBody() != null) {
						path = (String) re.getBody().get("path");
					}
				}
				MDC.put(MDCConstants.REQUEST_SERVICEURI_MDC_KEY, path);*/

				LOGGER.info(methodName, "------ Enter API " + className + " in method " + methodName + " ------", request.getMethod(), request.getQueryString());
				
				Slf4JStopWatch stopWatch = new Slf4JStopWatch(className);	
				stopWatch.start();
				stopWatchLocal.set(stopWatch);
			}
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        super.afterCompletion(request, response, handler, ex);
        
        String className;
		if (handler instanceof HandlerMethod) {
			className = ((HandlerMethod) handler).getBeanType().getSimpleName();
		} else {
			className = handler.getClass().getSimpleName();
		}
		
        if (handler instanceof HandlerMethod) {
        	
	  		String methodName = ((HandlerMethod) handler).getMethod().getName();
				if (!methodName.equals("health") && !methodName.equals("index")) {
					StopWatch stopWatch = stopWatchLocal.get();
					stopWatch.stop();
					LOGGER.info(methodName, stopWatch.getElapsedTime(), "------ Exit API " + className + " in method " + methodName + " ------",MDC.get(MDCConstants.HTTPBODY_REQUEST_RESPONSEMDC_KEY));
				}
	        }
        MDC.remove(MDCConstants.HTTPBODY_REQUEST_RESPONSEMDC_KEY);
		/*MDC.remove(MDCConstants.REQUEST_SERVICEURI_MDC_KEY);*/
    }
}
