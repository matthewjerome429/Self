package com.cathaypacific.mmbbizrule.v2.dto.common.booking;

import java.io.Serializable;

public class PassengerCheckInMandatoryDTOV2 implements Serializable{

	private static final long serialVersionUID = 7350839316257381226L;
	private Boolean ec;//Emergency Contact
	private Boolean da;//Destination Address
	private Boolean kn;//KTN
	private Boolean rn;//redress number
	private Boolean pi;//Phone info
	private Boolean ei;//Email info
	public Boolean getEc() {
		return ec;
	}
	public void setEc(Boolean ec) {
		this.ec = ec;
	}
	public Boolean getDa() {
		return da;
	}
	public void setDa(Boolean da) {
		this.da = da;
	}
	public Boolean getKn() {
		return kn;
	}
	public void setKn(Boolean kn) {
		this.kn = kn;
	}
	public Boolean getRn() {
		return rn;
	}
	public void setRn(Boolean rn) {
		this.rn = rn;
	}
	public Boolean getPi() {
		return pi;
	}
	public void setPi(Boolean pi) {
		this.pi = pi;
	}
	public Boolean getEi() {
		return ei;
	}
	public void setEi(Boolean ei) {
		this.ei = ei;
	}
	
}
