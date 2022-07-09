package com.cathaypacific.mmbbizrule.v2.dto.common.booking;

import java.io.Serializable;

public class PassengerDisplayDTOV2 implements Serializable{
	
	private static final long serialVersionUID = -3081139674586782104L;
	private Boolean ec;//Emergency Contact
	private Boolean da;//Destination Address
	private Boolean kn;//KTN
	private Boolean rn;//redress number
	private Boolean pi;//Phone info
	private Boolean ei;//Email info
	
	public Boolean isEc() {
		return ec;
	}
	public void setEc(Boolean ec) {
		this.ec = ec;
	}
	public Boolean isDa() {
		return da;
	}
	public void setDa(Boolean da) {
		this.da = da;
	}
	public Boolean isKn() {
		return kn;
	}
	public void setKn(Boolean kn) {
		this.kn = kn;
	}
	public Boolean isRn() {
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
