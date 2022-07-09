package com.cathaypacific.mmbbizrule.dto.common.booking;

import java.io.Serializable;

public class EventDescriptionDTO implements Serializable{
	
	private static final long serialVersionUID = 1948465163754173107L;
	
	private String shortName;
	
	private String fullName;
	
	private String inclusion;
	
	private String operatingDetails;

	private String additionalInstructions;
	
	private String small;
	
	private String essential;

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getInclusion() {
		return inclusion;
	}

	public void setInclusion(String inclusion) {
		this.inclusion = inclusion;
	}

	public String getOperatingDetails() {
		return operatingDetails;
	}

	public void setOperatingDetails(String operatingDetails) {
		this.operatingDetails = operatingDetails;
	}

	public String getAdditionalInstructions() {
		return additionalInstructions;
	}

	public void setAdditionalInstructions(String additionalInstructions) {
		this.additionalInstructions = additionalInstructions;
	}

	public String getSmall() {
		return small;
	}

	public void setSmall(String small) {
		this.small = small;
	}

	public String getEssential() {
		return essential;
	}

	public void setEssential(String essential) {
		this.essential = essential;
	}
	
}
