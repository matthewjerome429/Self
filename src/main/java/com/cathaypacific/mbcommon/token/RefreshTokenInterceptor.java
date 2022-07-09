package com.cathaypacific.mbcommon.token;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.cathaypacific.mbcommon.constants.MMBConstants;

public class RefreshTokenInterceptor implements HandlerInterceptor {
	
	//private static LogAgent logger = LogAgent.getLogAgent(RefreshTokenInterceptor.class);
	
	@Autowired
	private MbTokenCacheRepository mbTokenCacheRepository;
	
 
	@Override
	public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		// do nothing
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object arg2, ModelAndView arg3)
			throws Exception {
		// do nothing
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object arg2) throws Exception {
		String mmbToken = request.getHeader(MMBConstants.HEADER_KEY_MMB_TOKEN_ID);
		if(!StringUtils.isEmpty(mmbToken)){
			/*logger.error("Can not find MMB-Token in the request header");
			throw new MMBRunTimeException("Can not find MMB-Token in the request header", new ErrorInfo(ErrorCodeEnum.ERR_TOKEN_INVALID),HttpStatus.UNAUTHORIZED);
		}else{*/
			mbTokenCacheRepository.refreshExpire(mmbToken);
		}
		return true;

	}
 
}
