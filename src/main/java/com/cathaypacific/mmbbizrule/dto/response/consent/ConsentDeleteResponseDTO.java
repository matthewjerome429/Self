package com.cathaypacific.mmbbizrule.dto.response.consent;

import com.cathaypacific.mbcommon.dto.BaseResponseDTO;

public class ConsentDeleteResponseDTO extends BaseResponseDTO {

	private static final long serialVersionUID = 1671851018573930627L;

	private boolean consentInfoDelete = false;

	private int id;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean isConsentInfoDelete() {
		return consentInfoDelete;
	}

	public void setConsentInfoDelete(boolean consentInfoDelete) {
		this.consentInfoDelete = consentInfoDelete;
	}

}
