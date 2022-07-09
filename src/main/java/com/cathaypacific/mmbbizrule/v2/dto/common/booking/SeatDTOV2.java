package com.cathaypacific.mmbbizrule.v2.dto.common.booking;

import io.swagger.annotations.ApiModelProperty;


public class SeatDTOV2 {
	/** seat number */
	@ApiModelProperty("seat number")
	private String seatNo;
	/** if is a extra legroom seat */
	@ApiModelProperty("if the seat is a EXL seat")
	private Boolean exlSeat;
	/** if is a asr(regular, window, aisle) seat*/
	@ApiModelProperty("if the seat is a ASR seat")
	private Boolean asrSeat;
	/** if is a window seat */
	@ApiModelProperty("if the seat is a window seat")
	private Boolean windowSeat;
	/** if is an aisle seat */
	@ApiModelProperty("if the seat is a aisle seat")
	private Boolean aisleSeat;
	/** if is paid */
	@ApiModelProperty("if there is payment info (i.e. FA/FHD) associated to the seat")
	private Boolean paid;
	/** if is a free seat */
	@ApiModelProperty("if the seat is free for the passenger")
	private Boolean freeSeat;
	/** seat status */
	@ApiModelProperty("seat status")
	private String status;
	
	private boolean isFromDCS;
	@ApiModelProperty("the payment info (i.e. FA/FHD) associated to the seat, will be null if the seat is not paid")
	private PaymentInfoDTOV2 paymentInfo;
	/** if it is a temporary seat stored in cache; this is for the OLCI checking in case */
	@ApiModelProperty("if it is a temporary seat stored in cache; this is for the OLCI checking in case ")
	private boolean tempSeat;
	
	public SeatDTOV2() {
		this.paid = false;
	}
	
	public String getSeatNo() {
		return seatNo;
	}
	public void setSeatNo(String seatNo) {
		this.seatNo = seatNo;
	}
	
	public Boolean isExlSeat() {
		return exlSeat;
	}
	
	public void setExlSeat(Boolean exlSeat) {
		this.exlSeat = exlSeat;
	}
	
	public Boolean isAsrSeat() {
		return asrSeat;
	}

	public void setAsrSeat(Boolean asrSeat) {
		this.asrSeat = asrSeat;
	}

	public Boolean isWindowSeat() {
		return windowSeat;
	}

	public void setWindowSeat(Boolean windowSeat) {
		this.windowSeat = windowSeat;
	}

	public Boolean isAisleSeat() {
		return aisleSeat;
	}

	public void setAisleSeat(Boolean aisleSeat) {
		this.aisleSeat = aisleSeat;
	}

	public Boolean isPaid() {
		return paid;
	}
	
	public void setPaid(Boolean paid) {
		this.paid = paid;
	}

	public Boolean isFreeSeat() {
		return freeSeat;
	}

	public void setFreeSeat(Boolean freeSeat) {
		this.freeSeat = freeSeat;
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

	public PaymentInfoDTOV2 getPaymentInfo() {
		return paymentInfo;
	}

	public void setPaymentInfo(PaymentInfoDTOV2 paymentInfo) {
		this.paymentInfo = paymentInfo;
	}

	public boolean isTempSeat() {
		return tempSeat;
	}

	public void setTempSeat(boolean tempSeat) {
		this.tempSeat = tempSeat;
	}
	
}
