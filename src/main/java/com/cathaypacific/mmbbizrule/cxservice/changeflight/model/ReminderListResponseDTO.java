package com.cathaypacific.mmbbizrule.cxservice.changeflight.model;

import java.util.List;

import com.cathaypacific.mmbbizrule.cxservice.changeflight.model.common.BaseResponseDTO;
import com.google.gson.annotations.SerializedName;

public class ReminderListResponseDTO extends BaseResponseDTO {

	private static final long serialVersionUID = -7709539470991372988L;
	@SerializedName("sklist")
	private List<SSRSKDTO> skList;
	@SerializedName("ssrlist")
	private List<SSRSKDTO> ssrList;
	private boolean hasHotel;
	private boolean hasEvent;
	
	private boolean hasASREXL;
	private boolean hasExtraBackage;
	private boolean hasInsurance;

	public boolean isHasHotel() {
		return hasHotel;
	}
	public void setHasHotel(boolean hasHotel) {
		this.hasHotel = hasHotel;
	}
	public boolean isHasEvent() {
		return hasEvent;
	}
	public void setHasEvent(boolean hasEvent) {
		this.hasEvent = hasEvent;
	}
	public List<SSRSKDTO> getSsrlist() {
		return ssrList;
	}
	public void setSsrList(List<SSRSKDTO> ssrList) {
		this.ssrList = ssrList;
	}
	public List<SSRSKDTO> getSklist() {
		return skList;
	}
	public void setSkList(List<SSRSKDTO> skList) {
		this.skList = skList;
	}

	public boolean isHasInsurance() {
		return hasInsurance;
	}
	public void setHasInsurance(boolean hasInsurance) {
		this.hasInsurance = hasInsurance;
	}
	public boolean isHasExtraBackage() {
		return hasExtraBackage;
	}
	public void setHasExtraBackage(boolean hasExtraBackage) {
		this.hasExtraBackage = hasExtraBackage;
	}
	public boolean isHasASREXL() {
		return hasASREXL;
	}
	public void setHasASREXL(boolean hasASREXL) {
		this.hasASREXL = hasASREXL;
	}
	
	@Override
	public String toString() {
		return "ReminderListResponseDTO [skList=" + skList + ", ssrList=" + ssrList + ", hasHotel=" + hasHotel
				+ ", hasEvent=" + hasEvent + ",hasASREXL=" + hasASREXL + ", hasExtraBackage=" + hasExtraBackage + ",hasInsurance=" + hasInsurance + "]";
	}


}
