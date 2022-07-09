package com.cathaypacific.mmbbizrule.dto.request.updateseat;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class PaxSeatDetail {
	private String passengerID;
	private String seatNo;
	private String seatPreference;
	private boolean exlSeat;
	@JsonIgnore
	private boolean asrSeat;
	@JsonIgnore
	private List<ExtraSeatDetail> extraSeats;
	
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
	public List<ExtraSeatDetail> getExtraSeats() {
		return extraSeats;
	}
	public List<ExtraSeatDetail> findExtraSeats() {
		if(extraSeats == null) {
			extraSeats = new ArrayList<>();
		}
		return extraSeats;
	}
	public void setExtraSeats(List<ExtraSeatDetail> extraSeats) {
		this.extraSeats = extraSeats;
	}
	
}
