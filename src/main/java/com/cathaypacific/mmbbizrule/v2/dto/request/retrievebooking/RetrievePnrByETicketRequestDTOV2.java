package com.cathaypacific.mmbbizrule.v2.dto.request.retrievebooking;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.cathaypacific.mbcommon.constants.ValidationErrorConstants;
import com.cathaypacific.mmbbizrule.dto.common.RequiredInfoDTO;

/**
 * Created by shane.tian.xia on 12/4/2017.
 */
public class RetrievePnrByETicketRequestDTOV2 extends RequiredInfoDTO {

	@NotNull(message = ValidationErrorConstants.REQUEST_VALIDATION_EMPTY_ERROR_CODE)
	private String eticket;

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
	
}
