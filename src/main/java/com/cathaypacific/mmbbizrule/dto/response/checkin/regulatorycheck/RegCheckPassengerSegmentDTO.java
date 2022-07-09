package com.cathaypacific.mmbbizrule.dto.response.checkin.regulatorycheck;

import java.util.List;

import com.cathaypacific.mmbbizrule.v2.dto.common.adc.AdcMessageDTO;

public class RegCheckPassengerSegmentDTO extends RegCheckBaseResponseDTO {

	private static final long serialVersionUID = 8532040101571372421L;
	
	private String passengerId;
	
	private String cprUniqueCustomerId;
	
	private String segmentId;
	
	/** Unique flight id, DID */
	private String cprProductIdentifierDID;
	
	private List<AdcMessageDTO> adcMessages;

	public String getPassengerId() {
		return passengerId;
	}

	public void setPassengerId(String passengerId) {
		this.passengerId = passengerId;
	}

	public String getCprUniqueCustomerId() {
		return cprUniqueCustomerId;
	}

	public void setCprUniqueCustomerId(String cprUniqueCustomerId) {
		this.cprUniqueCustomerId = cprUniqueCustomerId;
	}

	public String getSegmentId() {
		return segmentId;
	}

	public void setSegmentId(String segmentId) {
		this.segmentId = segmentId;
	}

	public String getCprProductIdentifierDID() {
		return cprProductIdentifierDID;
	}

	public void setCprProductIdentifierDID(String cprProductIdentifierDID) {
		this.cprProductIdentifierDID = cprProductIdentifierDID;
	}

	public List<AdcMessageDTO> getAdcMessages() {
		return adcMessages;
	}

	public void setAdcMessages(List<AdcMessageDTO> adcMessages) {
		this.adcMessages = adcMessages;
	}
}
