package com.cathaypacific.mmbbizrule.cxservice.changeflight.model;

import java.io.Serializable;
import java.util.List;

import com.cathaypacific.mmbbizrule.cxservice.changeflight.model.common.BaseResponseDTO;

public class ChangeFlightResponseDTO extends BaseResponseDTO implements Serializable{
	
	/**
	 * serial Version UID
	 */
	private static final long serialVersionUID = -3268959444143634290L;
	
    private boolean canChangeFlight;
	
	private boolean atcBooking;
	
	private String retrieveOfficeId;
	
	private String timeStamp;
	
	private List<SSRSKDTO> ssrList;
	
	private boolean hasHotel;
	
	private boolean hasEvent;
	
	private boolean hasASREXL;
	
	private boolean hasExtraBackage;
	
	private boolean hasInsurance;
	
	private boolean reminderSK;

	public boolean isCanChangeFlight() {
		return canChangeFlight;
	}

	public void setCanChangeFlight(boolean canChangeFlight) {
		this.canChangeFlight = canChangeFlight;
	}

	public boolean isAtcBooking() {
		return atcBooking;
	}

	public void setAtcBooking(boolean atcBooking) {
		this.atcBooking = atcBooking;
	}

	public String getRetrieveOfficeId() {
		return retrieveOfficeId;
	}

	public void setRetrieveOfficeId(String retrieveOfficeId) {
		this.retrieveOfficeId = retrieveOfficeId;
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	public List<SSRSKDTO> getSsrList() {
		return ssrList;
	}

	public void setSsrList(List<SSRSKDTO> ssrList) {
		this.ssrList = ssrList;
	}

	public boolean isHasHotel() {
		return hasHotel;
	}

	public void setHasHotel(boolean hasHotel) {
		this.hasHotel = hasHotel;
	}

	public boolean isHasEvent() {
		return hasEvent;
	}

	public void setHasEvent(boolean hasEvent) {
		this.hasEvent = hasEvent;
	}

	public boolean isHasASREXL() {
		return hasASREXL;
	}

	public void setHasASREXL(boolean hasASREXL) {
		this.hasASREXL = hasASREXL;
	}

	public boolean isHasExtraBackage() {
		return hasExtraBackage;
	}

	public void setHasExtraBackage(boolean hasExtraBackage) {
		this.hasExtraBackage = hasExtraBackage;
	}

	public boolean isHasInsurance() {
		return hasInsurance;
	}

	public void setHasInsurance(boolean hasInsurance) {
		this.hasInsurance = hasInsurance;
	}

	public boolean isReminderSK() {
		return reminderSK;
	}

	public void setReminderSK(boolean reminderSK) {
		this.reminderSK = reminderSK;
	}
	
}
