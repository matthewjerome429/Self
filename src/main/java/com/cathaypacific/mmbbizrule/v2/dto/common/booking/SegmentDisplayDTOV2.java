package com.cathaypacific.mmbbizrule.v2.dto.common.booking;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class SegmentDisplayDTOV2 {
	
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
	public Boolean isCr() {
		return cr;
	}
	public void setCr(Boolean cr) {
		this.cr = cr;
	}
	public Boolean isPt() {
		return pt;
	}
	public void setPt(Boolean pt) {
		this.pt = pt;
	}
	public Boolean isSt() {
		return st;
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
	public Boolean isPi() {
		return pi;
	}
	public void setPi(Boolean pi) {
		this.pi = pi;
	}
	public Boolean isEi() {
		return ei;
	}
	public void setEi(Boolean ei) {
		this.ei = ei;
	}
	public void setSt(Boolean st) {
		this.st = st;
	}
	public Boolean isFf() {
		return ff;
	}
	public void setFf(Boolean ff) {
		this.ff = ff;
	}
	public Boolean isPn() {
		return pn;
	}
	public void setPn(Boolean pn) {
		this.pn = pn;
	}
	
}
