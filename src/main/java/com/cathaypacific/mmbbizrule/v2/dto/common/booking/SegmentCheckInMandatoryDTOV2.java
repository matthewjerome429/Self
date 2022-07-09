package com.cathaypacific.mmbbizrule.v2.dto.common.booking;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class SegmentCheckInMandatoryDTOV2 implements Serializable{

	private static final long serialVersionUID = 4150574537456621443L;

	private Boolean pt;//Primary Travel Document
	
	private Boolean st;//Secondary Travel Document

	private Boolean ff;//Frequent Flier
	
	private Boolean pn;//pick up number
	
	private Boolean ec;//Emergency Contact

	private Boolean da;//Destination Address

	private Boolean cr;//Country of Residence
	
	private Boolean kn;//KTN
	
	private Boolean rn;//redress number
	@JsonIgnore
	private Boolean pi;//Phone info
	@JsonIgnore
	private Boolean ei;//Email info
	public Boolean getPt() {
		return pt;
	}
	public void setPt(Boolean pt) {
		this.pt = pt;
	}
	public Boolean getSt() {
		return st;
	}
	public void setSt(Boolean st) {
		this.st = st;
	}
	public Boolean getFf() {
		return ff;
	}
	public void setFf(Boolean ff) {
		this.ff = ff;
	}
	public Boolean getPn() {
		return pn;
	}
	public void setPn(Boolean pn) {
		this.pn = pn;
	}
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
	public Boolean getCr() {
		return cr;
	}
	public void setCr(Boolean cr) {
		this.cr = cr;
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
