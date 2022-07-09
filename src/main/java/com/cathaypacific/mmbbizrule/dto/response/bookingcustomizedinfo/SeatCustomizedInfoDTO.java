package com.cathaypacific.mmbbizrule.dto.response.bookingcustomizedinfo;

import io.swagger.annotations.ApiModelProperty;

public class SeatCustomizedInfoDTO {
	
	/** seat number */
	@ApiModelProperty("seat number")
	private String seatNo;
	/** if is a extra legroom seat */
	@ApiModelProperty("if the seat is an EXL seat")
	private Boolean exlSeat;
	/** if is a asr(regular) seat*/
	@ApiModelProperty("if the seat is an ASR seat")
	private Boolean asrSeat;
	/**if the seat is a window seat */
	@ApiModelProperty("if the seat is an window seat")
	private Boolean windowSeat;
	/** if is an aisle seat */
	@ApiModelProperty("if the seat is an aisle seat")
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
	/** seat payment status */
	@ApiModelProperty("seat payment status")
	private String paymentStatus;
	/** if is from DCS */
	@ApiModelProperty("if the seat is from DCS")
	private boolean fromDCS;
	
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

	public String getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}
	public boolean isFromDCS() {
		return fromDCS;
	}
	public void setFromDCS(boolean fromDCS) {
		this.fromDCS = fromDCS;
	}
}
