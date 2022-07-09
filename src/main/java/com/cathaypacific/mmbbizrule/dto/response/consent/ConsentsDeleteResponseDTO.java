package com.cathaypacific.mmbbizrule.dto.response.consent;

import java.util.List;

import com.cathaypacific.mbcommon.dto.BaseResponseDTO;

public class ConsentsDeleteResponseDTO extends BaseResponseDTO {

	private static final long serialVersionUID = 7047523541151973994L;

	private boolean consentInfoDelete = false;

	private List<Integer> ids;

	public boolean isConsentInfoDelete() {
		return consentInfoDelete;
	}

	public void setConsentInfoDelete(boolean consentInfoDelete) {
		this.consentInfoDelete = consentInfoDelete;
	}

	public List<Integer> getIds() {
		return ids;
	}

	public void setIds(List<Integer> ids) {
		this.ids = ids;
	}

}
