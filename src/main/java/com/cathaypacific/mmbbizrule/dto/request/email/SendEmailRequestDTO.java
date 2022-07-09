package com.cathaypacific.mmbbizrule.dto.request.email;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import com.cathaypacific.mbcommon.constants.ValidationErrorConstants;

public class SendEmailRequestDTO {
	
	private Boolean useSameEmail;
	
	@NotBlank(message = ValidationErrorConstants.REQUEST_VALIDATION_EMPTY_ERROR_CODE)
	private String rloc;
	
	@Valid
	@NotNull(message = ValidationErrorConstants.REQUEST_VALIDATION_EMPTY_ERROR_CODE)
	private List<PaxEmailInfoDTO> passengerEmails;

	public Boolean getUseSameEmail() {
		return useSameEmail;
	}

	public void setUseSameEmail(Boolean useSameEmail) {
		this.useSameEmail = useSameEmail;
	}

	public String getRloc() {
		return rloc;
	}

	public void setRloc(String rloc) {
		this.rloc = rloc;
	}

	public List<PaxEmailInfoDTO> getPassengerEmails() {
		return passengerEmails;
	}

	public void setPassengerEmails(List<PaxEmailInfoDTO> passengerEmails) {
		this.passengerEmails = passengerEmails;
	}
	
}
