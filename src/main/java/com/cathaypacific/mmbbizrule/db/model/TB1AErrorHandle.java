package com.cathaypacific.mmbbizrule.db.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.NamedNativeQueries;
import org.hibernate.annotations.NamedNativeQuery;

@Entity
@NamedNativeQueries({
	@NamedNativeQuery(name = "findMostMatchedErrorHandle",
				query = "select t1.* from tb_1a_error_handle t1 where "
				+ " (t1.app_code = ?1 or t1.app_code = '*') " 
				+ " and (t1.action= ?2 or t1.action='*')"
				+ " and (t1.1a_ws_call= ?3 or t1.1a_ws_call='*')"
				+ " and (t1.1a_err_code=?4 or t1.1a_err_code='*')"
				+ " order by t1.app_code desc,t1.1a_err_code desc,t1.action desc limit 0,1", resultClass=TB1AErrorHandle.class)})
@Table(name = "TB_1A_ERROR_HANDLE")
@IdClass(TB1AErrorHandleKey.class)
public class TB1AErrorHandle {
	@Id
	@NotNull
	@Column(name = "APP_CODE", length = 5)
	private String appCode;

	@Id
	@NotNull
	@Column(name = "ACTION", length = 2)
	private String action;

	@Id
	@NotNull
	@Column(name = "1A_WS_CALL", length = 1)
	private String oneAWsCall;

	@Id
	@NotNull
	@Column(name = "1A_ERR_CODE", length = 5)
	private String oneAErrCode;

	@Id
	@NotNull
	@Column(name = "1A_ERR_HANDLE", length = 1)
	private String oneAErrHandle;

	@Column(name = "BASE_ERROR", length = 1)
	private Boolean baseError;
	
	@Column(name = "ACTION_DESC", length = 320)
	private String actionDesc;

	@Column(name = "1A_ERR_DESC", length = 320)
	private String oneAErrDesv;

	@NotNull
	@Column(name = "LAST_UPDATE_SOURCE", length = 8)
	private String lastUpdateSource;

	@NotNull
	@Column(name = "LAST_UPDATE_TIMESTAMP")
	private Timestamp lastUpdateTimeStamp;

	public String getAppCode() {
		return appCode;
	}

	public void setAppCode(String appCode) {
		this.appCode = appCode;
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

	public String getOneAErrDesv() {
		return oneAErrDesv;
	}

	public void setOneAErrDesv(String oneAErrDesv) {
		this.oneAErrDesv = oneAErrDesv;
	}

	public String getLastUpdateSource() {
		return lastUpdateSource;
	}

	public void setLastUpdateSource(String lastUpdateSource) {
		this.lastUpdateSource = lastUpdateSource;
	}

	public Timestamp getLastUpdateTimeStamp() {
		return lastUpdateTimeStamp;
	}

	public void setLastUpdateTimeStamp(Timestamp lastUpdateTimeStamp) {
		this.lastUpdateTimeStamp = lastUpdateTimeStamp;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getActionDesc() {
		return actionDesc;
	}

	public void setActionDesc(String actionDesc) {
		this.actionDesc = actionDesc;
	}

	public Boolean getBaseError() {
		return baseError;
	}

	public void setBaseError(Boolean baseError) {
		this.baseError = baseError;
	}


}
