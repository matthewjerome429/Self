package com.cathaypacific.mmbbizrule.handler;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.MDC;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.cathaypacific.mbcommon.annotation.CheckRloc;
import com.cathaypacific.mbcommon.constants.MDCConstants;
import com.cathaypacific.mbcommon.dto.error.ErrorInfo;
import com.cathaypacific.mbcommon.enums.error.ErrorCodeEnum;
import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mbcommon.exception.MMBRunTimeException;
import com.cathaypacific.mbcommon.loging.LogAgent;
import com.cathaypacific.mbcommon.model.login.LoginInfo;
import com.cathaypacific.mbcommon.token.MbTokenCacheRepository;
import com.cathaypacific.mbcommon.token.TokenCacheKeyEnum;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrBooking;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.service.PnrInvokeService;
import com.cathaypacific.mmbbizrule.service.PaxNameIdentificationService;

@Component
@Aspect
public class RlocAccessVerifyHandler implements Ordered{
	
	private static LogAgent logger = LogAgent.getLogAgent(RlocAccessVerifyHandler.class);
	
	private int order = 10;
	
	@Autowired
	private MbTokenCacheRepository mbTokenCacheRepository;
	
	@Autowired
	private PnrInvokeService  pnrInvokeServiceImpl;
	
	@Autowired
	private PaxNameIdentificationService paxNameIdentificationServiceImpl;
	
	 @Pointcut(value = "@annotation(com.cathaypacific.mbcommon.annotation.CheckRloc)")
	 public void checkRloc(){
		 //do nothing
	 }
	
	 
    @Around(value="checkRloc()&&@annotation(checkRloc)")
	public Object verifyRloc(ProceedingJoinPoint joinPoint, CheckRloc checkRloc) throws Throwable {
		String mbToken = (String) MDC.get(MDCConstants.MMB_TOKEN_MDC_KEY);
		LoginInfo loginInfo = mbTokenCacheRepository.get(mbToken, TokenCacheKeyEnum.LOGININFO, null, LoginInfo.class);
		if (isMemberLogin(loginInfo)) {// only check rloc for member login
			checkRloc(joinPoint, checkRloc.rlocPath(),checkRloc.argIndex(), mbToken, loginInfo);
		}
		return joinPoint.proceed();

	}

    /**
     * check if member login
     * @param loginInfo
     * @return
     */
	private boolean isMemberLogin(LoginInfo loginInfo) {
		return (loginInfo != null && LoginInfo.LOGINTYPE_MEMBER.equals(loginInfo.getLoginType()));

	}
     
     /**
      * check the rloc in request if valid
      * @param joinPoint
      * @param rlocPath
      * @param mbToken
      * @param loginInfo
      */
	public void checkRloc(ProceedingJoinPoint joinPoint, String rlocPath,int argIndex, String mbToken, LoginInfo loginInfo) {
		String rloc = getRlocFromArgs(joinPoint, rlocPath,argIndex);
		if (StringUtils.isEmpty(rloc)) {
			throw new MMBRunTimeException("CheckRloc annotation, rloc is empty,request rloc path:" + rlocPath, new ErrorInfo(ErrorCodeEnum.ERR_BUSSINESS_UNKNOW));
		}
		// check cahce
		@SuppressWarnings("unchecked")
		List<String> rlocs = mbTokenCacheRepository.get(mbToken, TokenCacheKeyEnum.VALIDFLIGHTRLOCS, null, ArrayList.class);
		if (!CollectionUtils.isEmpty(rlocs) && rlocs.contains(rloc)) {// valid srloc
			return;
		}
		// check 1A if cannot find in cache
		List<String> newRlocs = getRlocsByRequestRloc(rloc, loginInfo);
		if (!newRlocs.contains(rloc)) {// may be an attack if still cannot find valid rloc form 1A
			throw new MMBRunTimeException(String.format("CheckRloc annotation, cannot find request rloc by memberid, may be an attack request!!! reqeust rolc[%s], login memberid[%s]",
					rloc, loginInfo.getMemberId()), new ErrorInfo(ErrorCodeEnum.ERR_SYSTEM));
		} else {
			rlocs = Optional.ofNullable(rlocs).orElse(new ArrayList<>());
			rlocs.addAll(newRlocs);
			mbTokenCacheRepository.add(mbToken, TokenCacheKeyEnum.VALIDFLIGHTRLOCS, null, rlocs);// add valid rloc to cache
		}

	}
     /**
      * retrieve valid rlocs from 1A
      * @param rloc
      * @return
      */
	private List<String> getRlocsByRequestRloc(String rloc, LoginInfo loginInfo) {
		
		RetrievePnrBooking pnrBooking = null;
		// retrieve pnr 
		try {
			pnrBooking = pnrInvokeServiceImpl.retrievePnrByRloc(rloc);
		} catch (Exception e) {
			// throw exception if pnr retrieve failed, 
			throw new MMBRunTimeException("CheckRloc annotation, retrieve rloc failed", new ErrorInfo(), e);
		}
		if (pnrBooking == null) {
			return Collections.emptyList();
		}
		//passenger name check, only do this check now, because the performance will bad if check member rlocs list. and cannot async because the update request 
		try {
			paxNameIdentificationServiceImpl.primaryPassengerIdentificationByInFo(loginInfo, pnrBooking);
		} catch (BusinessBaseException e) {
			logger.warn("CheckRloc annotation, passenger name identification failed.", e);
			return Collections.emptyList();
		}

		List<String> rlocs = new ArrayList<>();
		if (!StringUtils.isEmpty(pnrBooking.getOneARloc())) {
			rlocs.add(pnrBooking.getOneARloc());
		}
		if (!StringUtils.isEmpty(pnrBooking.getGdsRloc())) {
			rlocs.add(pnrBooking.getGdsRloc());
		}
		if (!StringUtils.isEmpty(pnrBooking.getSpnr())) {
			rlocs.add(pnrBooking.getSpnr());
		}
		return rlocs;
	}
     /**
	 * 
	 * @param joinPoint
	 * @param rlocPath
	 * @return
	 */
	private String getRlocFromArgs(JoinPoint joinPoint, String rlocPath,int argIndex) {
		if(StringUtils.isEmpty(rlocPath)){
			throw new MMBRunTimeException("CheckRloc annotation, rlocPath must be not empty!", new ErrorInfo(ErrorCodeEnum.ERR_BUSSINESS_UNKNOW));
		}
		String[] rlocPathArray = StringUtils.split(rlocPath,".");
		return getRlocFromDefinedPath(joinPoint.getArgs()[argIndex], rlocPathArray, 0);
	}
	/**
	 * Recursive to get the value by designate path, path separator[.]</br>
	 * e.g. request.rloc
	 * @param obj
	 * @param rlocPathArray
	 * @param fieldIndex
	 * @return
	 */
	private String getRlocFromDefinedPath(Object obj, String[] rlocPathArray, int fieldIndex) {
		try {
            //return if it is the last item(and first item) 
            if (fieldIndex == rlocPathArray.length - 1) {

				return (String) obj;
			}
            fieldIndex++;
            //return if out of bound
			if (fieldIndex >= rlocPathArray.length || obj == null) {
				return null;
			}

			Field field = obj.getClass().getDeclaredField(rlocPathArray[fieldIndex]);
			if (field == null) {// stop check if cannot find field
				throw new MMBRunTimeException("CheckRloc annotation, cannot find field:" + rlocPathArray[fieldIndex], new ErrorInfo(ErrorCodeEnum.ERR_BUSSINESS_UNKNOW));
			}
			field.setAccessible(true);
			return getRlocFromDefinedPath(field.get(obj), rlocPathArray,fieldIndex);
		} catch (Exception ex) {
			throw new MMBRunTimeException("CheckRloc annotation, parser field failed:" + rlocPathArray[fieldIndex],	new ErrorInfo(ErrorCodeEnum.ERR_BUSSINESS_UNKNOW), ex);
		}
	}


	@Override
	public int getOrder() {
		// TODO Auto-generated method stub
		return order;
	}
}