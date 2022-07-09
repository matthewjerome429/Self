package com.cathaypacific.mmbbizrule.dto.request.memberselfbookings;

import org.hibernate.validator.constraints.NotEmpty;

import com.cathaypacific.mmbbizrule.dto.common.CustomizedRequiredInfoDTO;

public class SelfBookingsRequestDTO {
	
	@NotEmpty
	private String memberId;
	
	private CustomizedRequiredInfoDTO requiredInfo;

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public CustomizedRequiredInfoDTO getRequiredInfo() {
		return requiredInfo;
	}

	public void setRequiredInfo(CustomizedRequiredInfoDTO requiredInfo) {
		this.requiredInfo = requiredInfo;
	}
	
	
}
