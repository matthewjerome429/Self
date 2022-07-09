package com.cathaypacific.mmbbizrule.dto.response.boardingpass.spbp;

import java.util.List;

import com.cathaypacific.mbcommon.dto.BaseResponseDTO;
import com.cathaypacific.mbcommon.dto.error.ErrorInfo;

public class SPBPResponseDTOV2 extends BaseResponseDTO {
	
	private static final long serialVersionUID = -8307913591636737111L;
	
	private String journeyId;
	
	private List<String> eligibleBoardingPassPassengerUcis;
	
	private String olciJsessionId;
	
	public SPBPResponseDTOV2() {
		super();
	}
	
	public SPBPResponseDTOV2(List<ErrorInfo> errorInfos, boolean success) {
		super();
		this.addAllErrors(errorInfos);
	}
	
	public SPBPResponseDTOV2(ErrorInfo errorInfo, boolean success) {
		super();
		this.addError(errorInfo);
	}
	
	public SPBPResponseDTOV2(ErrorInfo errorInfo) {
		super();
		this.addError(errorInfo);
	}
	
	public SPBPResponseDTOV2(List<ErrorInfo> errorInfos) {
		super();
		this.addAllErrors(errorInfos);
	}

	public String getJourneyId() {
		return journeyId;
	}

	public void setJourneyId(String journeyId) {
		this.journeyId = journeyId;
	}

	public List<String> getEligibleBoardingPassPassengerUcis() {
		return eligibleBoardingPassPassengerUcis;
	}

	public void setEligibleBoardingPassPassengerUcis(List<String> eligibleBoardingPassPassengerUcis) {
		this.eligibleBoardingPassPassengerUcis = eligibleBoardingPassPassengerUcis;
	}

	public String getOlciJsessionId() {
		return olciJsessionId;
	}

	public void setOlciJsessionId(String olciJsessionId) {
		this.olciJsessionId = olciJsessionId;
	}

}
