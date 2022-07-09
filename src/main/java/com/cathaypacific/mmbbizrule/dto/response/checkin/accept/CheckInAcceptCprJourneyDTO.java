package com.cathaypacific.mmbbizrule.dto.response.checkin.accept;

import java.util.List;

import com.cathaypacific.mbcommon.dto.BaseResponseDTO;
import com.cathaypacific.mbcommon.dto.error.ErrorInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class CheckInAcceptCprJourneyDTO extends BaseResponseDTO {

	private static final long serialVersionUID = 1004369919204121958L;
	
	/** journey Id */
	private String journeyId;
	
	private List<CheckInAcceptPassengerDTO> passengers;
	
	private List<CheckInAcceptCprSegmentDTO> segments;
	
	private List<CheckInAcceptPassengerSegmentDTO> passengerSegments;
	
	/** Indicate whether Mobile Boarding Pass is allowed (field for sent boarding pass)**/
	@JsonIgnore
	private Boolean allowMBP;
	
	/** Indicate whether BP is inhibited (field for sent boarding pass) **/
	@JsonIgnore
	private Boolean inhibitUSBP = false;
	
	public CheckInAcceptCprJourneyDTO() {
		super();
	}

	public CheckInAcceptCprJourneyDTO(List<ErrorInfo> errorInfos) {
		super();
		this.addAllErrors(errorInfos);
	}
	
	public CheckInAcceptCprJourneyDTO(List<ErrorInfo> errorInfos, String journeyId) {
		super();
		this.addAllErrors(errorInfos);
		this.journeyId = journeyId;
	}

	public String getJourneyId() {
		return journeyId;
	}

	public void setJourneyId(String journeyId) {
		this.journeyId = journeyId;
	}

	public List<CheckInAcceptPassengerDTO> getPassengers() {
		return passengers;
	}

	public void setPassengers(List<CheckInAcceptPassengerDTO> passengers) {
		this.passengers = passengers;
	}

	public List<CheckInAcceptCprSegmentDTO> getSegments() {
		return segments;
	}

	public void setSegments(List<CheckInAcceptCprSegmentDTO> segments) {
		this.segments = segments;
	}

	public List<CheckInAcceptPassengerSegmentDTO> getPassengerSegments() {
		return passengerSegments;
	}

	public void setPassengerSegments(List<CheckInAcceptPassengerSegmentDTO> passengerSegments) {
		this.passengerSegments = passengerSegments;
	}

	public Boolean getAllowMBP() {
		return allowMBP;
	}

	public void setAllowMBP(Boolean allowMBP) {
		this.allowMBP = allowMBP;
	}

	public Boolean getInhibitUSBP() {
		return inhibitUSBP;
	}

	public void setInhibitUSBP(Boolean inhibitUSBP) {
		this.inhibitUSBP = inhibitUSBP;
	}

	
}
