package com.cathaypacific.mmbbizrule.dto.response.bookingproperties.properties;

import java.io.Serializable;

public class SeatPropertiesDTO implements Serializable{

	private static final long serialVersionUID = 8074390510351424677L;
	
	/** seat number */
	private String seatNo;
	
	/** if it is a extra legroom seat  */
	private Boolean exlSeat;
	
	/** if it is a regular seat */
	private Boolean asrSeat;
	
	private Boolean paid;
	
	/** seat status */
	private String status;
	
	private boolean isFromDCS;
	
	/** if it is a temporary seat stored in cache; this is for the OLCI checking in case*/
	private boolean tempSeat;

	public String getSeatNo() {
		return seatNo;
	}

	public void setSeatNo(String seatNo) {
		this.seatNo = seatNo;
	}

	public Boolean getExlSeat() {
		return exlSeat;
	}

	public void setExlSeat(Boolean exlSeat) {
		this.exlSeat = exlSeat;
	}

	public Boolean getAsrSeat() {
		return asrSeat;
	}

	public void setAsrSeat(Boolean asrSeat) {
		this.asrSeat = asrSeat;
	}

	public Boolean getPaid() {
		return paid;
	}

	public void setPaid(Boolean paid) {
		this.paid = paid;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public boolean isFromDCS() {
		return isFromDCS;
	}

	public void setFromDCS(boolean isFromDCS) {
		this.isFromDCS = isFromDCS;
	}

	public boolean isTempSeat() {
		return tempSeat;
	}

	public void setTempSeat(boolean tempSeat) {
		this.tempSeat = tempSeat;
	}
}
