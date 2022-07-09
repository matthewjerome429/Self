package com.cathaypacific.mmbbizrule.dto.response.consent;

import java.util.List;

import com.cathaypacific.mbcommon.dto.BaseResponseDTO;

public class ConsentCommonRecordResponseDTO extends BaseResponseDTO{

	private static final long serialVersionUID = 1671851018573930627L;

	private List<ConsentInfoRecordDTO> consentInfoRecords;

	public List<ConsentInfoRecordDTO> getConsentInfoRecords() {
		return consentInfoRecords;
	}

	public void setConsentInfoRecords(List<ConsentInfoRecordDTO> consentInfoRecords) {
		this.consentInfoRecords = consentInfoRecords;
	}

	

}
