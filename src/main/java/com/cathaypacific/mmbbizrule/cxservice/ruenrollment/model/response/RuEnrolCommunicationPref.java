package com.cathaypacific.mmbbizrule.cxservice.ruenrollment.model.response;

public class RuEnrolCommunicationPref {
	
	private String channelCode;	
	
	private String natureCode;
	
	private String selectionOptionCode;
	
	private String errorCode;
	
	private String errorMessage;

	public String getChannelCode() {
		return channelCode;
	}

	public void setChannelCode(String channelCode) {
		this.channelCode = channelCode;
	}

	public String getNatureCode() {
		return natureCode;
	}

	public void setNatureCode(String natureCode) {
		this.natureCode = natureCode;
	}

	public String getSelectionOptionCode() {
		return selectionOptionCode;
	}

	public void setSelectionOptionCode(String selectionOptionCode) {
		this.selectionOptionCode = selectionOptionCode;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
		
}
