package com.cathaypacific.mbcommon.handler.log;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;

import com.cathaypacific.mbcommon.constants.MDCConstants;
import com.cathaypacific.mbcommon.constants.MMBConstants;

@Order(Integer.MAX_VALUE)
public class CXMDCServletFilter implements Filter {
	
 
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
    	
    	insertIntoMDC(request, response);
    	try {
            chain.doFilter(request, response);
        } finally {
            clearMDC();
        }
    }
    
    void insertIntoMDC(ServletRequest request, ServletResponse response) {
    	if (request instanceof HttpServletRequest) {
    		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
    		
    		HttpSession session = httpServletRequest.getSession();
    		if (session != null) {
    			MDC.put(MDCConstants.REQUEST_SESSION_ID_MDC_KEY, session.getId());
    		}
    		MDC.put(MDCConstants.REQUEST_SERVICEURI_MDC_KEY, httpServletRequest.getRequestURI());
    		MDC.put(MDCConstants.REQUEST_USER_AGENT_MDC_KEY, httpServletRequest.getHeader("User-Agent"));
    		MDC.put(MDCConstants.REQUEST_X_FORWARDED_FOR_MDC_KEY, httpServletRequest.getHeader("X-Forwarded-For"));
            MDC.put(MDCConstants.REQUEST_CLIENT_IP_MDC_KEY, httpServletRequest.getHeader("X-Akamai-Pragma-Client-IP"));
            MDC.put(MDCConstants.REQUEST_USER_SESSION_ID, httpServletRequest.getHeader("User-Session-Id"));
            MDC.put(MDCConstants.REQUEST_ACCESS_CHANNEL, httpServletRequest.getHeader(MMBConstants.HEADER_KEY_ACCESS_CHANNEL));
            MDC.put(MDCConstants.REQUEST_REFERER_KEY, httpServletRequest.getHeader("referer"));
            //mmb-token
            if(!StringUtils.isEmpty(httpServletRequest.getHeader(MMBConstants.HEADER_KEY_MMB_TOKEN_ID))){
            	MDC.put(MDCConstants.MMB_TOKEN_MDC_KEY, httpServletRequest.getHeader(MMBConstants.HEADER_KEY_MMB_TOKEN_ID));
            }else{
            	MDC.put(MDCConstants.MMB_TOKEN_MDC_KEY, (String)httpServletRequest.getAttribute(MMBConstants.HEADER_KEY_MMB_TOKEN_ID));
            }
            //appcode
            if(!StringUtils.isEmpty(httpServletRequest.getHeader(MMBConstants.HEADER_KEY_APP_CODE))){
            	MDC.put(MDCConstants.APP_CODE_MDC_KEY, httpServletRequest.getHeader(MMBConstants.HEADER_KEY_APP_CODE));
            }else{
            	MDC.put(MDCConstants.APP_CODE_MDC_KEY, (String)httpServletRequest.getAttribute(MMBConstants.HEADER_KEY_APP_CODE));
            }
            //accept language
            if(!StringUtils.isEmpty(httpServletRequest.getHeader(MMBConstants.HEADER_KEY_ACCEPT_LANGUAGE))){
            	MDC.put(MDCConstants.ACCEPT_LANGUAGE_MDC_KEY, httpServletRequest.getHeader(MMBConstants.HEADER_KEY_ACCEPT_LANGUAGE));
            }else{
            	MDC.put(MDCConstants.ACCEPT_LANGUAGE_MDC_KEY, (String)httpServletRequest.getAttribute(MMBConstants.HEADER_KEY_ACCEPT_LANGUAGE));
            }
            //access channel
            if(!StringUtils.isEmpty(httpServletRequest.getHeader(MMBConstants.HEADER_KEY_ACCESS_CHANNEL))){
            	MDC.put(MDCConstants.ACCESS_CHANNEL_MDC_KEY, httpServletRequest.getHeader(MMBConstants.HEADER_KEY_ACCESS_CHANNEL));
            }else{
            	MDC.put(MDCConstants.ACCESS_CHANNEL_MDC_KEY, (String)httpServletRequest.getAttribute(MMBConstants.HEADER_KEY_ACCESS_CHANNEL));
            }
    	}
    }
    
    void clearMDC() {
    	MDC.remove(MDCConstants.REQUEST_SESSION_ID_MDC_KEY);
    	MDC.remove(MDCConstants.REQUEST_USER_AGENT_MDC_KEY);
    	MDC.remove(MDCConstants.REQUEST_X_FORWARDED_FOR_MDC_KEY);
    	MDC.remove(MDCConstants.REQUEST_CLIENT_IP_MDC_KEY);
    	MDC.remove(MDCConstants.REQUEST_USER_SESSION_ID);
    	MDC.remove(MDCConstants.REQUEST_ACCESS_CHANNEL);
    	MDC.remove(MDCConstants.REQUEST_REFERER_KEY);
    	MDC.remove(MDCConstants.MMB_TOKEN_MDC_KEY);
    	MDC.remove(MDCConstants.REQUEST_SERVICEURI_MDC_KEY);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }
}
