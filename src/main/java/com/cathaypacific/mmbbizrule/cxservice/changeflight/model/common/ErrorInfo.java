package com.cathaypacific.mmbbizrule.cxservice.changeflight.model.common;

import java.io.Serializable;

import com.cathaypacific.mbcommon.enums.error.ErrorCodeEnum;
import com.cathaypacific.mbcommon.enums.error.ErrorTypeEnum;
import com.google.gson.annotations.SerializedName;

import io.swagger.annotations.ApiModelProperty;

public class ErrorInfo implements Serializable{
	
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 7950343844574246670L;
	@SerializedName("code")
	private String errorCode;
	
	@ApiModelProperty(dataType = "string")
	private ErrorTypeEnum type;
	
	private String fieldName;
	
	private String description;
	
	public ErrorInfo(ErrorCodeEnum errorCodeEnum) {
		this.errorCode = errorCodeEnum.getCode();
		this.type = errorCodeEnum.getType();
	}
	
	public ErrorInfo(){
	//do nothing	
	}
	public ErrorInfo(ErrorCodeEnum errorCodeEnum,String fieldName) {
		this.errorCode = errorCodeEnum.getCode();	
		this.fieldName = fieldName;

	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public ErrorTypeEnum getType() {
		return type;
	}

	public void setType(ErrorTypeEnum type) {
		this.type = type;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
}
