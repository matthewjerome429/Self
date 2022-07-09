package com.cathaypacific.mmbbizrule.v2.dto.common.booking;

import java.util.List;

import com.cathaypacific.mbcommon.dto.BaseResponseDTO;
import com.cathaypacific.olciconsumer.model.response.CheckInOpenCloseTimeDTO;

public class JourneyDTOV2 extends BaseResponseDTO {

	private static final long serialVersionUID = -6511434072424003871L;

	/** journey Id */
	private String journeyId;
	
	/** display flag*/
	private Boolean canCheckIn;
	
	private List<CprJourneyPassengerDTOV2> passengers;
	
	private List<CprJourneySegmentDTOV2> segments;
	
	/** the relationship between passenger and segment in the same journey */
	private List<CprJourneyPassengerSegmentDTOV2> passengerSegments;

	/** The Journey in checkin window*/
	private boolean openToCheckIn;

	/** The Journey post checkin*/
	private boolean postCheckIn;
	
	/** Indicate whether Self Print Boarding Pass is allowed **/
	private Boolean allowSPBP;
	
	/** Indicate whether Mobile Boarding Pass is allowed **/
	private Boolean allowMBP;
	
	/** Indicate whether BP is inhibited (Has US Flight with Check-in time - Departure time >= 24) default value false **/
	private Boolean inhibitUSBP = false;
	
	/** The Journey withUS24h (Has US Flight with Departure time < 24h) **/
	private Boolean withUS24h;
	
	/** Indicate whether could go to one click check-in flow */
	private boolean occiEligible;

	/** The Journey in before check in window (priority check in) */
	private boolean beforeCheckIn;

	/** indicate whether the booking can perform priority check in */
	private boolean enabledPriorityCheckIn;

	/** current check in opening and closing time before the departure of the journey*/
	private CheckInOpenCloseTimeDTO openCloseTime;

	/** check in opening and closing time before the departure of the journey after upgrade member tier*/
	private CheckInOpenCloseTimeDTO nextOpenCloseTime;

	/** indicate whether the journey can check in but required upgrade the users' member tier*/
	private boolean canCheckInAfterUpgrade;

	/** Indicate whether the booking is allowed to enjoy the priority check in */
	private boolean priorityCheckInEligible;

	public String getJourneyId() {
		return journeyId;
	}

	public void setJourneyId(String journeyId) {
		this.journeyId = journeyId;
	}

	public Boolean isCanCheckIn() {
		return canCheckIn;
	}

	public void setCanCheckIn(Boolean canCheckIn) {
		this.canCheckIn = canCheckIn ;
	}

	public List<CprJourneyPassengerDTOV2> getPassengers() {
		return passengers;
	}

	public void setPassengers(List<CprJourneyPassengerDTOV2> passengers) {
		this.passengers = passengers;
	}

	public List<CprJourneySegmentDTOV2> getSegments() {
		return segments;
	}

	public void setSegments(List<CprJourneySegmentDTOV2> segments) {
		this.segments = segments;
	}

	public List<CprJourneyPassengerSegmentDTOV2> getPassengerSegments() {
		return passengerSegments;
	}

	public void setPassengerSegments(List<CprJourneyPassengerSegmentDTOV2> passengerSegments) {
		this.passengerSegments = passengerSegments;
	}

	public boolean isOpenToCheckIn() {
		return openToCheckIn;
	}

	public void setOpenToCheckIn(boolean openToCheckIn) {
		this.openToCheckIn = openToCheckIn;
	}

	public boolean isPostCheckIn() {
		return postCheckIn;
	}

	public void setPostCheckIn(boolean postCheckIn) {
		this.postCheckIn = postCheckIn;
	}

	public Boolean getAllowSPBP() {
		return allowSPBP;
	}

	public void setAllowSPBP(Boolean allowSPBP) {
		this.allowSPBP = allowSPBP;
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

	public Boolean getWithUS24h() {
		return withUS24h;
	}

	public void setWithUS24h(Boolean withUS24h) {
		this.withUS24h = withUS24h;
	}

	public boolean isOcciEligible() {
		return occiEligible;
	}

	public void setOcciEligible(boolean occiEligible) {
		this.occiEligible = occiEligible;
	}

	public boolean isBeforeCheckIn() {
		return beforeCheckIn;
	}

	public void setBeforeCheckIn(boolean beforeCheckIn) {
		this.beforeCheckIn = beforeCheckIn;
	}

	public boolean isEnabledPriorityCheckIn() {
		return enabledPriorityCheckIn;
	}

	public void setEnabledPriorityCheckIn(boolean enabledPriorityCheckIn) {
		this.enabledPriorityCheckIn = enabledPriorityCheckIn;
	}

	public CheckInOpenCloseTimeDTO getOpenCloseTime() {
		return openCloseTime;
	}

	public void setOpenCloseTime(CheckInOpenCloseTimeDTO openCloseTime) {
		this.openCloseTime = openCloseTime;
	}

	public CheckInOpenCloseTimeDTO getNextOpenCloseTime() {
		return nextOpenCloseTime;
	}

	public void setNextOpenCloseTime(CheckInOpenCloseTimeDTO nextOpenCloseTime) {
		this.nextOpenCloseTime = nextOpenCloseTime;
	}

	public boolean isCanCheckInAfterUpgrade() {
		return canCheckInAfterUpgrade;
	}

	public void setCanCheckInAfterUpgrade(boolean canCheckInAfterUpgrade) {
		this.canCheckInAfterUpgrade = canCheckInAfterUpgrade;
	}

	public boolean getPriorityCheckInEligible() {
		return priorityCheckInEligible;
	}

	public void setPriorityCheckInEligible(boolean priorityCheckInEligible) {
		this.priorityCheckInEligible = priorityCheckInEligible;
	}
}
