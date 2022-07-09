package com.cathaypacific.mmbbizrule.dto.common.booking;

import io.swagger.annotations.ApiModelProperty;


public class SeatDTO {
	/** seat number */
	@ApiModelProperty("seat number")
	private String seatNo;
	/** if is a extra legroom seat */
	@ApiModelProperty("if the seat is a EXL seat")
	private Boolean exlSeat;
	/** if is a asr(regular) seat*/
	@ApiModelProperty("if the seat is a ASR seat")
	private Boolean asrSeat;
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
	private PaymentInfoDTO paymentInfo;
	
	public SeatDTO() {
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

	public PaymentInfoDTO getPaymentInfo() {
		return paymentInfo;
	}

	public void setPaymentInfo(PaymentInfoDTO paymentInfo) {
		this.paymentInfo = paymentInfo;
	}

}
