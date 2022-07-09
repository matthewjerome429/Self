package com.cathaypacific.mbcommon.web;

import java.io.IOException;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;

import com.cathaypacific.mbcommon.constants.MMBConstants;

 
@Order(Integer.MAX_VALUE-2)
@WebFilter(urlPatterns="")
public class RequestHeaderParaCheckFilter implements Filter{

	@Value("#{'${app.code.list}'.split(',')}")
	private List<String> appCodeList;
	
	@Value("#{'${app.access.channel.list}'.split(',')}")
	private List<String> accessChannelList;
	
	//private static final LogAgent LOGGER = LogAgent.getLogAgent(RequestBodyLogger.class);
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		//do nothing
		
	}
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		 if(request instanceof HttpServletRequest){
			HttpServletRequest httpServletRequest = (HttpServletRequest) request;
			String reqAppCode = httpServletRequest.getHeader(MMBConstants.HEADER_KEY_APP_CODE);
			//check appcode
			if(StringUtils.isEmpty(reqAppCode)||!appCodeList.contains(reqAppCode)){
				httpServletRequest.setAttribute(MMBConstants.HEADER_KEY_APP_CODE,MMBConstants.DEFAULT_APP_CODE);
				//LOGGER.warn(String.format("Request appCode empty, will use default appCode:[%s]", MMBConstants.DEFAULT_APP_CODE));
			} 
			//language
			if(StringUtils.isEmpty(httpServletRequest.getHeader(MMBConstants.HEADER_KEY_ACCEPT_LANGUAGE))){
				httpServletRequest.setAttribute(MMBConstants.HEADER_KEY_ACCEPT_LANGUAGE,MMBConstants.DEFAULT_ACCEPT_LANGUAGE);
				//LOGGER.warn(String.format("Request Accept-Language empty, will use default language:[%s]", MMBConstants.DEFAULT_ACCEPT_LANGUAGE));
			}
			
			// no need to check access channel
		}
		chain.doFilter(request, response);
	}
	 

	@Override
	public void destroy() {
		//do nothing
		
	}
	
}