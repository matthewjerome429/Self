package com.cathaypacific.mmbbizrule.model.booking.detail;

import static com.cathaypacific.mbcommon.enums.flightstatus.FlightStatusEnum.CONFIRMED;
import static com.cathaypacific.mmbbizrule.constant.OLCIConstants.CPR_JOURNEY_NEED_UPGRADE_TO_CHECK_IN_ERROR_CODE;
import static com.cathaypacific.mmbbizrule.constant.OLCIConstants.CPR_JOURNEY_NOT_YET_CHECKIN_ERROR_CODE;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang.BooleanUtils;

import com.cathaypacific.mbcommon.constants.MMBConstants;
import com.cathaypacific.mbcommon.dto.error.ErrorInfo;
import com.cathaypacific.mbcommon.enums.error.ErrorTypeEnum;
import com.cathaypacific.olciconsumer.model.response.CheckInOpenCloseTimeDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class Journey {

	/** journey Id */
	private String journeyId;

	private boolean displayOnly;

	private List<CprJourneyPassenger> passengers;

	private List<CprJourneySegment> segments;

	/** the relationship between passenger and segment in the same journey */
	private List<CprJourneyPassengerSegment> passengerSegments;

	private List<ErrorInfo> errors;

	/** The Journey in checkin window*/
	private boolean openToCheckIn;

	/** The Journey post checkin*/
	private boolean postCheckIn;

	/** Indicate whether BP is inhibited (Has US Flight with Check-in time - Departure time >= 24) default value false **/
	private Boolean inhibitUSBP;

	/** Indicate whether Self Print Boarding Pass is allowed **/
	private Boolean allowSPBP;

	/** Indicate whether Mobile Boarding Pass is allowed **/
	private Boolean allowMBP;

	/** Indicate whether could go to one click check-in flow */
	private boolean occiEligible;

	/** Indicate whether the journey is before check in */
	private boolean beforeCheckIn;

	/** current check in opening and closing time before the departure of the journey*/
	private CheckInOpenCloseTimeDTO openCloseTime;

	/** check in opening and closing time before the departure of the journey after upgrade member tier*/
	private CheckInOpenCloseTimeDTO nextOpenCloseTime;

	/** Indicate whether the booking is allowed to enjoy the priority check in */
	private boolean priorityCheckInEligible;

	public String getJourneyId() {
		return journeyId;
	}

	public void setJourneyId(String journeyId) {
		this.journeyId = journeyId;
	}

	public boolean isDisplayOnly() {
		return displayOnly;
	}

	public void setDisplayOnly(boolean displayOnly) {
		this.displayOnly = displayOnly;
	}
	@JsonIgnore
	public boolean isCanCheckIn() {
		boolean hasNotYetCheckInError = this.hasNotYetCheckInError();
		return !displayOnly
				&& !hasNotYetCheckInError
				&& Optional.ofNullable(segments).orElse(new ArrayList<>()).stream().anyMatch(CprJourneySegment::getCanCheckIn)
				&& !this.hasNeedToUpgradeError()
				&& !containErrorWithType(ErrorTypeEnum.BUSERROR);
	}

	public boolean isCanCheckInAfterUPgrade() {
		return this.isCanCheckIn() || this.hasNeedToUpgradeError();
	}

	public List<CprJourneyPassenger> getPassengers() {
		return passengers;
	}

	public void setPassengers(List<CprJourneyPassenger> passengers) {
		this.passengers = passengers;
	}

	public List<CprJourneySegment> getSegments() {
		return segments;
	}

	public void setSegments(List<CprJourneySegment> segments) {
		this.segments = segments;
	}

	public List<CprJourneyPassengerSegment> getPassengerSegments() {
		return passengerSegments;
	}

	public void setPassengerSegments(List<CprJourneyPassengerSegment> passengerSegments) {
		this.passengerSegments = passengerSegments;
	}

	public List<ErrorInfo> getErrors() {
		return errors;
	}

	public void setErrors(List<ErrorInfo> errors) {
		this.errors = errors;
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

	public boolean isBeforeCheckIn() {
		return this.beforeCheckIn;
	}

	public void setBeforeCheckIn(boolean isBeforeCheckIn) {
		this.beforeCheckIn = isBeforeCheckIn;
	}

	public void setPostCheckIn(boolean postCheckIn) {
		this.postCheckIn = postCheckIn;
	}

	public boolean isOcciEligible() {
		return occiEligible;
	}

	public void setOcciEligible(boolean occiEligible) {
		this.occiEligible = occiEligible;
	}

	public Boolean getInhibitUSBP() {
		return inhibitUSBP;
	}

	public void setInhibitUSBP(Boolean inhibitUSBP) {
		this.inhibitUSBP = inhibitUSBP;
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

	// to check whether this booking has been able to enjoy the priority check in
	public boolean isEnabledPriorityCheckIn() {
		if (this.openCloseTime == null) return false;
		if (this.nextOpenCloseTime == null) return true;
		return this.openCloseTime.getOpenTimeLimit() == this.nextOpenCloseTime.getOpenTimeLimit()
				&& this.openCloseTime.getCloseTimeLimit() == this.nextOpenCloseTime.getCloseTimeLimit();
	}

	// check whether the journey has not yet check in error code.
	private Boolean hasNotYetCheckInError() {
		return Optional.ofNullable(this.errors).orElse(new ArrayList<>()).stream().anyMatch(e -> e != null && CPR_JOURNEY_NOT_YET_CHECKIN_ERROR_CODE.equals(e.getErrorCode()));
	}

	// check whether the journey has entered check in window but need to upgrade member
	private Boolean hasNeedToUpgradeError() {
		return Optional.ofNullable(this.errors).orElse(new ArrayList<>()).stream().anyMatch(e -> e != null && CPR_JOURNEY_NEED_UPGRADE_TO_CHECK_IN_ERROR_CODE.equals(e.getErrorCode()));
	}
	
	/**
	 * Journey contains any error with type
	 * 
	 * @param errorType
	 * @return 
	 */
	private boolean containErrorWithType(ErrorTypeEnum errorType) {
		if(errorType == null) {
			return false;
		}
		return Optional.ofNullable(this.errors).orElse(new ArrayList<>()).stream()
				.anyMatch(e -> e != null && e.getType() != null && errorType.getType().equals(e.getType().getType()));
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

	public boolean getPriorityCheckInEligible() {
		return priorityCheckInEligible;
	}

	public boolean isPriorityCheckInEligible(Segment firstSegment, boolean isStaffBooking) {
		if (firstSegment == null) return false;
		boolean isConfirmedFlight = firstSegment.getSegmentStatus() != null && firstSegment.getSegmentStatus().getStatus() == CONFIRMED;
		boolean isOperatedByCXKA = firstSegment.getOperateCompany() != null
                && (firstSegment.getOperateCompany().equals(MMBConstants.COMPANY_CX) || firstSegment.getOperateCompany().equals(MMBConstants.COMPANY_KA));
		boolean nonFlownFlight = BooleanUtils.isNotTrue(firstSegment.isFlown());
		return isConfirmedFlight && isOperatedByCXKA && nonFlownFlight && !isStaffBooking;
	}

	public void setPriorityCheckInEligible(boolean priorityCheckInEligible) {
		this.priorityCheckInEligible = priorityCheckInEligible;
	}
}
