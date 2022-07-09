package com.cathaypacific.mmbbizrule.cxservice.olci.model;

import java.io.Serializable;

public class Seat implements Serializable {

	/** Serial Version UID */
	private static final long serialVersionUID = -7297498996396860060L;

	/** Seat number */
	private String seatNum;

	/** Indicate whether the seat is exit seat */
	private boolean exitSeat;

	/** Indicate whether the seat is extra leg room seat */
	private boolean extraLegRoomSeat;

	/**
	 * Indicate whether the seat is chargeable to the passenger. Note that
	 * chargeable seat could be non-chargeable to top-tier member
	 */
	private boolean chargeable;

	/** Indicate whether the seat has been paid */
	private boolean paid;

	/** Seat payment status */
	private String paymentStatus;

	public String getSeatNum() {
		return seatNum;
	}

	public void setSeatNum(String seatNum) {
		this.seatNum = seatNum;
	}

	public boolean isExitSeat() {
		return exitSeat;
	}

	public void setExitSeat(boolean exitSeat) {
		this.exitSeat = exitSeat;
	}

	public boolean isExtraLegRoomSeat() {
		return extraLegRoomSeat;
	}

	public void setExtraLegRoomSeat(boolean extraLegRoomSeat) {
		this.extraLegRoomSeat = extraLegRoomSeat;
	}

	public boolean isChargeable() {
		return chargeable;
	}

	public void setChargeable(boolean chargeable) {
		this.chargeable = chargeable;
	}

	public boolean isPaid() {
		return paid;
	}

	public void setPaid(boolean paid) {
		this.paid = paid;
	}

	public String getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}
}
