package com.cathaypacific.mmbbizrule.dto.request.boardingpass.spbp;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.cathaypacific.mbcommon.constants.ValidationErrorConstants;
import com.cathaypacific.olciconsumer.model.request.BaseRequestDTO;

public class SendSMSRequestDTOV2 extends BaseRequestDTO {
	
	@NotNull(message = ValidationErrorConstants.REQUEST_VALIDATION_EMPTY_ERROR_CODE)
	private String rloc;
	
	@NotNull(message = ValidationErrorConstants.REQUEST_VALIDATION_EMPTY_ERROR_CODE)
	private String journeyId;
	
	@Valid
	private List<PhoneNumbersDTOV2> phoneNumbers;

	private Boolean useSamePhoneNum;
	
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

	public List<PhoneNumbersDTOV2> getPhoneNumbers() {
		return phoneNumbers;
	}

	public void setPhoneNumbers(List<PhoneNumbersDTOV2> phoneNumbers) {
		this.phoneNumbers = phoneNumbers;
	}

	public List<PhoneNumbersDTOV2> findPhoneNumbers() {
		if(phoneNumbers == null){
			phoneNumbers = new ArrayList<>();
		}
		return phoneNumbers;
	}

	public Boolean getUseSamePhoneNum() {
		return useSamePhoneNum;
	}

	public void setUseSamePhoneNum(Boolean useSamePhoneNum) {
		this.useSamePhoneNum = useSamePhoneNum;
	}

}
