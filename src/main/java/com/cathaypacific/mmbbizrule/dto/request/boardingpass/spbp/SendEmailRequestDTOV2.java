package com.cathaypacific.mmbbizrule.dto.request.boardingpass.spbp;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.cathaypacific.mbcommon.constants.ValidationErrorConstants;
import com.cathaypacific.olciconsumer.model.request.BaseRequestDTO;

public class SendEmailRequestDTOV2 extends BaseRequestDTO {
	
	@NotNull(message = ValidationErrorConstants.REQUEST_VALIDATION_EMPTY_ERROR_CODE)
	private String rloc;
	
	@NotNull(message = ValidationErrorConstants.REQUEST_VALIDATION_EMPTY_ERROR_CODE)
	private String journeyId;
	
	@Valid
	private List<EmailAddressDTOV2> emailAddresses;

	private Boolean useSameEmail;
	
	public String getRloc() {
		return rloc;
	}

	public void setRloc(String rloc) {
		this.rloc = rloc;
	}

	public String getJourneyId() {
		return journeyId;
	}

	public void setJourneyId(String journeyId) {
		this.journeyId = journeyId;
	}

	public List<EmailAddressDTOV2> getEmailAddresses() {
		return emailAddresses;
	}

	public void setEmailAddresses(List<EmailAddressDTOV2> emailAddresses) {
		this.emailAddresses = emailAddresses;
	}

	public Boolean getUseSameEmail() {
		return useSameEmail;
	}

	public void setUseSameEmail(Boolean useSameEmail) {
		this.useSameEmail = useSameEmail;
	}

	
}
