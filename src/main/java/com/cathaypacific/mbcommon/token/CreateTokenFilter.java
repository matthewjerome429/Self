package com.cathaypacific.mbcommon.token;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.core.annotation.Order;

import com.cathaypacific.mbcommon.constants.MMBConstants;
import com.cathaypacific.mbcommon.utils.TokenUtil;

@Order(Integer.MAX_VALUE-1)
public class CreateTokenFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		//do nothing
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		 if(request instanceof HttpServletRequest){
			HttpServletRequest httpServletRequest = (HttpServletRequest) request;
			//create token
			if(StringUtils.isEmpty(httpServletRequest.getHeader(MMBConstants.HEADER_KEY_MMB_TOKEN_ID))){
				httpServletRequest.setAttribute(MMBConstants.HEADER_KEY_MMB_TOKEN_ID,TokenUtil.createToken());
			}
		}
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
		//do nothing
	}

}
