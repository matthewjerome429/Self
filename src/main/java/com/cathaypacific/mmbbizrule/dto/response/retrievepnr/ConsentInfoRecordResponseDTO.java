package com.cathaypacific.mmbbizrule.dto.response.retrievepnr;

import com.cathaypacific.mbcommon.dto.BaseResponseDTO;

public class ConsentInfoRecordResponseDTO extends BaseResponseDTO{

	private static final long serialVersionUID = 1671851018573930627L;

	private boolean consentInfoRecord = false;

	public boolean isConsentInfoRecord() {
		return consentInfoRecord;
	}

	public void setConsentInfoRecord(boolean consentInfoRecord) {
		this.consentInfoRecord = consentInfoRecord;
	}

}
