package com.cathaypacific.mmbbizrule.cxservice.changeflight.model.common;


import java.io.Serializable;
import com.google.gson.annotations.SerializedName;


public class BaseResponseDTO implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 7950343844574246670L;

	@SerializedName("error")
	private ErrorInfo changeFlightErrorInfo;

	public ErrorInfo getChangeFlightErrorInfo() {
		return changeFlightErrorInfo;
	}
	
	public void setChangeFlightErrorInfo(ErrorInfo changeFlightErrorInfo) {
		this.changeFlightErrorInfo = changeFlightErrorInfo;
	}

	
}
