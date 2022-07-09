package com.cathaypacific.mmbbizrule.oneaservice.handler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import com.cathaypacific.mbcommon.constants.OneAErrorConstants;
import com.cathaypacific.mbcommon.dto.error.ErrorInfo;
import com.cathaypacific.mbcommon.enums.error.ErrorTypeEnum;
import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mbcommon.exception.ExpectedException;
import com.cathaypacific.mbcommon.exception.OneAErrorsException;
import com.cathaypacific.mbcommon.exception.UnexpectedException;
import com.cathaypacific.mbcommon.loging.LogAgent;
import com.cathaypacific.mmbbizrule.config.BizRuleConfig;
import com.cathaypacific.mmbbizrule.constant.TBConstants;
import com.cathaypacific.mmbbizrule.db.dao.TB1AErrorHandleDao;
import com.cathaypacific.mmbbizrule.db.model.TB1AErrorHandle;
import com.cathaypacific.mmbbizrule.oneaservice.model.common.OneAError;
 
@Component
public class OneAErrorHandler {

	private static final LogAgent LOGGER = LogAgent.getLogAgent(OneAErrorHandler.class);
	
	@Autowired
	TB1AErrorHandleDao errorHandleDao;
	
	@Autowired
	BizRuleConfig bizRuleConfig;
	
	/**
	 * This method will get 1A error handle form DB <br/>
	 * and throw exception if any D|L|S error, for D|L error, will add the all errors <br/>
	 * to OneAStopErrorException.oneAErrors, S error will throw BusinessException and the type is S.<br/>
	 * @param oneAErrorCodeList
	 * @param appCode
	 * @param actionCode
	 * @param oneACall
	 * @throws  BusinessException | OneAStopErrorException
	 */
	public void parseOneAErrorCode(List<OneAError> oneAErrorCodeList, String appCode, String actionCode, String oneAWsCallCode)
			throws BusinessBaseException {

		if (CollectionUtils.isEmpty(oneAErrorCodeList)) {
			return;
		}
		oneAErrorCodeList.stream().forEach(ec->LOGGER.info("Found 1A error code:"+ec.getErrorCode()));
		List<ErrorInfo> errorCodeErrorHandleList = buildErrorInfoList(oneAErrorCodeList, appCode, actionCode, oneAWsCallCode);


		if (!CollectionUtils.isEmpty(errorCodeErrorHandleList)) {
			boolean stopFlag = errorCodeErrorHandleList.stream()
					.allMatch(error -> ErrorTypeEnum.BUSERROR.equals(error.getType())
							|| ErrorTypeEnum.REMINDER.equals(error.getType()));

			if (stopFlag) {
				throw new OneAErrorsException("Received 1A D|L error code.", errorCodeErrorHandleList);
			}
		}
		
	}
	
	/**
	 * This method will return the list of error code and error handle. <br/>
	 * S error will throw exception and B error will ignore.
	 * @param appCode
	 * @param olciAction
	 * @param oneAWsCall
	 * @param oneAErrorCodeList
	 * @return
	 * @throws BusinessException 
	 */
	public List<ErrorInfo> getParsedErrorHandleByErrorCode(List<OneAError> oneAErrorCodeList, String appCode,
			String actionCode, String oneAWsCallCode) throws BusinessBaseException {
		if(CollectionUtils.isEmpty(oneAErrorCodeList)){
			return Collections.emptyList();
		}
		
		return buildErrorInfoList(oneAErrorCodeList, appCode, actionCode, oneAWsCallCode);
	}
	/**
	 * Get default error handle if can not find accurate error handle.
	 * @param db1AErrorHandle
	 * @return
	 * @throws BusinessException 
	 */
	private List<ErrorInfo> buildErrorInfoList(List<OneAError> oneAErrorCodeList, String appCode, String actionCode,String oneAWsCallCode)
			throws BusinessBaseException {
		List<ErrorInfo> resultList = new ArrayList<>();
		// build to ErrorInfo bean
		Set<String> baseErrors =  null;
		for (OneAError oneAErrorCode : oneAErrorCodeList) {
			
			String errorCode =  OneAErrorConstants.ONEAERROR_PREFIX_IN_ERRORCODE
					+ OneAErrorConstants.APPCODE_IN_ERRORCODE_MMB +OneAErrorConstants.SOURCECODE_IN_ERRORCODE
					+ oneAWsCallCode + oneAErrorCode.getErrorCode();
			TB1AErrorHandle db1AErrorHandle = errorHandleDao.findMostMatchedErrorHandle(appCode, actionCode,oneAWsCallCode, oneAErrorCode.getErrorCode());
			
			if (db1AErrorHandle == null) {
				if (!checkCanIgnore(oneAErrorCode, appCode, actionCode, oneAWsCallCode)) {
					ErrorInfo errorInfo = new ErrorInfo();
					errorInfo.setErrorCode(errorCode);
					errorInfo.setType(ErrorTypeEnum.BUSERROR);
					resultList.add(errorInfo);
				}
				continue;
			}
			
		
			if(BooleanUtils.isTrue(db1AErrorHandle.getBaseError())){
				baseErrors = baseErrors != null ? baseErrors:new HashSet<>();
				baseErrors.add(errorCode);
			}
			ErrorInfo errorInfo = new ErrorInfo();
			errorInfo.setErrorCode(errorCode);
			switch (db1AErrorHandle.getOneAErrHandle()) {

			case TBConstants.ERROR_HANDLE_IDENTIFIER_LIGHTBOX:
				errorInfo.setType(ErrorTypeEnum.REMINDER);
				break;
			case TBConstants.ERROR_HANDLE_IDENTIFIER_STOP:
				errorInfo.setType(ErrorTypeEnum.SYSERROR);
				throw new ExpectedException("Find 1A Stop(type:S) error code:" + errorCode + " with error message: "
						+ oneAErrorCode.getErrorMessage(), errorInfo);
				// errorInfo.setType(ErrorTypeEnum.SYSRROR)
				// break
			case TBConstants.ERROR_HANDLE_IDENTIFIER_DISPLAY:
				errorInfo.setType(ErrorTypeEnum.BUSERROR);
				break;
			case TBConstants.ERROR_HANDLE_IDENTIFIER_BYPASS:
				LOGGER.info("By Pass 1A error code:"+oneAErrorCode.getErrorCode()+ " with error message: " + oneAErrorCode.getErrorMessage());
				continue;
			default:
				LOGGER.error("UnExpect 1A error type found, please check DB config.");
				errorInfo.setType(ErrorTypeEnum.BUSERROR);
				break;
			}
			
			resultList.add(errorInfo);
		}
		removeBaseError(baseErrors, resultList);
		return resultList;

	}
	
	/**
	 * 
	 * @param baseErrors
	 * @param errorList
	 */
	private void removeBaseError(Set<String> baseErrors,List<ErrorInfo> errorList){
		//remove base error, if there is multiple error code in the list.
		if (!CollectionUtils.isEmpty(baseErrors)) {
			Iterator<ErrorInfo> iterator = errorList.iterator();
			while (iterator.hasNext() && errorList.size() > 1) {
				ErrorInfo errorInfo = iterator.next();
				if (baseErrors.contains(errorInfo.getErrorCode())) {
					iterator.remove();
				}
			}

		}
	}
	/**
	 * check error type can ignore
	 * @param db1AErrorHandle
	 * @param oneAErrorCode
	 * @param appCode
	 * @param actionCode
	 * @param oneAWsCallCode
	 * @throws UnexpectedException
	 */
	private boolean checkCanIgnore(OneAError oneAErrorCode, String appCode,
			String actionCode, String oneAWsCallCode) {

		if (bizRuleConfig.getIgnoreCategoryFromOneAError().contains(oneAErrorCode.getErrorCategory())) {
			LOGGER.warn(String.format(
					"Cannot find the handling/default handling of 1A error code,but the type in ignore list,will ignore it :appCode:%s,oneAWsCallCode:%s,actionCode:%s,oneAErrorCode:%s,oneAErrorMessage:%s,oneAerrorType%s",
					appCode, oneAWsCallCode, actionCode, oneAErrorCode.getErrorCode(), oneAErrorCode.getErrorMessage(), oneAErrorCode.getErrorCategory()));
		} else {
			String message = String.format(
					"Cannot find the handling/default handling of 1A error code, will use D as the error type:appCode:%s,oneAWsCallCode:%s,actionCode:%s,oneAErrorCode:%s,oneAErrorMessage:%s",
					appCode, oneAWsCallCode, actionCode, oneAErrorCode.getErrorCode(), oneAErrorCode.getErrorMessage());
			LOGGER.error(message);
			return false;
		}

		return true;

	}
	
}
