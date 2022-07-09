package com.cathaypacific.mmbbizrule.dto.response.bookingproperties.properties;

import java.io.Serializable;

public class PassengerSegmentPropertiesDTO implements Serializable{

	private static final long serialVersionUID = -3975605477604381130L;

	/** passenger id */
	private String passengerId;
	
	/** segment id */
	private String segmentId;
	
	/** issued e-ticket */
	private Boolean eticketIssued;
	
	/** has CX/KA e-ticket */
	private Boolean cxKaET;

	/** there is waiver baggage */
	private Boolean haveWaiverBaggage;
	
	private SeatPropertiesDTO seat;
	
	private SeatPropertiesDTO pnrSeat;
	
	private SeatPropertiesDTO cprSeat;
	
	private SeatPreferencePropertiesDTO seatPreference;
	
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

	public Boolean isEticketIssued() {
		return eticketIssued;
	}

	public void setEticketIssued(Boolean eticketIssued) {
		this.eticketIssued = eticketIssued;
	}

	public Boolean isCxKaET() {
		return cxKaET;
	}

	public void setCxKaET(Boolean cxKaET) {
		this.cxKaET = cxKaET;
	}

	public Boolean getHaveWaiverBaggage() {
		return haveWaiverBaggage;
	}

	public void setHaveWaiverBaggage(Boolean haveWaiverBaggage) {
		this.haveWaiverBaggage = haveWaiverBaggage;
	}

	public SeatPropertiesDTO getSeat() {
		return seat;
	}

	public void setSeat(SeatPropertiesDTO seat) {
		this.seat = seat;
	}

	public SeatPropertiesDTO getPnrSeat() {
		return pnrSeat;
	}

	public void setPnrSeat(SeatPropertiesDTO pnrSeat) {
		this.pnrSeat = pnrSeat;
	}

	public SeatPropertiesDTO getCprSeat() {
		return cprSeat;
	}

	public void setCprSeat(SeatPropertiesDTO cprSeat) {
		this.cprSeat = cprSeat;
	}

	public SeatPreferencePropertiesDTO getSeatPreference() {
		return seatPreference;
	}

	public void setSeatPreference(SeatPreferencePropertiesDTO seatPreference) {
		this.seatPreference = seatPreference;
	}
	
}
