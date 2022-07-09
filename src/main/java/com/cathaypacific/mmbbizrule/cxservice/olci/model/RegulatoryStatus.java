package com.cathaypacific.mmbbizrule.cxservice.olci.model;

import java.io.Serializable;

public class RegulatoryStatus implements Serializable {

	private static final long serialVersionUID = -7487866173007110690L;

	private String regSysType;

	private String actionCode;

	private String allowAcceptance;

	private String allowBP;

	private String errorCode;

	public String getRegSysType() {
		return regSysType;
	}

	public void setRegSysType(String regSysType) {
		this.regSysType = regSysType;
	}

	public String getActionCode() {
		return actionCode;
	}

	public void setActionCode(String actionCode) {
		this.actionCode = actionCode;
	}

	public String getAllowAcceptance() {
		return allowAcceptance;
	}

	public void setAllowAcceptance(String allowAcceptance) {
		this.allowAcceptance = allowAcceptance;
	}

	public String getAllowBP() {
		return allowBP;
	}

	public void setAllowBP(String allowBP) {
		this.allowBP = allowBP;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;

		if (this.getClass() != obj.getClass())
			return false;
		
		RegulatoryStatus rs = (RegulatoryStatus) obj;
		return regSysType.equals(rs.getRegSysType());
	}
	
	@Override
	public int hashCode() {
		return regSysType.hashCode();
	}

}
