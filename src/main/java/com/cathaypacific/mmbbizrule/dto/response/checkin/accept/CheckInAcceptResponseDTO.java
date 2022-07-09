package com.cathaypacific.mmbbizrule.dto.response.checkin.accept;

import java.util.List;

import com.cathaypacific.mbcommon.dto.BaseResponseDTO;
import com.cathaypacific.mbcommon.dto.error.ErrorInfo;

public class CheckInAcceptResponseDTO extends BaseResponseDTO {
	
	private static final long serialVersionUID = 1699088169592187501L;
	
	/**
	 * Notice: only S error returned when do assignSeat call, this value would be false.
	 * */
	private boolean assignseatSuccess = true;
	
	/** any sector check-in success flag*/
	private boolean hasAcceptedSectorOfRequest = false;
	
	private CheckInAcceptCprJourneyDTO cprJourney;
	
	public CheckInAcceptResponseDTO() {
		super();
	}
	
	public CheckInAcceptResponseDTO(List<ErrorInfo> errorInfos, boolean assignseatSuccess) {
		super();
		this.addAllErrors(errorInfos);
		this.assignseatSuccess = assignseatSuccess;
	}
	
	public CheckInAcceptResponseDTO(ErrorInfo errorInfo, boolean assignseatSuccess) {
		super();
		this.addError(errorInfo);
		this.assignseatSuccess = assignseatSuccess;
	}
	
	public CheckInAcceptResponseDTO(ErrorInfo errorInfo) {
		super();
		this.addError(errorInfo);
	}
	
	public CheckInAcceptResponseDTO(List<ErrorInfo> errorInfos) {
		super();
		this.addAllErrors(errorInfos);
	}

	public boolean isAssignseatSuccess() {
		return assignseatSuccess;
	}

	public void setAssignseatSuccess(boolean assignseatSuccess) {
		this.assignseatSuccess = assignseatSuccess;
	}

	public boolean isHasAcceptedSectorOfRequest() {
		return hasAcceptedSectorOfRequest;
	}

	public void setHasAcceptedSectorOfRequest(boolean hasAcceptedSectorOfRequest) {
		this.hasAcceptedSectorOfRequest = hasAcceptedSectorOfRequest;
	}

	public CheckInAcceptCprJourneyDTO getCprJourney() {
		return cprJourney;
	}

	public void setCprJourney(CheckInAcceptCprJourneyDTO cprJourney) {
		this.cprJourney = cprJourney;
	}

}
