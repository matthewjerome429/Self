package com.cathaypacific.mmbbizrule.cxservice.ibe.model.ticketrefund.request;

import java.util.List;

public class TicketRefundRequestDTO {
	
	private String consent;
	
	private String lang;
	
	private String countryCode;

	private RequesterInfoDTO requesterInfo;
	
	private List<PassengerInfoDTO> passengerInfos;
	
	private String site;
	
	private RandomFieldMapDTO randomFieldMap;
	
	private String predefinedField;
	
	private String channel;

	public String getConsent() {
		return consent;
	}

	public void setConsent(String consent) {
		this.consent = consent;
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public RequesterInfoDTO getRequesterInfo() {
		return requesterInfo;
	}

	public void setRequesterInfo(RequesterInfoDTO requesterInfo) {
		this.requesterInfo = requesterInfo;
	}

	public List<PassengerInfoDTO> getPassengerInfos() {
		return passengerInfos;
	}

	public void setPassengerInfos(List<PassengerInfoDTO> passengerInfos) {
		this.passengerInfos = passengerInfos;
	}

	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}

	public RandomFieldMapDTO getRandomFieldMap() {
		return randomFieldMap;
	}

	public void setRandomFieldMap(RandomFieldMapDTO randomFieldMap) {
		this.randomFieldMap = randomFieldMap;
	}

	public String getPredefinedField() {
		return predefinedField;
	}

	public void setPredefinedField(String predefinedField) {
		this.predefinedField = predefinedField;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

}
