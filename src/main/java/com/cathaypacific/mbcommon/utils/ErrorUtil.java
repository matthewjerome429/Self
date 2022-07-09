package com.cathaypacific.mbcommon.utils;

import java.util.ArrayList;
import java.util.List;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.validation.ObjectError;
import org.springframework.web.client.HttpStatusCodeException;

import com.cathaypacific.mbcommon.constants.OneAErrorConstants;
import com.cathaypacific.mbcommon.dto.error.ErrorInfo;
import com.cathaypacific.mbcommon.enums.error.ErrorTypeEnum;
import com.cathaypacific.mbcommon.loging.LogAgent;
import com.google.gson.Gson;

public class ErrorUtil {
	
	private static final LogAgent LOGGER = LogAgent.getLogAgent(ErrorUtil.class);
	
	/**
	 * Convert field validation errors to ErrorInfo which can be used for display.
	 * 
	 * @param errorList
	 * @param returnDto
	 */
	public static List<ErrorInfo> convertToErrorInfo(List<ObjectError> errorList) {
		ArrayList<ErrorInfo> errors = new ArrayList<>();
		for (ObjectError errorInfo : errorList) {
			String defaultMessage = ((MessageSourceResolvable) errorInfo.getArguments()[0]).getDefaultMessage();
			String fieldName;
			String[] newFieldName;
			String fieldName1;
			ErrorInfo errorInfoDto = new ErrorInfo();
			if (defaultMessage.indexOf('.') != -1) {
				fieldName = defaultMessage.substring(defaultMessage.indexOf('.')+1);
				newFieldName=defaultMessage.split("\\.");
				fieldName1=newFieldName[newFieldName.length-1];
				errorInfoDto.setErrorCode(errorInfo.getDefaultMessage() + fieldName1.toUpperCase());
			} else {
				fieldName = defaultMessage;
				errorInfoDto.setErrorCode(errorInfo.getDefaultMessage() + fieldName.toUpperCase());
			}
			errorInfoDto.setFieldName(fieldName);
			errorInfoDto.setType(ErrorTypeEnum.VALIDATION);
			errors.add(errorInfoDto);
		}
		return errors;
	}
	
	/**
	 * Get error from http exception and format to @param clz  
	 * @param ex
	 * @param clz
	 * @return
	 */
	public static <T> T getFormatedErrorFromHttpException(Exception ex, Class<T> clz){
		T result= null;
		if(ex instanceof HttpStatusCodeException){
			try {
				String message =  ((HttpStatusCodeException)ex).getResponseBodyAsString();
				Gson gson= new Gson();
				result = gson.fromJson(message, clz);
			} catch (Exception e) {
				//just log message because it just a warning
				LOGGER.warn("Cannot covert the error to request model.",e.getMessage());
			}
			
		}
		return result;
	} 
}
