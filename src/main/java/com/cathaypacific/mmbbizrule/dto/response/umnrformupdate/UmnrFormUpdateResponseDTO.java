package com.cathaypacific.mmbbizrule.dto.response.umnrformupdate;

import com.cathaypacific.oneaconsumer.model.response.pnracc_16_1_1a.PNRReply;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class UmnrFormUpdateResponseDTO {
	
	private boolean success;
	
	@JsonIgnore
	private PNRReply pnrReply;

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public PNRReply getPnrReply() {
		return pnrReply;
	}

	public void setPnrReply(PNRReply pnrReply) {
		this.pnrReply = pnrReply;
	}
}
