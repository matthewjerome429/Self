package com.cathaypacific.mbcommon.handler.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.cathaypacific.mbcommon.constants.MDCConstants;
import com.cathaypacific.mbcommon.dto.BaseResponseDTO;
import com.cathaypacific.mbcommon.dto.error.ErrorInfo;
import com.cathaypacific.mbcommon.enums.error.ErrorCodeEnum;
import com.cathaypacific.mbcommon.enums.error.ErrorTypeEnum;
import com.cathaypacific.mbcommon.exception.OneAErrorsException;
import com.cathaypacific.mbcommon.exception.ExpectedException;
import com.cathaypacific.mbcommon.exception.MMBRunTimeException;
import com.cathaypacific.mbcommon.exception.SoapFaultException;
import com.cathaypacific.mbcommon.exception.UnexpectedException;
import com.cathaypacific.mbcommon.loging.LogAgent;
import com.cathaypacific.mbcommon.utils.ErrorUtil;
import com.google.gson.Gson;

/**
 * 
 * @author zilong.bu
 *
 */
@ControllerAdvice
public class GlobalExceptionBaseHandler {
	
	private Gson gson = new Gson();
	
	private static LogAgent logger = LogAgent.getLogAgent(GlobalExceptionBaseHandler.class);

	@ExceptionHandler(value = { BindException.class, MethodArgumentNotValidException.class })
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public BaseResponseDTO validationException(HttpServletRequest request, HttpServletResponse response, Exception ex) {
		logger.warn("Invalid request param. ", ex);
		BaseResponseDTO responseDTO = new BaseResponseDTO();
		if (ex instanceof BindException) {
			BindException exception = (BindException) ex;
			responseDTO.addAllErrors(ErrorUtil.convertToErrorInfo(exception.getBindingResult().getAllErrors()));
		} else if (ex instanceof MethodArgumentNotValidException) {
			MethodArgumentNotValidException exception = (MethodArgumentNotValidException) ex;
			responseDTO.addAllErrors(ErrorUtil.convertToErrorInfo(exception.getBindingResult().getAllErrors()));
		}
		logResponseInExceptionCase(responseDTO);
		return responseDTO;
	}
	
	@ExceptionHandler(ServletRequestBindingException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public BaseResponseDTO servletRequestBindingException(HttpServletRequest request, HttpServletResponse response, Exception ex) {
		logger.warn("Missing request header param. ", ex);
		BaseResponseDTO responseDTO = new BaseResponseDTO();
		ServletRequestBindingException exception = (ServletRequestBindingException)ex;
		ErrorInfo errorInfo = new ErrorInfo();
		errorInfo.setType(ErrorTypeEnum.VALIDATION);
		errorInfo.setErrorCode(exception.getMessage());
		errorInfo.setFieldName("request header");
		responseDTO.addError(errorInfo);
		logResponseInExceptionCase(responseDTO);
		return responseDTO;
	}
	
	@ExceptionHandler(MMBRunTimeException.class)
	@ResponseBody
	public BaseResponseDTO mmbRunTimeException(HttpServletRequest request, HttpServletResponse response, Exception ex) {
		BaseResponseDTO responseDTO = new BaseResponseDTO();
		MMBRunTimeException expectedException = (MMBRunTimeException) ex;
		responseDTO.addError(expectedException.getErrorInfo());
		if (expectedException.getHttpStatus() != null) {
			response.setStatus(expectedException.getHttpStatus().value());
		} 
		logger.error("Run time bussiness exception. "+ex.getMessage(), expectedException.getErrorInfo().getErrorCode(),ex);
		logResponseInExceptionCase(responseDTO);
		return responseDTO;
	}
	
	@ExceptionHandler(ExpectedException.class)
	@ResponseBody
	public BaseResponseDTO businessExpectedException(HttpServletRequest request, HttpServletResponse response, Exception ex) {
		BaseResponseDTO responseDTO = new BaseResponseDTO();
		ExpectedException expectedException = (ExpectedException) ex;
		responseDTO.addError(expectedException.getErrorInfo());
		if (expectedException.getHttpStatus() != null) {
			response.setStatus(expectedException.getHttpStatus().value());
		} 
		logger.error("Business expected exception. "+ex.getMessage(), expectedException.getErrorInfo().getErrorCode(),ex);
		logResponseInExceptionCase(responseDTO);
		return responseDTO;
	}
		
	@ExceptionHandler(SoapFaultException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ResponseBody
	public BaseResponseDTO soapFaultException(HttpServletRequest request, HttpServletResponse response, Exception ex) {
		BaseResponseDTO responseDTO = new BaseResponseDTO();
		SoapFaultException soapFaultException = (SoapFaultException) ex;
		ErrorInfo error = null;
		if (StringUtils.isEmpty(soapFaultException.getErrorCode())) {
			error = new ErrorInfo(ErrorCodeEnum.ERR_SYSTEM);
		} else {
			error = new ErrorInfo();
			error.setErrorCode(ErrorCodeEnum.ERR_PREFIX_SOAP_FAULT.getCode() + soapFaultException.getInterfaceCode()
					+ soapFaultException.getErrorCode());
			error.setType(ErrorCodeEnum.ERR_PREFIX_SOAP_FAULT.getType());
		}
		logger.error("Soap Fault exception. "+ex.getMessage(),error.getErrorCode(),ex);
		responseDTO.addError(error);
		logResponseInExceptionCase(responseDTO);
		return responseDTO;
	}
	
	@ExceptionHandler(UnexpectedException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ResponseBody
	public BaseResponseDTO businessUnExpectedException(HttpServletRequest request, HttpServletResponse response, Exception ex) {
		UnexpectedException unexpectedException = (UnexpectedException) ex;
		BaseResponseDTO responseDTO = new BaseResponseDTO();
		responseDTO.addError(unexpectedException.getErrorInfo());
		logger.error("Business unexpected exception. "+ex.getMessage(),unexpectedException.getErrorInfo().getErrorCode(), ex);
		logResponseInExceptionCase(responseDTO);
		return responseDTO;
	}
	
	@ExceptionHandler(OneAErrorsException.class)
	@ResponseBody
	public BaseResponseDTO businessOneAErrorsException(HttpServletRequest request, HttpServletResponse response, Exception ex) {
		OneAErrorsException oneAErrorsException = (OneAErrorsException) ex;
		if(oneAErrorsException.getOneAErrors() != null){
			oneAErrorsException.getOneAErrors().stream().forEach(errorInfo->logger.error("Business oneA Errors.", errorInfo.getErrorCode(),ex));
		}else{
			logger.error("Business oneA Errors.", ex);
		}
	
		BaseResponseDTO responseDTO = new BaseResponseDTO();
		responseDTO.setErrors(oneAErrorsException.getOneAErrors());
		logResponseInExceptionCase(responseDTO);
		return responseDTO;
	}

	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ResponseBody
	public BaseResponseDTO otherException(HttpServletRequest request, HttpServletResponse response, Exception ex) {
		BaseResponseDTO responseDTO = new BaseResponseDTO();
		ErrorInfo error = new ErrorInfo(ErrorCodeEnum.ERR_SYSTEM);
		responseDTO.addError(error);
		logger.error("System exception. "+ex.getMessage(),error.getErrorCode(), ex);
		
		logResponseInExceptionCase(responseDTO);
		return responseDTO;
	}
	
	private void logResponseInExceptionCase(BaseResponseDTO responseDTO ){
		String jsonBody = gson.toJson(responseDTO);
		MDC.put(MDCConstants.HTTPBODY_REQUEST_RESPONSEMDC_KEY, jsonBody);
	}
}
