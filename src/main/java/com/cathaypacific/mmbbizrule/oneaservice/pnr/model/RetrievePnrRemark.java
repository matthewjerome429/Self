package com.cathaypacific.mmbbizrule.oneaservice.pnr.model;

import java.util.List;

public class RetrievePnrRemark {
	
	private String type;
	
	private String category;
	
	private String freeText;
	
	private List<String> passengerIds;
	
	private List<String> segmentIds;
	
	private String otQualifierNumber;
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getFreeText() {
		return freeText;
	}
	public void setFreeText(String freeText) {
		this.freeText = freeText;
	}
	public List<String> getPassengerIds() {
		return passengerIds;
	}
	public void setPassengerIds(List<String> passengerIds) {
		this.passengerIds = passengerIds;
	}
	public List<String> getSegmentIds() {
		return segmentIds;
	}
	public void setSegmentIds(List<String> segmentIds) {
		this.segmentIds = segmentIds;
	}
	public String getOtQualifierNumber() {
		return otQualifierNumber;
	}
	public void setOtQualifierNumber(String otQualifier) {
		this.otQualifierNumber = otQualifier;
	}
}
