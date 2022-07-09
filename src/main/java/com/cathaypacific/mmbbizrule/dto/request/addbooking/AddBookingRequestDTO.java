package com.cathaypacific.mmbbizrule.dto.request.addbooking;

import io.swagger.annotations.ApiModelProperty;

public class AddBookingRequestDTO {
	
	private String eticket;

	private String rloc;
	
	@ApiModelProperty(value="skip RU member adding the booking with MPO/AM FQTV check or not")
	private Boolean ignoreMPOCheck;
	
	private String familyName;
	
	private String givenName;

	public String getEticket() {
		return eticket;
	}

	public void setEticket(String eticket) {
		this.eticket = eticket;
	}

	public String getRloc() {
		return rloc;
	}

	public void setRloc(String rloc) {
		this.rloc = rloc;
	}

	public Boolean getIgnoreMPOCheck() {
		return ignoreMPOCheck;
	}

	public void setIgnoreMPOCheck(Boolean ignoreMPOCheck) {
		this.ignoreMPOCheck = ignoreMPOCheck;
	}

	public String getFamilyName() {
		return familyName;
	}

	public void setFamilyName(String familyName) {
		this.familyName = familyName;
	}

	public String getGivenName() {
		return givenName;
	}

	public void setGivenName(String givenName) {
		this.givenName = givenName;
	}
	
}
