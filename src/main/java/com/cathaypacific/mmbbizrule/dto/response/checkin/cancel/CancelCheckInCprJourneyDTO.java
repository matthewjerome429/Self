package com.cathaypacific.mmbbizrule.dto.response.checkin.cancel;

import java.util.List;

import com.cathaypacific.mbcommon.dto.BaseResponseDTO;

public class CancelCheckInCprJourneyDTO extends BaseResponseDTO {
	
	private static final long serialVersionUID = 4888939586762158463L;

	/** journey Id */
	private String journeyId;
	
	private List<CancelCheckInPassengerDTO> passengers;
	
	private List<CancelCheckInSegmentDTO> segments;

	public String getJourneyId() {
		return journeyId;
	}

	public void setJourneyId(String journeyId) {
		this.journeyId = journeyId;
	}

	public List<CancelCheckInPassengerDTO> getPassengers() {
		return passengers;
	}

	public void setPassengers(List<CancelCheckInPassengerDTO> passengers) {
		this.passengers = passengers;
	}

	public List<CancelCheckInSegmentDTO> getSegments() {
		return segments;
	}

	public void setSegments(List<CancelCheckInSegmentDTO> segments) {
		this.segments = segments;
	}

}
