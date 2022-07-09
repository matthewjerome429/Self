package com.cathaypacific.mbcommon.aop.logininfocheck;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.MDC;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.cathaypacific.mbcommon.constants.MDCConstants;
import com.cathaypacific.mbcommon.dto.error.ErrorInfo;
import com.cathaypacific.mbcommon.enums.error.ErrorCodeEnum;
import com.cathaypacific.mbcommon.exception.MMBRunTimeException;
import com.cathaypacific.mbcommon.loging.LogAgent;
import com.cathaypacific.mbcommon.model.login.LoginInfo;
import com.cathaypacific.mbcommon.token.MbTokenCacheRepository;
import com.cathaypacific.mbcommon.token.TokenCacheKeyEnum;

@Component
@Aspect
public class CheckLoginInfoHandler implements Ordered{

	private int order = 1;
	
	private static LogAgent logger = LogAgent.getLogAgent(CheckLoginInfoHandler.class);
	private static String loginTypeErrorMessage = "CheckLoginInfo annotation, login type not matched, required login type[%s], current login type[%s].";

	@Autowired
	private MbTokenCacheRepository mbTokenCacheRepository;

	@Pointcut(value = "@annotation(com.cathaypacific.mbcommon.aop.logininfocheck.CheckLoginInfo)")
	public void checkLoginInfo() {
		// do nothing
	}

	@Around(value = "checkLoginInfo()&&@annotation(checkLoginInfo)")
	public Object checkLoginInfo(ProceedingJoinPoint joinPoint, CheckLoginInfo checkLoginInfo) throws Throwable {
		//Get Token from MDC
		String mbToken = (String) MDC.get(MDCConstants.MMB_TOKEN_MDC_KEY);
		
		if(StringUtils.isEmpty(mbToken)){
			logger.warn("Cannot find mmbtoken from MDC, maybe this api should not check mmb token?");
			throw new MMBRunTimeException("CheckLoginInfo annotation, cannot find mmbtoken from MDC.", new ErrorInfo(ErrorCodeEnum.ERR_TOKEN_INVALID),HttpStatus.UNAUTHORIZED);	
		}
		//if need to check login type
		boolean checkLoginType = checkLoginInfo.eticketLoginRequired() || checkLoginInfo.memberLoginRequired() || checkLoginInfo.rlocLoginRequired() || checkLoginInfo.nonMemberLoginRequired();
		//if need to populate login info
		Integer loginInfoIndex = getIndexOfLoginInfo(joinPoint);
		//Receive login info is need check login type or 
		if(checkLoginType || loginInfoIndex != null){
			LoginInfo loginInfo = mbTokenCacheRepository.get(mbToken, TokenCacheKeyEnum.LOGININFO, null, LoginInfo.class);
			checkLoginInfoNotEmpty(loginInfo);
			if(checkLoginType){
				checkLoginType(loginInfo, checkLoginInfo);
			}
			if(loginInfoIndex != null){
				joinPoint.getArgs()[loginInfoIndex]=loginInfo;
			}
		}else if(!mbTokenCacheRepository.hasTokenKey(mbToken, TokenCacheKeyEnum.LOGININFO, null)){
			throw new MMBRunTimeException("CheckLoginInfo annotation, cannot find mmbtoken in redis.", new ErrorInfo(ErrorCodeEnum.ERR_TOKEN_INVALID),HttpStatus.UNAUTHORIZED);	
			}
	 
		// checkLoginInfo.memebrLogin()
		return joinPoint.proceed(joinPoint.getArgs());
	}
	
	/**
	 * throw exception if login type not same as required
	 * @param loginInfo
	 * @param checkLoginInfo
	 */
	private void checkLoginType(LoginInfo loginInfo,CheckLoginInfo checkLoginInfo){
		if(checkLoginInfo.memberLoginRequired() && !LoginInfo.LOGINTYPE_MEMBER.equals(loginInfo.getLoginType())){
			throw new MMBRunTimeException(String.format(loginTypeErrorMessage, LoginInfo.LOGINTYPE_MEMBER,loginInfo.getLoginType()), new ErrorInfo(ErrorCodeEnum.ERR_TOKEN_INVALID),HttpStatus.UNAUTHORIZED);	
		}
		if(checkLoginInfo.eticketLoginRequired() && !LoginInfo.LOGINTYPE_ETICKET.equals(loginInfo.getLoginType())){
			throw new MMBRunTimeException(String.format(loginTypeErrorMessage, LoginInfo.LOGINTYPE_ETICKET,loginInfo.getLoginType()), new ErrorInfo(ErrorCodeEnum.ERR_TOKEN_INVALID),HttpStatus.UNAUTHORIZED);	
		}
		if(checkLoginInfo.rlocLoginRequired() && !LoginInfo.LOGINTYPE_RLOC.equals(loginInfo.getLoginType())){
			throw new MMBRunTimeException(String.format(loginTypeErrorMessage, LoginInfo.LOGINTYPE_RLOC,loginInfo.getLoginType()), new ErrorInfo(ErrorCodeEnum.ERR_TOKEN_INVALID),HttpStatus.UNAUTHORIZED);	
		}
		if(checkLoginInfo.nonMemberLoginRequired() && LoginInfo.LOGINTYPE_MEMBER.equals(loginInfo.getLoginType())){
			throw new MMBRunTimeException(String.format(loginTypeErrorMessage, "nun memebr",loginInfo.getLoginType()), new ErrorInfo(ErrorCodeEnum.ERR_TOKEN_INVALID),HttpStatus.UNAUTHORIZED);	
		}
	}
	
	/**
	 * Throw exception if login is empty 
	 * @param loginInfo
	 */
	private void checkLoginInfoNotEmpty(LoginInfo loginInfo){
		if(loginInfo == null){
			throw new MMBRunTimeException("CheckLoginInfo annotation,invalid token, cannot find loginInfo from redis.", new ErrorInfo(ErrorCodeEnum.ERR_TOKEN_INVALID),HttpStatus.UNAUTHORIZED);	
		}
	}
	
	/**
	 * Get the login index of in args, only CheckLoginInfo will be find
	 * @param joinPoint
	 * @return
	 */
	private Integer getIndexOfLoginInfo(ProceedingJoinPoint joinPoint) {

		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Method method = signature.getMethod();
		Annotation[][] methodAnnotations = method.getParameterAnnotations();
		for (int i = 0; i < methodAnnotations.length; i++) {
			for (int j = 0; j < methodAnnotations[i].length; j++) {
				if (methodAnnotations[i][j].annotationType().isAssignableFrom(LoginInfoPara.class)) {
					return i;
				}
			}
		}
		return null;
	}

	@Override
	public int getOrder() {
		return order;
	}
}
