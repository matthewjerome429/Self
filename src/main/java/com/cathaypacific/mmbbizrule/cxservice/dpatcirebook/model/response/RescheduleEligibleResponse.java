package com.cathaypacific.mmbbizrule.cxservice.dpatcirebook.model.response;


import com.cathaypacific.mbcommon.dto.BaseResponseDTO;

public class RescheduleEligibleResponse extends BaseResponseDTO {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private boolean eligible;

    private boolean success;
    
    private String sessionID;

	public boolean isEligible() {
		return eligible;
	}

	public void setEligible(boolean eligible) {
		this.eligible = eligible;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getSessionID() {
		return sessionID;
	}

	public void setSessionID(String sessionID) {
		this.sessionID = sessionID;
	}
}
