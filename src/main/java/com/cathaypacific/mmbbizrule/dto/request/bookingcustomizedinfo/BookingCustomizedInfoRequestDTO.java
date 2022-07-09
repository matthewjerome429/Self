package com.cathaypacific.mmbbizrule.dto.request.bookingcustomizedinfo;

import com.cathaypacific.mmbbizrule.dto.common.CustomizedRequiredInfoDTO;

public class BookingCustomizedInfoRequestDTO {
	/**
	 * request parameter logic:
	 * 		"familyName" & "givenName" are for non-member login
	 * 		"memberId" & "originalRuMemberId" are for member login
	 * 		if "familyName" & "givenName" is not empty, this request will be treated as an non-member login session 
	 * 			and "memberId" & "originalRuMemberId" should be null
	 * 		otherwise, if "memberId" is not empty, this request will be treated as member login session, 
	 * 			the "originalRuMemberId" is for name identification(if exists), and "familyName" & "givenName" must be null
 	 */
//	@Pattern(regexp = "^[0-9]{10}$",message = ValidationErrorConstants.REQUEST_VALIDATION_INVALID_ERROR_CODE)
	private String memberId;
	
	private String originalRuMemberId;
	
	private String rloc;
	
	private String eticket;
	
	private String familyName;
	
	private String givenName;
	
	private CustomizedRequiredInfoDTO requiredInfo;

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getOriginalRuMemberId() {
		return originalRuMemberId;
	}

	public void setOriginalRuMemberId(String originalRuMemberId) {
		this.originalRuMemberId = originalRuMemberId;
	}

	public String getRloc() {
		return rloc;
	}

	public void setRloc(String rloc) {
		this.rloc = rloc;
	}

	public String getEticket() {
		return eticket;
	}

	public void setEticket(String eticket) {
		this.eticket = eticket;
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

	public CustomizedRequiredInfoDTO getRequiredInfo() {
		return requiredInfo;
	}

	public void setRequiredInfo(CustomizedRequiredInfoDTO requiredInfo) {
		this.requiredInfo = requiredInfo;
	}

}
