package com.cathaypacific.mmbbizrule.dto.response.checkin.boardingpass;

import java.util.List;

import com.cathaypacific.mbcommon.dto.BaseResponseDTO;

public class SendBoardingPassResponseDTO extends BaseResponseDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private List<String> eligibleBoardingPassPassengerUcis;
	
	private boolean sendEmailBPSuccessFlag;
	private boolean sendEmailSMSSuccessFlag;

	public List<String> getEligibleBoardingPassPassengerUcis() {
		return eligibleBoardingPassPassengerUcis;
	}

	public void setEligibleBoardingPassPassengerUcis(List<String> eligibleBoardingPassPassengerUcis) {
		this.eligibleBoardingPassPassengerUcis = eligibleBoardingPassPassengerUcis;
	}

	public boolean isSendEmailBPSuccessFlag() {
		return sendEmailBPSuccessFlag;
	}

	public void setSendEmailBPSuccessFlag(boolean sendEmailBPSuccessFlag) {
		this.sendEmailBPSuccessFlag = sendEmailBPSuccessFlag;
	}

	public boolean isSendEmailSMSSuccessFlag() {
		return sendEmailSMSSuccessFlag;
	}

	public void setSendEmailSMSSuccessFlag(boolean sendEmailSMSSuccessFlag) {
		this.sendEmailSMSSuccessFlag = sendEmailSMSSuccessFlag;
	}

}
