package com.cathaypacific.mmbbizrule.dto.response.checkin.cancancel;

import java.util.List;

import com.cathaypacific.mbcommon.dto.BaseResponseDTO;

public class CanCancelCheckInDetailResponseDTO extends BaseResponseDTO {

	private static final long serialVersionUID = -399471346706634953L;

	private String journeyId;
	
	private List<CanCancelCheckInPassengerDTO> passengers;
	
	private List<CanCancelCheckInSegmentDTO> segments;

	public String getJourneyId() {
		return journeyId;
	}

	public void setJourneyId(String journeyId) {
		this.journeyId = journeyId;
	}

	public List<CanCancelCheckInPassengerDTO> getPassengers() {
		return passengers;
	}

	public void setPassengers(List<CanCancelCheckInPassengerDTO> passengers) {
		this.passengers = passengers;
	}

	public List<CanCancelCheckInSegmentDTO> getSegments() {
		return segments;
	}

	public void setSegments(List<CanCancelCheckInSegmentDTO> segments) {
		this.segments = segments;
	}
	
}
