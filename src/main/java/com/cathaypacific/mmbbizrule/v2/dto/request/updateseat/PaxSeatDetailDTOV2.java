package com.cathaypacific.mmbbizrule.v2.dto.request.updateseat;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.validator.constraints.NotBlank;

import com.cathaypacific.mbcommon.constants.ValidationErrorConstants;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class PaxSeatDetailDTOV2 {
	@NotBlank(message = ValidationErrorConstants.REQUEST_VALIDATION_EMPTY_ERROR_CODE)
	private String passengerID;
	private String seatNo;
	private String seatPreference;
	private boolean exitSeat;
	private boolean exlSeat;
	@JsonIgnore
	private boolean asrSeat;
	@JsonIgnore
	private List<ExtraSeatDetailDTOV2> extraSeats;
	
	public String getPassengerID() {
		return passengerID;
	}
	public void setPassengerID(String passengerID) {
		this.passengerID = passengerID;
	}
	public String getSeatNo() {
		return seatNo;
	}
	public void setSeatNo(String seatNo) {
		this.seatNo = seatNo;
	}
	public String getSeatPreference() {
		return seatPreference;
	}
	public void setSeatPreference(String seatPreference) {
		this.seatPreference = seatPreference;
	}
	public boolean isExitSeat() {
		return exitSeat;
	}
	public void setExitSeat(boolean exitSeat) {
		this.exitSeat = exitSeat;
	}
	public boolean isExlSeat() {
		return exlSeat;
	}
	public void setExlSeat(boolean exlSeat) {
		this.exlSeat = exlSeat;
	}
	public boolean isAsrSeat() {
		return asrSeat;
	}
	public void setAsrSeat(boolean asrSeat) {
		this.asrSeat = asrSeat;
	}
	public List<ExtraSeatDetailDTOV2> getExtraSeats() {
		return extraSeats;
	}
	public List<ExtraSeatDetailDTOV2> findExtraSeats() {
		if(extraSeats == null) {
			extraSeats = new ArrayList<>();
		}
		return extraSeats;
	}
	public void setExtraSeats(List<ExtraSeatDetailDTOV2> extraSeats) {
		this.extraSeats = extraSeats;
	}
	
}
