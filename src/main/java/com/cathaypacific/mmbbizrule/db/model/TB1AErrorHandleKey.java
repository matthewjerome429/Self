package com.cathaypacific.mmbbizrule.db.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Embeddable
public class TB1AErrorHandleKey implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@NotNull
	@Column(name = "APP_CODE", length=5)
	private String appCode;

	@Id
	@NotNull
	@Column(name = "ACTION", length=2)
	private String action;

	@Id
	@NotNull
	@Column(name = "1A_WS_CALL", length=1)
	private String oneAWsCall;

	@Id
	@NotNull
	@Column(name = "1A_ERR_CODE", length=5)
	private String oneAErrCode;

	@Id
	@NotNull
	@Column(name = "1A_ERR_HANDLE", length=1)
	private String oneAErrHandle;

	public String getAppCode() {
		return appCode;
	}

	public void setAppCode(String appCode) {
		this.appCode = appCode;
	}

 
	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getOneAWsCall() {
		return oneAWsCall;
	}

	public void setOneAWsCall(String oneAWsCall) {
		this.oneAWsCall = oneAWsCall;
	}

	public String getOneAErrCode() {
		return oneAErrCode;
	}

	public void setOneAErrCode(String oneAErrCode) {
		this.oneAErrCode = oneAErrCode;
	}

	public String getOneAErrHandle() {
		return oneAErrHandle;
	}

	public void setOneAErrHandle(String oneAErrHandle) {
		this.oneAErrHandle = oneAErrHandle;
	}
	
}
