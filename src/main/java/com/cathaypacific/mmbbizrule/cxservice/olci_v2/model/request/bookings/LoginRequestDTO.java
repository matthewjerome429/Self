package com.cathaypacific.mmbbizrule.cxservice.olci_v2.model.request.bookings;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Object model LoginRequestDTO
 */
public class LoginRequestDTO {

	private String txtETicket;
	
	private String txtFamilyName;
	
	private String txtGivenName;
	
	private String txtBookingRef;
	
	private String ocReferenceType;
	
	
	public String getTxtBookingRef() {
		return txtBookingRef;
	}

	public void setTxtBookingRef(String txtBookingRef) {
		this.txtBookingRef = txtBookingRef;
	}

	public String getTxtETicket() {
		return txtETicket;
	}

	public void setTxtETicket(String txtETicket) {
		this.txtETicket = txtETicket;
	}

	public String getTxtFamilyName() {
		return txtFamilyName;
	}

	public void setTxtFamilyName(String txtFamilyName) {
		this.txtFamilyName = txtFamilyName;
	}

	public String getTxtGivenName() {
		return txtGivenName;
	}
	
	public void setTxtGivenName(String txtGivenName) {
		this.txtGivenName = txtGivenName;
	}
	
	

	public String getOcReferenceType() {
		return ocReferenceType;
	}

	public void setOcReferenceType(String ocReferenceType) {
		this.ocReferenceType = ocReferenceType;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	
}
