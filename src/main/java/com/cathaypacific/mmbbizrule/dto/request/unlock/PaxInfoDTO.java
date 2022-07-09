package com.cathaypacific.mmbbizrule.dto.request.unlock;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;

import com.cathaypacific.mbcommon.constants.ValidationErrorConstants;

public class PaxInfoDTO {
	
	@NotBlank(message = ValidationErrorConstants.REQUEST_VALIDATION_EMPTY_ERROR_CODE)
	private String passengerId;
	
	@Pattern(regexp = "^([0-9]{13})?$",message = ValidationErrorConstants.REQUEST_VALIDATION_INVALID_ERROR_CODE)
	private String eticket;
	
	private boolean unlock;
	
	public PaxInfoDTO() {
		super();
	}

	public PaxInfoDTO(String passengerId, String eticket) {
		super();
		this.passengerId = passengerId;
		this.eticket = eticket;
	}

	public String getPassengerId() {
		return passengerId;
	}

	public void setPassengerId(String passengerId) {
		this.passengerId = passengerId;
	}

	public String getEticket() {
		return eticket;
	}

	public void setEticket(String eticket) {
		this.eticket = eticket;
	}

	public boolean isUnlock() {
		return unlock;
	}

	public void setUnlock(boolean unlock) {
		this.unlock = unlock;
	}
	
}
