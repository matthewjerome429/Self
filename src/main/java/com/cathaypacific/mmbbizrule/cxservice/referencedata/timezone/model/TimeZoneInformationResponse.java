package com.cathaypacific.mmbbizrule.cxservice.referencedata.timezone.model;

import java.util.List;

public class TimeZoneInformationResponse {

	private String gmtTime;

	private String localeTimeOffset;
	
	private List<ErrorInfo> errors;

	public String getGmtTime() {
		return gmtTime;
	}

	public void setGmtTime(String gmtTime) {
		this.gmtTime = gmtTime;
	}

	public String getLocaleTimeOffset() {
		return localeTimeOffset;
	}

	public void setLocaleTimeOffset(String localeTimeOffset) {
		this.localeTimeOffset = localeTimeOffset;
	}

	public List<ErrorInfo> getErrors() {
		return errors;
	}

	public void setErrors(List<ErrorInfo> errors) {
		this.errors = errors;
	}
	
}
