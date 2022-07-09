package com.cathaypacific.mmbbizrule.v2.dto.request.retrievebooking;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;


import com.cathaypacific.mbcommon.constants.ValidationErrorConstants;
import com.cathaypacific.mmbbizrule.dto.common.RequiredInfoDTO;

public class RetrievePnrByRlocRequestDTOV2 extends RequiredInfoDTO{

	@NotNull(message = ValidationErrorConstants.REQUEST_VALIDATION_EMPTY_ERROR_CODE)
	@Pattern(regexp = "^[A-Za-z0-9]{6,7}$",message = ValidationErrorConstants.REQUEST_VALIDATION_INVALID_ERROR_CODE)
	private String rloc;
	
	@NotNull(message = ValidationErrorConstants.REQUEST_VALIDATION_EMPTY_ERROR_CODE)
	@Pattern(regexp = "^[A-Za-z ]{1,50}$", message = ValidationErrorConstants.REQUEST_VALIDATION_INVALID_ERROR_CODE)
	private String familyName;
	
	@NotNull(message = ValidationErrorConstants.REQUEST_VALIDATION_EMPTY_ERROR_CODE)
	@Pattern(regexp = "^[A-Za-z ]{1,32}$", message = ValidationErrorConstants.REQUEST_VALIDATION_INVALID_ERROR_CODE)
	private String givenName;
	
	/**The identifier of retrieve DCS(CPR).*/
	private boolean retrieveDcs = true;
	
	/**The identifier of retrieve DCS(pnr).*/
	private boolean retrieveRes = true;
	
	/**The identifier of retrieve OJ(hotel/event in package booking).*/
	private boolean retrieveOJ = true;
	
	public String getRloc() {
		return rloc;
	}
	public void setRloc(String rloc) {
		this.rloc = rloc;
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
	
	public boolean isRetrieveDcs() {
		return retrieveDcs;
	}
	public void setRetrieveDcs(boolean retrieveDcs) {
		this.retrieveDcs = retrieveDcs;
	}
	public boolean isRetrieveRes() {
		return retrieveRes;
	}
	public void setRetrieveRes(boolean retrieveRes) {
		this.retrieveRes = retrieveRes;
	}
	public boolean isRetrieveOJ() {
		return retrieveOJ;
	}
	public void setRetrieveOJ(boolean retrieveOJ) {
		this.retrieveOJ = retrieveOJ;
	}
	@Override
	public String toString() {
		return "RetrievePnrByRlocRequestDTOV2 [rloc=" + rloc + ", familyName=" + familyName + ", givenName=" + givenName
				+ ", retrieveDcs=" + retrieveDcs + ", retrieveRes=" + retrieveRes + ", retrieveOJ=" + retrieveOJ + "]";
	}
 
}
