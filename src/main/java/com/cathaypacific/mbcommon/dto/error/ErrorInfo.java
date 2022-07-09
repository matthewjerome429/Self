package com.cathaypacific.mbcommon.dto.error;

import java.io.Serializable;

import com.cathaypacific.mbcommon.enums.error.ErrorCodeEnum;
import com.cathaypacific.mbcommon.enums.error.ErrorLevelEnum;
import com.cathaypacific.mbcommon.enums.error.ErrorTypeEnum;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
@ApiModel
public class ErrorInfo implements Serializable{
	
	private static final long serialVersionUID = 9221865752430878617L;

	private String errorCode;
	//TODO for support old mmb, will remove in R2
	private String code;
	@ApiModelProperty(dataType = "string")
	private ErrorTypeEnum type;
	
	@ApiModelProperty(dataType = "string")
	private ErrorLevelEnum level;
	
	private String passengerId;
	
	private String segmentId;
	
	private String fieldName;
	
	public ErrorInfo(ErrorCodeEnum errorCodeEnum) {
		this.errorCode = errorCodeEnum.getCode();
		this.type = errorCodeEnum.getType();
		this.code = errorCodeEnum.getOldMmbCode();
	}
	
	public ErrorInfo(){
	//do nothing	
	}
	public ErrorInfo(ErrorCodeEnum errorCodeEnum,ErrorLevelEnum level,String passengerId,String segmentId,String fieldName) {
		this.errorCode = errorCodeEnum.getCode();
		this.level = level;
		this.passengerId = passengerId;
		this.segmentId = segmentId;
		this.fieldName = fieldName;
		this.code=errorCodeEnum.getOldMmbCode();
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public ErrorTypeEnum getType() {
		return type;
	}

	public void setType(ErrorTypeEnum type) {
		this.type = type;
	}

	public ErrorLevelEnum getLevel() {
		return level;
	}

	public void setLevel(ErrorLevelEnum level) {
		this.level = level;
	}

	public String getPassengerId() {
		return passengerId;
	}

	public void setPassengerId(String passengerId) {
		this.passengerId = passengerId;
	}

	public String getSegmentId() {
		return segmentId;
	}

	public void setSegmentId(String segmentId) {
		this.segmentId = segmentId;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
}
