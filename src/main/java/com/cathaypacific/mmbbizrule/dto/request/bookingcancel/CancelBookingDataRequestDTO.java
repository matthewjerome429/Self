package com.cathaypacific.mmbbizrule.dto.request.bookingcancel;

public class CancelBookingDataRequestDTO {

	private String oneARloc;

	private String entryPoint;

	private String entryLanguage;

	private String entryCountry;

	private String returnUrl;

	private String errorUrl;

	public String getOneARloc() {
		return oneARloc;
	}

	public void setOneARloc(String oneARloc) {
		this.oneARloc = oneARloc;
	}

	public String getEntryPoint() {
		return entryPoint;
	}

	public void setEntryPoint(String entryPoint) {
		this.entryPoint = entryPoint;
	}

	public String getEntryLanguage() {
		return entryLanguage;
	}

	public void setEntryLanguage(String entryLanguage) {
		this.entryLanguage = entryLanguage;
	}

	public String getEntryCountry() {
		return entryCountry;
	}

	public void setEntryCountry(String entryCountry) {
		this.entryCountry = entryCountry;
	}

	public String getReturnUrl() {
		return returnUrl;
	}

	public void setReturnUrl(String returnUrl) {
		this.returnUrl = returnUrl;
	}

	public String getErrorUrl() {
		return errorUrl;
	}

	public void setErrorUrl(String errorUrl) {
		this.errorUrl = errorUrl;
	}

}
