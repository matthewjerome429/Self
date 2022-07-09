package com.cathaypacific.mbcommon.model.common;

public class TempSeat {
	
	private String passengerId;
	
	private String segmentId;
	
	private String journeyId;
	
	private String seatNo;
	
	private String seatPreference;
	
	private boolean exlSeat;
	
	private String originalSeatNo;
	
	// if the seat has been forfeited from a paid EXL to regular seat
	private boolean forfeited;

	public String getPassengerId() {
		return passengerId;
	}

	public void setPassengerId(String passengerId) {
		this.passengerId = passengerId;
	}

	public String getSegmentId() {
		return segmentId;
	}

	public void setSegmentId(String segmentId) {
		this.segmentId = segmentId;
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

	public String getJourneyId() {
		return journeyId;
	}

	public void setJourneyId(String journeyId) {
		this.journeyId = journeyId;
	}

	public boolean isExlSeat() {
		return exlSeat;
	}

	public void setExlSeat(boolean exlSeat) {
		this.exlSeat = exlSeat;
	}

	public String getOriginalSeatNo() {
		return originalSeatNo;
	}

	public void setOriginalSeatNo(String originalSeatNo) {
		this.originalSeatNo = originalSeatNo;
	}

	public boolean isForfeited() {
		return forfeited;
	}

	public void setForfeited(boolean forfeited) {
		this.forfeited = forfeited;
	}
}
