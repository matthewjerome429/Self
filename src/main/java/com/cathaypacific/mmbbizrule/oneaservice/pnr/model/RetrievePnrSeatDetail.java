package com.cathaypacific.mmbbizrule.oneaservice.pnr.model;

import java.util.List;

public class RetrievePnrSeatDetail {
	/** seat number */
	private String seatNo;
	
	/** seat characteristic */
	private List<String> seatCharacteristics;
	
	/** seat pax indicator */
	private String indicator;
	
	/** is paid */
	private Boolean paid;
	
	/** seat status */
	private String status;
	
	/** seat crossRef */
	private String crossRef;
	
	/** payment info of this seat, the payment info may be share with others, the same payment has same qualifierId */
	private RetrievePnrPaymentInfo paymentInfo;
	
	public RetrievePnrSeatDetail() {
		this.paid = false;
	}
	
	public String getSeatNo() {
		return seatNo;
	}
	public void setSeatNo(String seatNo) {
		this.seatNo = seatNo;
	}
	public List<String> getSeatCharacteristics() {
		return seatCharacteristics;
	}
	public void setSeatCharacteristics(List<String> seatCharacteristics) {
		this.seatCharacteristics = seatCharacteristics;
	}
	public Boolean isPaid() {
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
	public String getCrossRef() {
		return crossRef;
	}
	public void setCrossRef(String crossRef) {
		this.crossRef = crossRef;
	}
	public String getIndicator() {
		return indicator;
	}
	public void setIndicator(String indicator) {
		this.indicator = indicator;
	}

	public RetrievePnrPaymentInfo getPaymentInfo() {
		return paymentInfo;
	}

	public void setPaymentInfo(RetrievePnrPaymentInfo paymentInfo) {
		this.paymentInfo = paymentInfo;
	}
	
	
}
