package com.cathaypacific.mmbbizrule.v2.dto.response.retrievebooking;

import com.cathaypacific.mbcommon.dto.BaseResponseDTO;

public class ConsentInfoRecordResponseDTOV2 extends BaseResponseDTO{

	private static final long serialVersionUID = 1671851018573930627L;

	private boolean consentInfoRecord = false;

	public boolean isConsentInfoRecord() {
		return consentInfoRecord;
	}

	public void setConsentInfoRecord(boolean consentInfoRecord) {
		this.consentInfoRecord = consentInfoRecord;
	}

}
