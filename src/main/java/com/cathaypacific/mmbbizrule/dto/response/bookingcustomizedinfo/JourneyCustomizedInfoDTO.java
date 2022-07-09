package com.cathaypacific.mmbbizrule.dto.response.bookingcustomizedinfo;

import java.util.List;

import com.cathaypacific.mbcommon.dto.BaseResponseDTO;

public class JourneyCustomizedInfoDTO extends BaseResponseDTO {

	private static final long serialVersionUID = -6511434072424003871L;

	/** journey Id */
	private String journeyId;
	
	/** the relationship between passenger and segment in the same journey */
	private List<CprJourneySegmentCustomizedInfoDTO> segments;
	
	/** the relationship between passenger and segment in the same journey */
	private List<CprJourneyPassengerSegmentCustomizedInfoDTO> passengerSegments;
	
	/** The Journey in checkin window*/
	private boolean openToCheckIn;
	
	/** The journey can checkin */
	private boolean canCheckIn;
	
	/** Indicate whether the journey is before check in */
	private boolean beforeCheckIn;
	
	/** Indicate whether the booking is allowed to enjoy the priority check in */
	private boolean priorityCheckInEligible;
	
	/** Indicate whether the booking has been able to enjoy the priority check in */
	private boolean enabledPriorityCheckIn;
	
	private boolean canCheckInAfterUpgrade;
	
	public String getJourneyId() {
		return journeyId;
	}

	public void setJourneyId(String journeyId) {
		this.journeyId = journeyId;
	}

	public List<CprJourneySegmentCustomizedInfoDTO> getSegments() {
		return segments;
	}

	public void setSegments(List<CprJourneySegmentCustomizedInfoDTO> segments) {
		this.segments = segments;
	}

	public List<CprJourneyPassengerSegmentCustomizedInfoDTO> getPassengerSegments() {
		return passengerSegments;
	}

	public void setPassengerSegments(List<CprJourneyPassengerSegmentCustomizedInfoDTO> passengerSegments) {
		this.passengerSegments = passengerSegments;
	}

	public boolean isOpenToCheckIn() {
		return openToCheckIn;
	}

	public void setOpenToCheckIn(boolean openToCheckIn) {
		this.openToCheckIn = openToCheckIn;
	}

	public boolean isCanCheckIn() {
		return canCheckIn;
	}

	public void setCanCheckIn(boolean canCheckIn) {
		this.canCheckIn = canCheckIn;
	}

	public boolean isBeforeCheckIn() {
		return beforeCheckIn;
	}

	public void setBeforeCheckIn(boolean beforeCheckIn) {
		this.beforeCheckIn = beforeCheckIn;
	}

	public boolean isPriorityCheckInEligible() {
		return priorityCheckInEligible;
	}

	public void setPriorityCheckInEligible(boolean priorityCheckInEligible) {
		this.priorityCheckInEligible = priorityCheckInEligible;
	}

	public boolean isEnabledPriorityCheckIn() {
		return enabledPriorityCheckIn;
	}

	public void setEnabledPriorityCheckIn(boolean enabledPriorityCheckIn) {
		this.enabledPriorityCheckIn = enabledPriorityCheckIn;
	}

	public boolean isCanCheckInAfterUpgrade() {
		return canCheckInAfterUpgrade;
	}

	public void setCanCheckInAfterUpgrade(boolean canCheckInAfterUpgrade) {
		this.canCheckInAfterUpgrade = canCheckInAfterUpgrade;
	}
	
}
